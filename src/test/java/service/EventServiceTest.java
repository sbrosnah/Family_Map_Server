package service;

import dao.*;
import model.AuthToken;
import model.Event;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import result.EventResult;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventServiceTest {
    Database db;

    UserDAO userDAO;
    EventDAO eventDAO;
    AuthTokenDAO authTokenDAO;

    EventService eventService;

    Event eventOne;
    Event eventTwo;

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
        eventOne = new Event("123", "sbrosnah", "4545", (float) 10.5, 343,"Asia",
                "Hong Kong", "marriage", 1223);
        eventTwo = new Event("456", "ebarlow", "878d", (float) 12.3, 809, "America",
                "Heber", "death", 9098);

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
        eventService = new EventService();

        EventResult result = eventService.processRequest(authTokenOne.getAuthtoken(), eventOne.getEventID());
        assertNotNull(result);

        checkPassingResult(result, eventOne);

    }

    private void checkPassingResult(EventResult result, Event event) {
        assertEquals(event.getEventID(), result.getEventID());
        assertEquals(event.getPersonID(), result.getPersonID());
        assertEquals(event.getUsername(), result.getAssociatedUsername());
        assertEquals(event.getLatitude(), result.getLatitude());
        assertEquals(event.getLongitude(), result.getLongitude());
        assertEquals(event.getEventType(), result.getEventType());
        assertEquals(event.getYear(), result.getYear());
        assertEquals(event.getCity(), result.getCity());
        assertEquals(event.getCountry(), result.getCountry());
        assertTrue(result.isSuccess());
        assertNull(result.getMessage());
    }

    @Test
    public void processRequestFailUnassociatedUsername() {
        eventService = new EventService();

        EventResult result = eventService.processRequest(authTokenTwo.getAuthtoken(), eventOne.getEventID());
        assertNotNull(result);

        assertFalse(result.isSuccess());
        String expectedMessage = "Error: Invalid eventID";

        assertEquals(expectedMessage, result.getMessage());
    }

    @Test
    public void processRequestFailInvalidAuthToken() {
        eventService = new EventService();

        EventResult result = eventService.processRequest(eventOne.getPersonID(), "bad");
        assertNotNull(result);

        assertFalse(result.isSuccess());
        String expectedMessage = "Error: Invalid authtoken";

        assertEquals(expectedMessage, result.getMessage());
    }
}
