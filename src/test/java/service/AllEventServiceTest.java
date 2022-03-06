package service;

import dao.*;
import model.AuthToken;
import model.Event;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import result.AllEventResult;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AllEventServiceTest {
    Database db;

    UserDAO userDAO;
    EventDAO eventDAO;
    AuthTokenDAO authTokenDAO;

    AllEventService allEventService;

    Event eventOne;
    Event eventTwo;
    Event eventThree;
    Event eventFour;

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
        eventOne = new Event("123", "sbrosnah", "4545", (float) 10.5, 343,"Asia",
                "Hong Kong", "marriage", 1223);
        eventTwo = new Event("456", "ebarlow", "878d", (float) 12.3, 809, "America",
                "Heber", "death", 9098);
        eventThree = new Event("789", "sbrosnah", "45", (float) 1.5, 33,"Aa",
                "Hong Kong", "marriage", 1223);
        eventFour = new Event("101112", "sbrosnah", "8d", (float) 1.3, 89, "Ameca",
                "Her", "deh", 998);

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

        eventDAO = new EventDAO(db.getConnection());
        eventDAO.insert(eventOne);
        eventDAO.insert(eventTwo);
        eventDAO.insert(eventThree);
        eventDAO.insert(eventFour);

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
        allEventService = new AllEventService();

        AllEventResult result = allEventService.processRequest(authTokenOne.getAuthtoken());
        assertNotNull(result);
        assertEquals(3, result.getData().size());
        assertEquals(eventOne, result.getData().get(0));
        assertEquals(eventThree, result.getData().get(1));
        assertEquals(eventFour, result.getData().get(2));
    }

    @Test
    public void processRequestFail() {
        allEventService = new AllEventService();

        AllEventResult result = allEventService.processRequest("accent");
        assertFalse(result.isSuccess());
        String expectedMessage = "Error: Invalid authtoken";
        assertEquals(expectedMessage, result.getMessage());
    }
}
