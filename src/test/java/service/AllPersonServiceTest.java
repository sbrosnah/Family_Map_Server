package service;

import dao.*;
import model.AuthToken;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import result.AllPersonResult;
import result.PersonResult;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class AllPersonServiceTest {
    Database db;

    UserDAO userDAO;
    PersonDAO personDAO;
    AuthTokenDAO authTokenDAO;

    AllPersonService allPersonService;

    Person personOne;
    Person personTwo;
    Person personThree;
    Person personFour;

    User userOne;
    User userTwo;

    AuthToken authTokenOne;
    AuthToken authTokenTwo;
    AuthToken authTokenThree;

    //I need to load 2 users
    //I need to load 1 person related to one user and at least 2 related to the other
    //I need to check that I get two back and that both are related to the user

    @BeforeEach
    public void setup() throws DataAccessException {
        db = new Database();
        db.getConnection();
        db.clearTables();

        //I need to generate dummy data to insert into the database
        personOne = new Person("123", "sbrosnah", "Spencer", "Brosnahan",
                'm', "899889", "kdkfjd", "kdkdkdkdkdkkd");
        personTwo = new Person("456", "ebarlow", "Jenny", "Baker", 'f',
                "", "", "");

        personThree = new Person("789", "sbrosnah", "Sper", "Brohan",
                'm', "899889", "kdkfjd", "kdkdkdkdkdkkd");
        personFour = new Person("101112", "sbrosnah", "Jny", "Ber", 'f',
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
        personDAO.insert(personThree);
        personDAO.insert(personFour);

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
        allPersonService = new AllPersonService();

        AllPersonResult result = allPersonService.processRequest(authTokenOne.getAuthtoken());
        assertNotNull(result);
        assertEquals(3, result.getData().size());
        assertEquals(personOne, result.getData().get(0));
        assertEquals(personThree, result.getData().get(1));
        assertEquals(personFour, result.getData().get(2));
    }

    @Test
    public void processRequestFail() {
        allPersonService = new AllPersonService();

        AllPersonResult result = allPersonService.processRequest("accent");
        assertFalse(result.isSuccess());
        String expectedMessage = "Error: Invalid authtoken";
        assertEquals(expectedMessage, result.getMessage());
    }
}
