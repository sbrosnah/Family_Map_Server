package service;

import com.google.gson.Gson;
import dao.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoadRequest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import result.Result;
import model.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LoadServiceTest {
    Database db;
    EventDAO eventDAO;
    PersonDAO personDAO;
    UserDAO userDAO;
    AuthTokenDAO authTokenDAO;
    LoadRequest goodLoadRequest;
    LoadRequest badLoadRequest;
    LoadService loadService = new LoadService();


    @BeforeEach
    public void setup() throws DataAccessException, IOException {
        db = new Database();
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
        String goodFilePath = "src/test/sample.json";
        String badFilePath = "src/test/bad_sample.json";
        File goodFile = new File(goodFilePath);
        Gson gson = new Gson();

        try(FileReader fileReader = new FileReader(goodFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader)) {
           goodLoadRequest = gson.fromJson(bufferedReader, LoadRequest.class);

        }
        try(FileReader fileReader = new FileReader(badFilePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            badLoadRequest = gson.fromJson(bufferedReader, LoadRequest.class);
        }
    }

    @AfterEach
    public void teardown() throws DataAccessException, SQLException {
        if(!db.getConnection().isClosed()){
            db.closeConnection(false);
        }
    }

    @Test
    public void ProcessRequestPass() throws DataAccessException {
        Result goodResult = loadService.processRequest(goodLoadRequest);
        List<User> users = goodLoadRequest.getUsers();
        List<Person> persons = goodLoadRequest.getPersons();
        List<Event> events = goodLoadRequest.getEvents();
        int numExpectedUsers = users.size();
        int numExpecedPersons = persons.size();
        int numExpectedEvents = events.size();


        String successMessage = "Successfully added " + numExpectedUsers + " users, " + numExpecedPersons + " persons, and "
        + numExpectedEvents + " events to the database.";
        assertTrue(goodResult.isSuccess());
        assertEquals(successMessage, goodResult.getMessage());

        userDAO = new UserDAO(db.getConnection());
        List<User> usersFromDatabase = userDAO.getAll();
        db.closeConnection(false);
        assertTrue(checkUserEquality(users, usersFromDatabase));

        personDAO = new PersonDAO(db.getConnection());
        List<Person> personsFromDatabase = personDAO.getAll();
        db.closeConnection(false);
        assertTrue(checkPersonEquality(persons, personsFromDatabase));

        eventDAO = new EventDAO(db.getConnection());
        List<Event> eventsFromDatabase = eventDAO.getAll();
        db.closeConnection(false);
        assertTrue(checkEventEquality(events, eventsFromDatabase));
    }

    @Test
    public void ProcessRequestFail() throws DataAccessException {
        Result badResult = loadService.processRequest(badLoadRequest);
        List<User> users = badLoadRequest.getUsers();
        List<Person> persons = badLoadRequest.getPersons();
        List<Event> events = badLoadRequest.getEvents();
        int numExpectedUsers = users.size();
        int numExpecedPersons = persons.size();
        int numExpectedEvents = events.size();


        String successMessage = "Successfully added " + numExpectedUsers + " users, " + numExpecedPersons + " persons, and "
                + numExpectedEvents + " events to the database.";
        assertFalse(badResult.isSuccess());
        assertNotEquals(successMessage, badResult.getMessage());

        userDAO = new UserDAO(db.getConnection());
        List<User> usersFromDatabase = userDAO.getAll();
        db.closeConnection(false);
        assertFalse(checkUserEquality(users, usersFromDatabase));

        personDAO = new PersonDAO(db.getConnection());
        List<Person> personsFromDatabase = personDAO.getAll();
        db.closeConnection(false);
        assertFalse(checkPersonEquality(persons, personsFromDatabase));

        eventDAO = new EventDAO(db.getConnection());
        List<Event> eventsFromDatabase = eventDAO.getAll();
        db.closeConnection(false);
        assertFalse(checkEventEquality(events, eventsFromDatabase));
    }

    private boolean checkUserEquality(List<User> listOne, List<User> listTwo){
        boolean areEqual = true;
        if(listOne == null && listTwo == null){
            return true;
        }
        if(listOne == null && listTwo != null){
            return false;
        }
        if(listOne != null && listTwo == null){
            return false;
        }
        if(listOne.size() != listTwo.size()){
            return false;
        }

        //At this point we know that the lists are the same size
        for(int i = 0; i < listOne.size(); i++){
            if(!listOne.get(i).equals(listTwo.get(i))){
                return false;
            }
        }
        //If we've made it here, we can conclude that the lists are equal
        return areEqual;
    }

    private boolean checkPersonEquality(List<Person> listOne, List<Person> listTwo){
        boolean areEqual = true;
        if(listOne == null && listTwo == null){
            return true;
        }
        if(listOne == null && listTwo != null){
            return false;
        }
        if(listOne != null && listTwo == null){
            return false;
        }
        if(listOne.size() != listTwo.size()){
            return false;
        }

        //At this point we know that the lists are the same size
        for(int i = 0; i < listOne.size(); i++){
            if(!listOne.get(i).equals(listTwo.get(i))){
                return false;
            }
        }
        //If we've made it here, we can conclude that the lists are equal
        return areEqual;
    }

    private boolean checkEventEquality(List<Event> listOne, List<Event> listTwo){
        boolean areEqual = true;
        if(listOne == null && listTwo == null){
            return true;
        }
        if(listOne == null && listTwo != null){
            return false;
        }
        if(listOne != null && listTwo == null){
            return false;
        }
        if(listOne.size() != listTwo.size()){
            return false;
        }

        //At this point we know that the lists are the same size
        for(int i = 0; i < listOne.size(); i++){
            if(!listOne.get(i).equals(listTwo.get(i))){
                return false;
            }
        }
        //If we've made it here, we can conclude that the lists are equal
        return areEqual;
    }

}
