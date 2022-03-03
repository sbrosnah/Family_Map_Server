package service;

import dao.DataAccessException;
import dao.Database;
import dao.UserDAO;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import result.LoginResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public class LoginServiceTest {
    private User user;
    private User userTwo;
    private LoginRequest request;
    private LoginResult result;
    private LoginRequest requestTwo;
    private LoginResult resultTwo;
    private Database db = new Database();
    private UserDAO userDAO;
    private LoginService loginService;


    @BeforeEach
    public void setup() throws DataAccessException {
        user = new User("jb", "hello", "jb@music.com", "Justin", "Beiber", 'm', "98909d8d6fs");
        userTwo = new User("celeb", "hello", "jb@music.com", "Justin", "Beiber", 'm', "98909d8d6fs");

        loginService = new LoginService();

        request = new LoginRequest();
        requestTwo = new LoginRequest();

        userDAO = new UserDAO(db.getConnection());
        db.clearTables();
        userDAO.insert(user);
        userDAO.insert(userTwo);
        db.closeConnection(true);

        setRequestInfo(user, request);
        setRequestInfo(userTwo, requestTwo);


    }

    private void setRequestInfo(User user, LoginRequest request) {
        request.setUsername(user.getUsername());
        request.setPassword(user.getPassword());
    }

    @AfterEach
    public void teardown() throws DataAccessException {


        user = null;
        db = null;
        loginService = null;
        request = null;
        result = null;
        requestTwo = null;
        resultTwo = null;


    }

    @Test
    public void ProcessRequestPass() {
        result = loginService.processRequest(request);
        CheckPassedRequest(result, user);
    }

    @Test
    public void ProcessRequestPassMultiple() {
        result = loginService.processRequest(request);
        resultTwo = loginService.processRequest(requestTwo);

        CheckPassedRequest(result, user);
        CheckPassedRequest(resultTwo, userTwo);
    }

    private void CheckPassedRequest(LoginResult result, User user){
        assertEquals(result.getClass(), LoginResult.class);
        assertTrue(result.isSuccess());
        assertEquals(result.getUsername(), user.getUsername());
        assertNotNull(result.getPersonID());
        assertNull(result.getMessage());
        assertNotNull(result.getAuthToken());
    }

    @Test
    public void ProcessRequestFailInvalidNumParamsAllMissing() {
        request.setUsername(null);
        request.setPassword(null);
        result = loginService.processRequest(request);
        CheckFailedRequest(result);
    }

    @Test
    public void ProcessRequestFailInvalidNumParamsOneMissing() {
        request.setUsername(null);
        result = loginService.processRequest(request);
        CheckFailedRequest(result);
    }


    public void CheckFailedRequest(LoginResult result){
        assertEquals(result.getClass(), LoginResult.class);
        assertFalse(result.isSuccess());
        assertNull(result.getUsername());
        assertNull(result.getPersonID());
        assertNull(result.getUsername());
        assertNotNull(result.getMessage());
        assertNull(result.getAuthToken());
    }
}
