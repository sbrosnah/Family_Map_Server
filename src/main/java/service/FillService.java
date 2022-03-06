package service;

import com.google.gson.Gson;
import dao.*;
import result.Result;

import model.Person;
import model.User;
import model.Event;
import model.Location;
import model.ObjectCatalog;
import model.Catalog;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.io.File;
import java.util.List;
import java.util.Random;

public class FillService {
    private static Logger logger = Logger.getLogger("FillService");

    Database db;

    UserDAO userDAO;
    PersonDAO personDAO;
    EventDAO eventDAO;

    Result result;

    //This is the root user. This username will be used in all 'associatedUsername' fields
    User user;

    List<Location> locations;
    List<String> femaleNames;
    List<String> maleNames;
    List<String> surnames;

    boolean firstMarriageYearSet;
    int originalGenerations;
    int totalPeople;
    int totalEvents;

    int defaultGen = 4;
    int presentYear = 2022;

    int deathBinWidth;
    int marriageBinWidth;
    int childHoodBinWidth;
    int birthBinWidth;
    int maxLifeSpan;




    Random randomizer = new Random();

    String locationsFilePath = "json/locations.json";
    String femaleNamesFilePath = "json/fnames.json";
    String maleNamesFilePath = "json/mnames.json";
    String surnamesFilePath = "json/snames.json";




    //MAIN FUNCTION
    /**
     * fill the Ancestors of a given person
     * @param username
     * @param generations
     * @return a result object
     */
    public Result processRequest(String username, Integer generations) throws IOException {

        //Things to Check:
        //validity of generations parameter
        //If the username is in the database
            //If it is, all data associated with that username (in the person and event tables) must be deleted

        logger.info("Processing request");

        db = new Database();

        result = new Result();

        //initializing the count of total events and people added to the database
        totalEvents = 0;
        totalPeople = 0;

        firstMarriageYearSet = false;

        setEventBins();

        //Loading the JSON data for randon names and locations
        loadData();

        //Determining if a specified number of generations were passed in
        if (generations == null){
            generations = defaultGen;
        }

        originalGenerations = generations;

        try {
            logger.info("Checking parameter information");
            if(username != null && generations >= 0){
                logger.info("Checking if the user is in the database");
                if(checkIfUsernameExists(username)){
                    logger.info("Checking if there is data associated with the user");
                    deleteAllRelatedInfo();
                    fillGenerations(generations);

                    //If we made it to this point, we can say that it was a success
                    result.setSuccess(true);
                    result.setMessage("Successfully added " + totalPeople + " persons and " + totalEvents + " events to the database.");
                }else {
                   throw new Exception("Username not found in database");
                }
            } else {
                throw new Exception("Bad parameters passed in");
            }
        } catch (Exception ex){
            ex.printStackTrace();
            logger.info("exception thrown");
            result.setSuccess(false);
            result.setMessage("Error: " + ex.getMessage());
            logger.info(result.toString());
        } finally {
            try {
                Connection conn = db.getConnection();
                if (!conn.isClosed()) {
                    db.closeConnection(false);
                }
            } catch(SQLException ex){
                logger.info(ex.getMessage());
            } catch(DataAccessException ex){
                logger.info(ex.getMessage());
            }
        }
        return result;
    }





    //FILL FUNCTIONS

    private void fillGenerations(int generations) throws DataAccessException, Exception {
        Gender gender = getUserGender();
        int iterationCounter = 0;
        Person userPersonObject = generatePerson(gender, generations, iterationCounter);
        insertPerson(userPersonObject);
    }

    private Person generatePerson(Gender gender, int generations, int iterationCounter) throws DataAccessException, Exception{

        Person mother = null;
        Person father = null;

        if(generations > 0) {
            mother = generatePerson(Gender.FEMALE, generations - 1, iterationCounter + 1);
            father = generatePerson(Gender.MALE, generations - 1, iterationCounter + 1);

            mother.setSpouseID(father.getPersonID());
            father.setSpouseID(mother.getPersonID());

            ArrayList<Event> marriageEvents = createMarriageEvents(mother, father, iterationCounter);

            insertEvent(marriageEvents.get(0));
            insertEvent(marriageEvents.get(1));

            insertPerson(mother);
            insertPerson(father);
        }

        //All of the person information for the person being generated in recursion iteration
        String personID;
        String associatedUsername;
        String firstName;
        String lastName;
        char personGender;
        String motherID;
        String fatherID;

        //If we are generating a person object for the root user, we will use the user info
        //else we generate random info
        if(iterationCounter == 0) {
            firstName = user.getFirstName();
            lastName = user.getLastName();
            personID = user.getPersonID();
        } else {
            firstName = getRandomFirstName(gender);
            lastName = getRandomLastName();
            personID = getRandomID();
        }

        //This information is generated the same way regardless of the iteration
        personGender = getGenderChar(gender);
        associatedUsername = user.getUsername();

        if(mother != null && father != null){
            motherID = mother.getPersonID();
            fatherID = father.getPersonID();
        } else {
            motherID = "";
            fatherID = "";
        }

        //We set spouseID to null so that it can be reset as the stack pops off commands when recursion is back-propogating
        Person person = new Person(personID, associatedUsername, firstName, lastName, personGender, fatherID, motherID,
                                    "");

        //now that the person is made, I need to create events for him/her
        generateLifeEvents(person, iterationCounter);

        return person;
    }




    //EVENT GENERATION FUNCTIONS

    private void setEventBins() {

        deathBinWidth = 60;
        marriageBinWidth = 18;
        childHoodBinWidth = 18;
        birthBinWidth = 5;

        maxLifeSpan = deathBinWidth + marriageBinWidth + childHoodBinWidth;

    }

    private void generateLifeEvents(Person person, int iteration) throws DataAccessException {
        //Birth and death will always be generated before marriage

        Event birthEvent = generateBirthEvent(person, iteration);

        if(iteration > 0){
            Event deathEvent = generateDeathEvent(person, iteration);
            insertEvent(deathEvent);
        }

        insertEvent(birthEvent);


    }

    private Event generateBirthEvent(Person person, int iteration) {
        int earliestPossibleBirth = presentYear - deathBinWidth - (childHoodBinWidth * (iteration + 1));
        int randNum = randomizer.nextInt(birthBinWidth);
        int birthYear = earliestPossibleBirth + randNum;
        String eventType = "birth";

        return generateGenericEvent(person, eventType, birthYear);
    }

    private Event generateGenericEvent(Person person, String eventType, int year) {
        String eventID = getRandomID();
        String associatedUsername = user.getUsername();
        Location location = getRandomLocation();
        float latitude = location.getLatitude();
        float longitude = location.getLongitude();
        String country = location.getCountry();
        String city = location.getCity();
        String personID = person.getPersonID();
        return new Event(eventID, associatedUsername, personID, latitude, longitude, country, city, eventType, year);
    }

    private Event generateDeathEvent(Person person, int iteration) {
        int earliestPossibleDeath = presentYear - deathBinWidth - (childHoodBinWidth * (iteration - 1));
        earliestPossibleDeath += marriageBinWidth;
        int randNum = randomizer.nextInt(deathBinWidth);
        int deathYear = earliestPossibleDeath + randNum;
        String eventType = "death";

        return generateGenericEvent(person, eventType, deathYear);
    }

    private Integer calculateMarriageYear(int iteration) {
        int earliestPossibleMarriage = presentYear - deathBinWidth - (childHoodBinWidth * iteration);
        int randNum = randomizer.nextInt(marriageBinWidth);
        int marriageYear = earliestPossibleMarriage + randNum;
        return marriageYear;
    }

    private ArrayList<Event> createMarriageEvents(Person mother, Person father, int iterations){

        //shared info
        String associatedUsername = user.getUsername();
        String eventType = "marriage";
        Integer year = calculateMarriageYear(iterations);
        Float latitude;
        Float longitude;
        String city;
        String country;

        //individual info
        String fatherEventID = getRandomID();
        String motherEventID = getRandomID();

        //get the location info needed
        Location location = getRandomLocation();
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        city = location.getCity();
        country = location.getCountry();

        Event fatherMarriageEvent = new Event(fatherEventID, associatedUsername, father.getPersonID(), latitude, longitude,
                                                country, city, eventType, year);
        Event motherMarriageEvent = new Event(motherEventID, associatedUsername, mother.getPersonID(), latitude, longitude,
                                                country, city, eventType, year);

        ArrayList<Event> events = new ArrayList<>();
        events.add(fatherMarriageEvent);
        events.add(motherMarriageEvent);

        return events;
    }





    //Randomizing Functions


    private String getRandomLastName() {
        int objectIndex = randomizer.nextInt(surnames.size());
        return surnames.get(objectIndex);
    }

    private String getRandomFirstName(Gender gender) throws Exception {
        int objectIndex;
        if(gender.equals(Gender.MALE)){
            objectIndex = randomizer.nextInt(maleNames.size());
            return maleNames.get(objectIndex);
        } else if (gender.equals(Gender.FEMALE)) {
            objectIndex = randomizer.nextInt(femaleNames.size());
            return femaleNames.get(objectIndex);
        } else {
            throw new Exception("Error in getRandomFirstName");
        }
    }

    private Location getRandomLocation() {
        //accesses a random location object from the locations List
        int objectIndex = randomizer.nextInt(locations.size());
        return locations.get(objectIndex);
    }





    //UTILITY FUNCTIONS

    private void insertPerson(Person person) throws DataAccessException {
        personDAO = new PersonDAO(db.getConnection());
        personDAO.insert(person);
        db.closeConnection(true);
        totalPeople++;
    }

    private void insertEvent(Event event) throws DataAccessException {
        eventDAO = new EventDAO(db.getConnection());
        eventDAO.insert(event);
        db.closeConnection(true);
        totalEvents++;
    }

    private char getGenderChar(Gender gender) throws Exception {
        char genderChar;
        if (gender.equals(Gender.FEMALE)){
            genderChar = 'f';
        } else if (gender.equals(Gender.MALE)) {
            genderChar = 'm';
        } else {
            throw new Exception("gender info not set correctly");
        }
        return genderChar;
    }

    private Gender getUserGender() throws Exception {
        Gender genderType;
        char gender = user.getGender();
        if(gender == 'f'){
            genderType = Gender.FEMALE;
        } else if (gender == 'm'){
            genderType = Gender.MALE;
        } else {
            throw new Exception("user has invalid gender");
        }
        return genderType;
    }

    private boolean checkIfUsernameExists(String username) throws DataAccessException {
        userDAO = new UserDAO(db.getConnection());
        user = userDAO.find(username);
        db.closeConnection(false);
        if(user != null) {
            return true;
        }
        return false;
    }

    private void deleteAllRelatedInfo() throws DataAccessException {
        eventDAO = new EventDAO(db.getConnection());
        eventDAO.deleteAllAssociated(user.getUsername());
        db.closeConnection(true);
        personDAO = new PersonDAO(db.getConnection());
        personDAO.deleteAllAssociated(user.getUsername());
        db.closeConnection(true);
    }

    private Integer findEarliestDeathDate(String motherID, String fatherID) throws DataAccessException {
        eventDAO = new EventDAO(db.getConnection());
        Event motherDeath = eventDAO.findEventOfPerson(motherID, "death");
        Event fatherDeath = eventDAO.findEventOfPerson(fatherID, "death");
        db.closeConnection(false);

        int motherDeathYear = motherDeath.getYear();
        int fatherDeathYear = fatherDeath.getYear();

        int earliest;
        if(motherDeathYear <= fatherDeathYear) {
            earliest = motherDeathYear;
        } else {
            earliest = fatherDeathYear;
        }
        return earliest;
    }

    private Integer findLatestBirthDate(String motherID, String fatherID) throws DataAccessException{
        eventDAO = new EventDAO(db.getConnection());
        Event motherBirth = eventDAO.findEventOfPerson(motherID, "birth");
        Event fatherBirth = eventDAO.findEventOfPerson(fatherID, "birth");
        db.closeConnection(false);

        int motherBirthDate = motherBirth.getYear();
        int fatherBirthDate = fatherBirth.getYear();

        int latest;
        if(fatherBirthDate >= motherBirthDate) {
            latest = fatherBirthDate;
        } else {
            latest = motherBirthDate;
        }
        return latest;
    }

    private String getRandomID() {
        return UUID.randomUUID().toString();
    }






    //FILE PARSING FUNCTIONS

    private void loadData() throws IOException {
        File locationsFile = new File(locationsFilePath);
        File femaleNamesFile = new File(femaleNamesFilePath);
        File maleNamesFile = new File(maleNamesFilePath);
        File surnamesFile = new File(surnamesFilePath);

        locations = parseLocations(locationsFile);
        femaleNames = parseFile(femaleNamesFile);
        maleNames = parseFile(maleNamesFile);
        surnames = parseFile(surnamesFile);
    }

    private List<Location> parseLocations(File locationsFile) throws IOException {
        try (FileReader fileReader = new FileReader(locationsFile);
             BufferedReader bufferedReader = new BufferedReader(fileReader)){

            Gson gson = new Gson();
            ObjectCatalog locationCatalog = gson.fromJson(bufferedReader, ObjectCatalog.class);
            return locationCatalog.getLocations();
        }
    }

    private List<String> parseFile(File file) throws IOException {
        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)){

            Gson gson = new Gson();
            Catalog catalog = gson.fromJson(bufferedReader, Catalog.class);
            return catalog.getList();
        }
    }
}
