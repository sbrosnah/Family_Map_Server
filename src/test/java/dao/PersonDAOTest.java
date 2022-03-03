package dao;

import dao.DataAccessException;
import dao.Database;
import dao.PersonDAO;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.sql.Array;
import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PersonDAOTest {
    private Database db;
    private Person person;
    private Person personTwo;
    private Person personThree;
    private PersonDAO personDAO;

    @BeforeEach
    public void setup() throws DataAccessException {
        db = new Database();
        person = new Person("3", "spencerbrosnahan", "Mike", "Lee",
                            'm', "45", "54", "6");
        personTwo = new Person("6", "spencerbrosnahan", "Lucia", "Lee",
                'f', "89", "90", "3");
        personThree = new Person("9", "johnathanbrosnahan", "pillow", "Lee",
                'f', "89", "90", "3");
        Connection conn = db.getConnection();
        personDAO = new PersonDAO(conn);
        personDAO.clear();
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
    public void deletePass() throws DataAccessException {
        personDAO.insert(person);
        personDAO.insert(personTwo);
        personDAO.delete(person.getPersonID());
        personDAO.delete(personTwo.getPersonID());
        ArrayList<Person> allPeople = personDAO.getAll();
        assertEquals(0, allPeople.size());
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

    @Test
    public void getAllPass() throws DataAccessException {
        personDAO.insert(person);
        personDAO.insert(personTwo);
        ArrayList<Person> allPeople = personDAO.getAll();
        Person compareTestOne = personDAO.find(allPeople.get(0).getPersonID());
        Person compareTestTwo = personDAO.find(allPeople.get(1).getPersonID());
        assertEquals(compareTestOne, allPeople.get(0));
        assertEquals(compareTestTwo, allPeople.get(1));
        assertEquals(allPeople.size(), 2);
    }


    @Test
    public void getAllAssociatedPass() throws DataAccessException {
        personDAO.insert(person);
        personDAO.insert(personTwo);
        personDAO.insert(personThree);
        String associatedUsername = person.getAssociatedUsername();
        ArrayList<Person> selectedPeople = personDAO.getAllAssociated(associatedUsername);
        Person compareTestOne = selectedPeople.get(0);
        Person compareTestTwo = selectedPeople.get(1);
        assertEquals(person, compareTestOne);
        assertEquals(personTwo, compareTestTwo);
        assertEquals(selectedPeople.size(), 2);
    }

    @Test
    public void getAllAssociatedFail() throws DataAccessException {
        personDAO.insert(person);
        personDAO.insert(personTwo);
        personDAO.insert(personThree);
        String associatedUsername = "blah";
        ArrayList<Person> selectedPeople = personDAO.getAllAssociated(associatedUsername);
        assertEquals(selectedPeople.size(), 0);
    }
}


