package dao;

import dao.DataAccessException;
import dao.Database;
import dao.EventDAO;
import model.Event;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.sql.Array;
import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class EventDAOTest {
    Database db;
    Event event;
    Event eventTwo;
    Event eventThree;
    EventDAO eventDAO;

    @BeforeEach
    public void setup() throws DataAccessException {
        db  = new Database();
        event = new Event("eirhwleikrj", "spencerbrosnahan", "9ihrowiehrj3e", 9, 10, "America",
                "Heber", "birth", 1989);
        eventTwo = new Event("eirhwidifj99dj", "spencerbrosnahan", "9ih", 99, 10, "America",
                "Heber", "birth", 1989);
        eventThree = new Event("eirh88rj", "johnathanbrosnahan", "9ihrowiehrj3e", 9, 10, "America",
                "Heber", "birth", 1989);
        Connection conn = db.getConnection();
        eventDAO = new EventDAO(conn);
        eventDAO.clear();
    }

    @AfterEach
    public void teardown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        eventDAO.insert(event);
        Event compareTest = eventDAO.find(event.getEventID());
        assertNotNull(compareTest);
        assertEquals(event, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        eventDAO.insert(event);
        assertThrows(DataAccessException.class, () -> eventDAO.insert(event));
    }

    @Test
    public void findPass() throws DataAccessException {
        eventDAO.insert(event);
        Event compareTest = eventDAO.find(event.getEventID());
        assertNotNull(compareTest);
        assertEquals(compareTest, event);
    }

    @Test
    public void findFail() throws DataAccessException {
        eventDAO.insert(event);
        assertNull(eventDAO.find("kdkdkdkdkdkd"));
    }

    @Test
    public void findEventOfPersonPass() throws DataAccessException {
        eventDAO.insert(event);
        Event compareTest = eventDAO.findEventOfPerson(event.getPersonID(), event.getEventType());
        assertNotNull(compareTest);
        assertEquals(compareTest, event);
    }

    @Test
    public void findEventOfPersonFail() throws DataAccessException {
        eventDAO.insert(event);
        assertNull(eventDAO.findEventOfPerson(event.getPersonID(), "marriage"));
    }

    @Test
    public void getAllForPersonPass() throws DataAccessException {
        eventDAO.insert(event);
        eventDAO.insert(eventTwo);
        eventDAO.insert(eventThree);
        ArrayList<Event> allForPerson = eventDAO.getAllForPerson("9ihrowiehrj3e");
        assertEquals(2, allForPerson.size());
    }

    @Test
    public void clearPass() throws DataAccessException {
        eventDAO.insert(event);
        eventDAO.insert(eventTwo);
        eventDAO.clear();
        Event compareTestOne = eventDAO.find(event.getEventID());
        Event compareTestTwo = eventDAO.find(eventTwo.getEventID());
        assertNull(compareTestTwo);
        assertNull(compareTestOne);
    }

    @Test
    public void getAllPass() throws DataAccessException {
        eventDAO.insert(event);
        eventDAO.insert(eventTwo);
        ArrayList<Event> allEvents = eventDAO.getAll();
        Event compareTestOne = eventDAO.find(allEvents.get(0).getEventID());
        Event compareTestTwo = eventDAO.find(allEvents.get(1).getEventID());
        assertEquals(compareTestOne, allEvents.get(0));
        assertEquals(compareTestTwo, allEvents.get(1));
        assertEquals(allEvents.size(), 2);
    }

    @Test
    public void getAllAssociatedPass() throws DataAccessException {
        eventDAO.insert(event);
        eventDAO.insert(eventTwo);
        eventDAO.insert(eventThree);
        String associatedUsername = event.getUsername();
        ArrayList<Event> selectedEvents = eventDAO.getAllAssociated(associatedUsername);
        Event compareTestOne = selectedEvents.get(0);
        Event compareTestTwo = selectedEvents.get(1);
        assertEquals(event, compareTestOne);
        assertEquals(eventTwo, compareTestTwo);
        assertEquals(selectedEvents.size(), 2);
    }

    @Test
    public void getAllAssociatedFail() throws DataAccessException {
        eventDAO.insert(event);
        eventDAO.insert(eventTwo);
        eventDAO.insert(eventThree);
        String associatedUsername = "blah";
        ArrayList<Event> selectedEvents = eventDAO.getAllAssociated(associatedUsername);
        assertEquals(selectedEvents.size(), 0);
    }

    @Test
    public void deletePass() throws DataAccessException {
        eventDAO.insert(event);
        eventDAO.insert(eventTwo);
        eventDAO.delete(event.getEventID());
        eventDAO.delete(eventTwo.getEventID());
        ArrayList<Event> allEvents = eventDAO.getAll();
        assertEquals(0, allEvents.size());
    }

    @Test
    public void deleteAllAssociatedPass() throws DataAccessException {
        eventDAO.insert(event);
        eventDAO.insert(eventTwo);
        eventDAO.insert(eventThree);
        eventDAO.deleteAllAssociated(event.getUsername());
        assertEquals(eventDAO.getAll().size(), 1);
    }

    @Test
    public void deleteAllAssociatedFail() throws DataAccessException {
        eventDAO.insert(event);
        eventDAO.insert(eventTwo);
        eventDAO.insert(eventThree);
        eventDAO.deleteAllAssociated("blah");
        assertEquals(eventDAO.getAll().size(), 3);
    }
}
