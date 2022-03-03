package dao;

import dao.DataAccessException;
import dao.Database;
import dao.AuthTokenDAO;
import model.AuthToken;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class AuthTokenDAOTest {
    private Database db;
    private AuthToken authToken;
    private AuthToken authTokenTwo;
    private AuthTokenDAO authTokenDAO;

    @BeforeEach
    public void setup() throws DataAccessException {
        db = new Database();
        authToken = new AuthToken("789837839839", "sb");
        authTokenTwo = new AuthToken("83928202949", "JB");
        Connection conn = db.getConnection();
        authTokenDAO = new AuthTokenDAO(conn);
        authTokenDAO.clear();
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        authTokenDAO.insert(authToken);
        AuthToken compareTest = authTokenDAO.find(authToken.getAuthtoken());
        assertNotNull(compareTest);
        assertEquals(authToken, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        authTokenDAO.insert(authToken);
        assertThrows(DataAccessException.class, ()-> authTokenDAO.insert(authToken));
    }

    @Test
    public void findPass() throws DataAccessException {
        authTokenDAO.insert(authToken);
        AuthToken compareTest = authTokenDAO.find(authToken.getAuthtoken());
        assertNotNull(compareTest);
        assertEquals(authToken, compareTest);
    }

    @Test
    public void findFail() throws DataAccessException {
        authTokenDAO.find(authToken.getAuthtoken());
        assertNull(authTokenDAO.find("elisabethbarlow"));
    }

    @Test
    public void clearPass() throws DataAccessException {
        authTokenDAO.insert(authToken);
        authTokenDAO.insert(authTokenTwo);
        authTokenDAO.clear();
        AuthToken compareTestOne = authTokenDAO.find(authToken.getAuthtoken());
        AuthToken compareTestTwo = authTokenDAO.find(authToken.getAuthtoken());
        assertEquals(compareTestOne, compareTestTwo);
    }

    @Test
    public void deletePass() throws DataAccessException {
        authTokenDAO.insert(authToken);
        authTokenDAO.insert(authTokenTwo);
        AuthToken compareTest = authTokenDAO.find(authToken.getAuthtoken());
        assertEquals(compareTest, authToken);
        authTokenDAO.delete(authToken.getAuthtoken());
        compareTest = authTokenDAO.find(authToken.getAuthtoken());
        assertNull(compareTest);
        compareTest = authTokenDAO.find(authTokenTwo.getAuthtoken());
        assertEquals(compareTest, authTokenTwo);
        authTokenDAO.delete(authTokenTwo.getAuthtoken());
        compareTest = authTokenDAO.find(authTokenTwo.getAuthtoken());
        assertNull(compareTest);
    }

    @Test
    public void deletFail() throws DataAccessException {
        authTokenDAO.insert(authToken);
        authTokenDAO.delete(authTokenTwo.getAuthtoken());
        AuthToken compareTest = authTokenDAO.find(authToken.getAuthtoken());
        assertEquals(compareTest, authToken);
        assertNotEquals(compareTest, authTokenTwo);
    }

}
