package service;

import dao.DataAccessException;
import dao.Database;

import dao.UserDAO;
import dao.PersonDAO;
import dao.EventDAO;

import model.User;
import model.Person;
import model.Event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.Math;
import result.Result;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class FillServiceTest {
    Database db = new Database();
    EventDAO eventDAO;
    PersonDAO personDAO;
    UserDAO userDAO;
    User user;
    User userTwo;
    FillService fillService;
    Result result;
    Result resultTwo;

    @BeforeEach
    public void setup() throws DataAccessException {

        db.openConnection();
        db.clearTables();
        db.closeConnection(true);


        user = new User("jb", "hello", "jb@music.com", "Justin", "Beiber", 'm', "98909d8d6fs");
        userTwo = new User("sb", "hello", "jb@musicdsdfs.com", "Jusdsdftin", "Beibdfsder", 'm', "98909ddsdsdfs8d6fs");
        userDAO = new UserDAO(db.getConnection());

        userDAO.insert(user);
        db.closeConnection(true);

        fillService = new FillService();

    }

    @Test
    public void basicDefaultGenerationPass() throws IOException, DataAccessException {

        result = fillService.processRequest(user.getUsername(), null);
        runAmountTesting(4);
        runTimeLineTesting(4);
        checkResult(4);
    }


    @Test
    public void basicZeroGenerationPass() throws DataAccessException, IOException {
        int numGen = 0;
        result = fillService.processRequest(user.getUsername(), numGen);
        runAmountTesting(numGen);
        runTimeLineTesting(numGen);
        checkResult(0);
    }

    @Test
    public void basicFiveGenerationPass() throws DataAccessException, IOException {
        int numGen = 5;
        result = fillService.processRequest(user.getUsername(), numGen);
        runAmountTesting(numGen);
        runTimeLineTesting(numGen);
        checkResult(5);
    }

    @Test
    public void dataToDeleteDefaultGenerationPass() throws IOException, DataAccessException {
        userDAO = new UserDAO(db.getConnection());
        userDAO.insert(userTwo);
        db.closeConnection(true);

        result = fillService.processRequest(user.getUsername(), null);
        result = fillService.processRequest(user.getUsername(), 5);
        resultTwo = fillService.processRequest(userTwo.getUsername(), 0);

        eventDAO = new EventDAO(db.getConnection());
        ArrayList<Event> allEvents = eventDAO.getAll();
        ArrayList<Event> associatedEvents = eventDAO.getAllAssociated(user.getUsername());
        db.closeConnection(false);

        int expectedAssociated = calculateExpectedEvents(calculateExpectedPeople(5));
        int expectedTotal = expectedAssociated + 1;
        assertEquals(expectedAssociated, associatedEvents.size());
        assertEquals(expectedTotal, allEvents.size());
    }

    @Test
    public void unknownUsernameFail() throws IOException {
        result = fillService.processRequest("hahhahaha", null);
        assertFalse(result.isSuccess());
    }

    @Test
    public void missingArgumentFail() throws IOException {
        result = fillService.processRequest(null, null);
        assertFalse(result.isSuccess());
    }


    private void checkResult(int numGen) {
        assertTrue(result.isSuccess());
        int expectedPeople = calculateExpectedPeople(numGen);
        int expectedEvents = calculateExpectedEvents(expectedPeople);
        String desiredMessage = "Successfully added " + expectedPeople + " persons and " + expectedEvents + " events to the database.";
        assertTrue(desiredMessage.equals(result.getMessage()));
    }

    private void runTimeLineTesting(int numGenerations) throws DataAccessException {
        //Get the person obj for the user and the parents
        //compare relationships between all three
        int iteration = 0;
        runTimeLineTestingHelper(user.getPersonID(), numGenerations, iteration);
    }

    private void runTimeLineTestingHelper(String childID, int generations, int iteration) throws DataAccessException {
        if(generations > 0){
            personDAO = new PersonDAO(db.getConnection());
            Person child = personDAO.find(childID);
            Person mother = personDAO.find(child.getMotherID());
            Person father = personDAO.find(child.getFatherID());
            db.closeConnection(false);

            checkRelationships(child, mother, father, generations, iteration);

            runTimeLineTestingHelper(mother.getPersonID(), generations - 1, iteration + 1);
            runTimeLineTestingHelper(father.getPersonID(), generations - 1, iteration + 1);
        }
    }

    private void checkRelationships(Person child, Person mother, Person father, int generation, int iteration) throws DataAccessException {
        //Check the number of events in each
        eventDAO = new EventDAO(db.getConnection());
        ArrayList<Event> selectedChildEvents = eventDAO.getAllForPerson(child.getPersonID());
        //db.closeConnection(false);
        if(iteration == 0){
            assertEquals(1, selectedChildEvents.size());
        } else {
            assertEquals(3, selectedChildEvents.size());
        }
        //Check the birth dates
        int tolerance = 13;
        int childBirthYear = eventDAO.findEventOfPerson(child.getPersonID(), "birth").getYear();
        int motherBirthYear = eventDAO.findEventOfPerson(mother.getPersonID(), "birth").getYear();
        int fatherBirthYear = eventDAO.findEventOfPerson(father.getPersonID(), "birth").getYear();
        int minYear = childBirthYear - tolerance;
        assertTrue(motherBirthYear <= minYear);
        assertTrue(fatherBirthYear <= minYear);

        //Check the age of parents at marriage and that they are the same year
        Event motherMarriage = eventDAO.findEventOfPerson(mother.getPersonID(), "marriage");
        int motherMarriageYear = motherMarriage.getYear();
        Event fatherMarriage = eventDAO.findEventOfPerson(father.getPersonID(), "marriage");
        int fatherMarriageYear = fatherMarriage.getYear();
        minYear = motherBirthYear + tolerance;
        assertEquals(motherMarriageYear, fatherMarriageYear);
        assertTrue(minYear <= motherMarriageYear);
        minYear = fatherBirthYear + tolerance;
        assertTrue(minYear <= motherMarriageYear);

        //Check that parents don't die before child is born
        int motherDeathYear = eventDAO.findEventOfPerson(mother.getPersonID(), "death").getYear();
        int fatherDeathYear = eventDAO.findEventOfPerson(father.getPersonID(), "death").getYear();
        assertTrue(childBirthYear <= fatherDeathYear);
        assertTrue(childBirthYear <= motherDeathYear);

        //Check that woman don't give birth when older than 50
        tolerance = 50;
        int remainder = motherBirthYear - childBirthYear;
        assertTrue(remainder <= tolerance);


        //Check that birth events occur first for a person
        if(iteration != 0){
            int childMarriageYear = eventDAO.findEventOfPerson(child.getPersonID(), "marriage").getYear();
            int childDeathYear = eventDAO.findEventOfPerson(child.getPersonID(), "death").getYear();
            assertTrue(childBirthYear <= childMarriageYear && childBirthYear <= childDeathYear);
        }

        assertTrue(motherBirthYear <= motherMarriageYear && motherBirthYear <= fatherDeathYear);
        assertTrue(fatherBirthYear <= fatherMarriageYear && fatherBirthYear<= fatherDeathYear);

        //Check that death events occur last for a everyone chronologically
        int childDeathYear = 0;
        int childMarriageYear = 0;
        if(iteration != 0){
            childMarriageYear = eventDAO.findEventOfPerson(child.getPersonID(), "marriage").getYear();
            childDeathYear = eventDAO.findEventOfPerson(child.getPersonID(), "death").getYear();
            assertTrue(childDeathYear >= childMarriageYear && childDeathYear >= childBirthYear);
        }

        assertTrue(motherDeathYear >= motherMarriageYear && motherDeathYear >= motherBirthYear);
        assertTrue(fatherDeathYear >= fatherMarriageYear && fatherDeathYear >= fatherBirthYear);

        //Check that nobody dies at an age older than 120
        int maxLifeSpan = 120;
        if(iteration != 0){
            int childLifeSpan = childDeathYear - childMarriageYear;
            assertTrue(childLifeSpan < maxLifeSpan);
        }
        int motherLifeSpan = motherDeathYear - motherBirthYear;
        int fatherLifeSpan = fatherDeathYear - fatherBirthYear;
        assertTrue(motherLifeSpan < maxLifeSpan);
        assertTrue(fatherLifeSpan < maxLifeSpan);

        //Each person in a married couple must have their own marriage event with all the other info the same
        assertTrue(!motherMarriage.getEventID().equals(fatherMarriage.getEventID()));
        assertTrue(motherMarriage.getYear() == fatherMarriage.getYear());
        assertTrue(motherMarriage.getCity().equals(fatherMarriage.getCity()));
        assertTrue(motherMarriage.getCountry().equals(fatherMarriage.getCountry()));
        assertTrue(motherMarriage.getLatitude() == fatherMarriage.getLatitude());
        assertTrue(motherMarriage.getLongitude() == fatherMarriage.getLongitude());
        db.closeConnection(false);
    }

    private int calculateExpectedEvents(int numExpectedPeople){
        return (numExpectedPeople * 3) - 2;
    }

    private void runAmountTesting(int iterations) throws DataAccessException {
        //Check the number of people associated to the user. Expected =
        personDAO = new PersonDAO(db.getConnection());
        ArrayList<Person> associatedPeople = personDAO.getAllAssociated(user.getUsername());

        int numExpectedPeople = calculateExpectedPeople(iterations);
        int numExpectedEvents = calculateExpectedEvents(numExpectedPeople);

        assertEquals(numExpectedPeople, associatedPeople.size());
        assertEquals(associatedPeople.size(), personDAO.getAll().size());

        db.closeConnection(false);

        //I need to be sure that the size of events is correct
        eventDAO = new EventDAO(db.getConnection());
        ArrayList<Event> associatedEvents = eventDAO.getAllAssociated(user.getUsername());

        assertEquals(numExpectedEvents, associatedEvents.size());
        assertEquals(associatedEvents.size(), eventDAO.getAll().size());

        db.closeConnection(false);
    }

    private Integer calculateExpectedPeople(int n){
        int total = 0;
        for(int i = 0; i <= n; i++) {
            total += Math.pow(2, i);
        }
        return total;
    }
}
