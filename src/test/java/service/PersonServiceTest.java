package service;

import dao.*;

import model.Person;
import model.User;
import model.AuthToken;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import result.PersonResult;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;


public class PersonServiceTest {
    Database db;

    User user;

    UserDAO userDAO;
    PersonDAO personDAO;
    AuthTokenDAO authTokenDAO;

    PersonService personService;

    Person personOne;
    Person personTwo;

    User userOne;
    User userTwo;

    AuthToken authTokenOne;
    AuthToken authTokenTwo;
    AuthToken authTokenThree;

    @BeforeEach
    public void setup() throws DataAccessException {
        //I need to clear out the database
        db = new Database();
        db.openConnection();
        db.clearTables();
        //I need to generate dummy data to insert into the database
        personOne = new Person("123", "sbrosnah", "Spencer", "Brosnahan",
                                 'm', "899889", "kdkfjd", "kdkdkdkdkdkkd");
        personTwo = new Person("456", "ebarlow", "Jenny", "Baker", 'f',
                "", "", "");

        userOne = new User("sbrosnah", "hi", "mine", "Spencer", "Brosnahan",
                'm', "gggg");
        userTwo = new User("ebarlow", "hello", "hers", "Elisabeth", "Barlow",
                'f', "tttt");

        authTokenOne = new AuthToken("blah", "sbrosnah");
        authTokenTwo = new AuthToken("hehe", "ebarlow");
        authTokenThree = new AuthToken("moo", "unexistent");

        userDAO = new UserDAO(db.getConnection());
        userDAO.insert(userOne);
        userDAO.insert(userTwo);

        personDAO = new PersonDAO(db.getConnection());
        personDAO.insert(personOne);
        personDAO.insert(personTwo);

        authTokenDAO = new AuthTokenDAO(db.getConnection());
        authTokenDAO.insert(authTokenOne);
        authTokenDAO.insert(authTokenTwo);
        authTokenDAO.insert(authTokenThree);

        db.closeConnection(true);

    }

    @AfterEach
    public void teardown() throws DataAccessException, SQLException {
        if(!db.getConnection().isClosed()) {
            db.closeConnection(false);
        }
    }

    @Test
    public void processRequestPass() {
        personService = new PersonService();

        PersonResult result = personService.processRequest(personOne.getPersonID(), authTokenOne.getAuthtoken());
        assertNotNull(result);

        checkPassingResult(result, personOne);

    }

    @Test
    public void processRequestMissingValuesPass() {
        personService = new PersonService();

        PersonResult result = personService.processRequest(personTwo.getPersonID(), authTokenTwo.getAuthtoken());
        assertNotNull(result);

        assertNull(result.getMotherID());
        assertNull(result.getFatherID());
        assertNull(result.getSpouseID());

        assertEquals(personTwo.getPersonID(), result.getPersonID());
        assertEquals(personTwo.getAssociatedUsername(), result.getAssociatedUsername());
        assertEquals(personTwo.getFirstName(), result.getFirstName());
        assertEquals(personTwo.getLastName(), result.getLastName());
        assertEquals(String.valueOf(personTwo.getGender()), result.getGender());
        assertTrue(result.isSuccess());
        assertNull(result.getMessage());
    }

    private void checkPassingResult(PersonResult result, Person person) {
        assertEquals(person.getPersonID(), result.getPersonID());
        assertEquals(person.getAssociatedUsername(), result.getAssociatedUsername());
        assertEquals(person.getFirstName(), result.getFirstName());
        assertEquals(person.getLastName(), result.getLastName());
        assertEquals(String.valueOf(person.getGender()), result.getGender());
        assertEquals(person.getFatherID(), result.getFatherID());
        assertEquals(person.getMotherID(), result.getMotherID());
        assertEquals(person.getSpouseID(), result.getSpouseID());
        assertTrue(result.isSuccess());
        assertNull(result.getMessage());
    }

    @Test
    public void processRequestFailUnassociatedUsername() {
        personService = new PersonService();

        PersonResult result = personService.processRequest(personOne.getPersonID(), authTokenTwo.getAuthtoken());
        assertNotNull(result);

        assertFalse(result.isSuccess());
        String expectedMessage = "Error: Invalid personID";

        assertEquals(expectedMessage, result.getMessage());
    }

    @Test
    public void processRequestFailInvalidAuthToken() {
        personService = new PersonService();

        PersonResult result = personService.processRequest(personOne.getPersonID(), "bad");
        assertNotNull(result);

        assertFalse(result.isSuccess());
        String expectedMessage = "Error: Invalid authtoken";

        assertEquals(expectedMessage, result.getMessage());
    }
}
