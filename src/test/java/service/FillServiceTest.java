package service;

import dao.DataAccessException;
import dao.Database;

import dao.UserDAO;
import dao.PersonDAO;
import dao.EventDAO;

import model.User;
import model.Person;
import model.Event;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.Math;
import result.Result;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class FillServiceTest {
    Database db = new Database();
    EventDAO eventDAO;
    PersonDAO personDAO;
    UserDAO userDAO;
    User user;
    FillService fillService;

    @BeforeEach
    public void setup() throws DataAccessException {

        db.openConnection();
        db.clearTables();
        db.closeConnection(true);


        user = new User("jb", "hello", "jb@music.com", "Justin", "Beiber", 'm', "98909d8d6fs");
        userDAO = new UserDAO(db.getConnection());

        userDAO.insert(user);
        db.closeConnection(true);

        fillService = new FillService();

    }

    @AfterEach
    public void teardown() throws DataAccessException {

    }

    @Test
    public void basicDefaultGenerationPass() throws IOException, DataAccessException {
        fillService.processRequest(user.getUsername(), null);
        runAmountTesting(4);
        runTimeLineTesting(4);
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
        int marriageYear = eventDAO.findEventOfPerson(mother.getPersonID(), "marriage").getYear();
        int fatherMarriageYear = eventDAO.findEventOfPerson(father.getPersonID(), "marriage").getYear();
        minYear = motherBirthYear + tolerance;
        assertEquals(marriageYear, fatherMarriageYear);
        assertTrue(minYear <= marriageYear);
        minYear = fatherBirthYear + tolerance;
        assertTrue(minYear <= marriageYear);

        //Check that parents don't die before child is born
        int motherDeathYear = eventDAO.findEventOfPerson(mother.getPersonID(), "death").getYear();
        int fatherDeathYear = eventDAO.findEventOfPerson(father.getPersonID(), "death").getYear();
        assertTrue(childBirthYear < fatherDeathYear);
        assertTrue(childBirthYear < motherDeathYear);

        //Check that woman don't give birth when older than 50

    }

    private void runAmountTesting(int iterations) throws DataAccessException {
        //Check the number of people associated to the user. Expected =
        personDAO = new PersonDAO(db.getConnection());
        ArrayList<Person> associatedPeople = personDAO.getAllAssociated(user.getUsername());

        int numExpectedPeople = calculateExpectedPeople(iterations);
        int numExpectedEvents = (numExpectedPeople * 3) - 2;

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



    /*
    @Test
    public void dataToDeleteDefaultGenerationPass() {

    }

    @Test
    public void basicCustomGenerationPass() {

    }

    @Test
    public void dataToDeleteCustomGenerationPass() {

    }

    @Test
    public void unknownUsernameFail() {

    }

    @Test
    public void missingArgumentFail() {

    }
    */
}
