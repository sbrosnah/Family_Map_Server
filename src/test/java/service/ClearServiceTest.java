package service;

import dao.*;
import model.*;
import request.RegisterRequest;
import result.Result;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTest {
    RegisterService registerService;
    ClearService clearService;
    RegisterRequest registerRequest;
    Database db;
    EventDAO eventDAO;
    PersonDAO personDAO;
    UserDAO userDAO;
    AuthTokenDAO authTokenDAO;
    Result result;

    @BeforeEach
    public void setup() throws DataAccessException {
        db = new Database();
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);

        registerService = new RegisterService();

        registerRequest = new RegisterRequest();
        registerRequest.setUsername("sbrosnah");
        registerRequest.setLastname("haha");
        registerRequest.setPassword("poop");
        registerRequest.setEmail("me@gmail.com");
        registerRequest.setGender("m");
        registerService.processRequest(registerRequest);

        registerRequest = new RegisterRequest();
        registerRequest.setUsername("comp");
        registerRequest.setLastname("hahdkijfa");
        registerRequest.setPassword("pododpopdpop");
        registerRequest.setEmail("me@gdofokdmail.com");
        registerRequest.setGender("f");
        registerService.processRequest(registerRequest);

        registerRequest = new RegisterRequest();
        registerRequest.setUsername("yeet");
        registerRequest.setLastname("mafia");
        registerRequest.setPassword("p373839398p");
        registerRequest.setEmail("melt@gil.com");
        registerRequest.setGender("f");
        registerService.processRequest(registerRequest);

        clearService = new ClearService();
    }

    @Test
    public void clearPass() throws DataAccessException {
        result = clearService.processRequest();

        eventDAO = new EventDAO(db.getConnection());
        ArrayList<Event> events = eventDAO.getAll();
        assertTrue(events.size() == 0);
        db.closeConnection(true);

        userDAO = new UserDAO(db.getConnection());
        ArrayList<User> users = userDAO.getAll();
        assertTrue(users.size() == 0);
        db.closeConnection(true);

        personDAO = new PersonDAO(db.getConnection());
        ArrayList<Person> people = personDAO.getAll();
        assertTrue(people.size() == 0);
        db.closeConnection(true);

        authTokenDAO = new AuthTokenDAO(db.getConnection());
        ArrayList<AuthToken> authTokens = authTokenDAO.getAll();
        assertTrue(authTokens.size() == 0);

        assertTrue(result.isSuccess());
        String expectedMessage = "Clear succeeded.";
        assertTrue(expectedMessage.equals(result.getMessage()));
        db.closeConnection(true);
    }
}
