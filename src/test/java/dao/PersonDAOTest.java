package dao;

import dao.DataAccessException;
import dao.Database;
import dao.PersonDAO;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class PersonDAOTest {
    private Database db;
    private Person person;
    private Person personTwo;
    private PersonDAO personDAO;

    @BeforeEach
    public void setup() throws DataAccessException {
        db = new Database();
        person = new Person("3", "spencerbrosnahan", "Mike", "Lee",
                            'm', "45", "54", "6");
        personTwo = new Person("6", "spencerbrosnahan", "Lucia", "Lee",
                'f', "89", "90", "3");
        Connection conn = db.getConnection();
        db.clearTables();
        personDAO = new PersonDAO(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        personDAO.insert(person);
        Person compareTest = personDAO.find(person.getPersonID());
        assertNotNull(compareTest);
        assertEquals(person, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        personDAO.insert(person);
        assertThrows(DataAccessException.class, ()-> personDAO.insert(person));
    }

    @Test
    public void findPass() throws DataAccessException {
        personDAO.insert(person);
        Person compareTest = personDAO.find(person.getPersonID());
        assertNotNull(compareTest);
        assertEquals(person, compareTest);
    }

    @Test
    public void findFail() throws DataAccessException {
        personDAO.insert(person);
        assertNull(personDAO.find("8986"));
    }

    @Test
    public void clearPass() throws DataAccessException {
        personDAO.insert(person);
        personDAO.insert(personTwo);
        personDAO.clear();
        Person compareTestOne = personDAO.find(person.getPersonID());
        Person compareTestTwo = personDAO.find(personTwo.getPersonID());
        assertEquals(compareTestOne, compareTestTwo);
    }
}
