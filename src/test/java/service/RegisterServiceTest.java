package service;

import dao.DataAccessException;
import dao.Database;

import model.User;

import request.RegisterRequest;
import result.RegisterResult;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

    /*
    Possible errors to test for and things to check:
    - Check that the result is a generic result that can be casted to RegisterResult
    - Check that I can pass in a request of generic request (Not register request)
    - Test the wrong types of data passed in
    - Test missing values passed in (Ensure that the correct error code is displayed
    - Test passing in an already registered user

    Tests:
    - ProcessRequestPass()
    - ProcessRequestFailInvalidNumParams()
    - ProcessRequestFailInvalidParamTypes()
    - ProcessRequestFailAlreadyExists()
     */

public class RegisterServiceTest {
    private User user;
    private User userTwo;
    private RegisterRequest request;
    private RegisterResult result;
    private RegisterRequest requestTwo;
    private RegisterResult resultTwo;
    private Database db = new Database();
    private RegisterService service;


    @BeforeEach
    public void setup() throws DataAccessException{
        user = new User("jb", "hello", "jb@music.com", "Justin", "Beiber", 'm', "98909d8d6fs");
        userTwo = new User("celeb", "hello", "jb@music.com", "Justin", "Beiber", 'm', "98909d8d6fs");

        service = new RegisterService();

        request = new RegisterRequest();
        requestTwo = new RegisterRequest();

        setRequestInfo(user, request);
        setRequestInfo(userTwo, requestTwo);

        db.openConnection();
        db.clearTables();
        db.closeConnection(true);

    }

    private void setRequestInfo(User user, RegisterRequest request) {
        request.setUsername(user.getUsername());
        request.setPassword(user.getPassword());
        request.setEmail(user.getEmail());
        request.setFirstname(user.getFirstName());
        request.setLastname(user.getLastName());
        request.setGender(String.valueOf(user.getGender()));
    }

    @AfterEach
    public void teardown() throws DataAccessException, SQLException {

        if(!db.getConnection().isClosed()){
            db.closeConnection(false);
        }

        user = null;
        db = null;
        service = null;
        request = null;
        result = null;
        requestTwo = null;
        resultTwo = null;


    }

    @Test
    public void ProcessRequestPass() {
        result = service.processRequest(request);
        assertEquals(result.getClass(), RegisterResult.class);
        assertTrue(result.isSuccess());
        assertEquals(result.getUsername(), user.getUsername());
        assertNotNull(result.getPersonID());
        assertNull(result.getMessage());
        assertNotNull(result.getAuthtoken());
    }

    @Test
    public void ProcessRequestPassMultiple() {
        result = service.processRequest(request);
        resultTwo = service.processRequest(requestTwo);

        CheckPassedRequest(result, user);
        CheckPassedRequest(resultTwo, userTwo);
    }

    private void CheckPassedRequest(RegisterResult result, User user){
        assertEquals(result.getClass(), RegisterResult.class);
        assertTrue(result.isSuccess());
        assertEquals(result.getUsername(), user.getUsername());
        assertNotNull(result.getPersonID());
        assertNull(result.getMessage());
        assertNotNull(result.getAuthtoken());
    }

    @Test
    public void ProcessRequestFailInvalidNumParamsAllMissing() {
        request.setEmail(null);
        request.setUsername(null);
        request.setPassword(null);
        request.setFirstname(null);
        request.setLastname(null);
        request.setGender(null);
        result = service.processRequest(request);
        CheckFailedRequest(result);
    }

    @Test
    public void ProcessRequestFailInvalidNumParamsSomeMissing() {
        request.setEmail(null);
        request.setUsername(null);
        result = service.processRequest(request);
        CheckFailedRequest(result);
    }

    @Test
    public void ProcessRequestFailInvalidNumParamsOneMissing() {
        request.setUsername(null);
        request.setPassword(null);
        request.setFirstname(null);
        request.setLastname(null);
        request.setGender(null);
        result = service.processRequest(request);
        CheckFailedRequest(result);
    }

    @Test
    public void ProcessRequestFailAlreadyExists() {
        result = service.processRequest(request);
        resultTwo = service.processRequest(requestTwo);
        result = service.processRequest(request);
        resultTwo = service.processRequest(requestTwo);

        CheckFailedRequest(result);
        CheckFailedRequest(resultTwo);
    }

    public void CheckFailedRequest(RegisterResult result){
        assertEquals(result.getClass(), RegisterResult.class);
        assertFalse(result.isSuccess());
        assertNull(result.getUsername());
        assertNull(result.getPersonID());
        assertNull(result.getUsername());
        assertNotNull(result.getMessage());
        assertNull(result.getAuthtoken());
    }
}
