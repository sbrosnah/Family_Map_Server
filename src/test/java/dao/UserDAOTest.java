package dao;

import dao.DataAccessException;
import dao.Database;
import dao.UserDAO;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {
    private Database db;
    private User user;
    private User userTwo;
    private UserDAO userDAO;

    @BeforeEach
    public void setup() throws DataAccessException {
        db = new Database();
        user = new User("spencerbrosnahan", "supdude", "elisabethis@hotmail.com",
                         "Spencer", "Brosnahan", 'm', "1");
        userTwo = new User("timbrosnahan", "supbro", "danais@hotmail.com",
                "Tim", "Brosnahan", 'm', "2");
        Connection conn = db.getConnection();
        userDAO = new UserDAO(conn);
        userDAO.clear();
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        userDAO.insert(user);
        User compareTest = userDAO.find(user.getUsername());
        assertNotNull(compareTest);
        assertEquals(user, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        userDAO.insert(user);
        assertThrows(DataAccessException.class, ()-> userDAO.insert(user));
    }

    @Test
    public void findPass() throws DataAccessException {
        userDAO.insert(user);
        User compareTest = userDAO.find(user.getUsername());
        assertNotNull(compareTest);
        assertEquals(user, compareTest);
    }

    @Test
    public void findFail() throws DataAccessException {
        userDAO.insert(user);
        assertNull(userDAO.find("elisabethbarlow"));
    }

    @Test
    public void clearPass() throws DataAccessException {
        userDAO.insert(user);
        userDAO.insert(userTwo);
        userDAO.clear();
        User compareTestOne = userDAO.find(user.getUsername());
        User compareTestTwo = userDAO.find(userTwo.getUsername());
        assertEquals(compareTestOne, compareTestTwo);
    }

    @Test
    public void deletePass() throws DataAccessException {
        userDAO.insert(user);
        userDAO.insert(userTwo);
        User compareTest = userDAO.find(user.getUsername());
        assertEquals(compareTest, user);
        userDAO.delete(user.getUsername());
        compareTest = userDAO.find(user.getUsername());
        assertNull(compareTest);
        compareTest = userDAO.find(userTwo.getUsername());
        assertEquals(compareTest, userTwo);
        userDAO.delete(userTwo.getUsername());
        compareTest = userDAO.find(userTwo.getUsername());
        assertNull(compareTest);
    }

    @Test
    public void deletFail() throws DataAccessException {
        userDAO.insert(user);
        userDAO.delete(userTwo.getUsername());
        User compareTest = userDAO.find(user.getUsername());
        assertEquals(compareTest, user);
        assertNotEquals(compareTest, userTwo);
    }
}
