package service;

import dao.*;
import model.User;
import model.Person;
import model.Event;
import request.LoadRequest;
import result.Result;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class LoadService {
    private static Logger logger = Logger.getLogger("LoadService");

    Database db;
    Result result;
    ClearService clearService;

    UserDAO userDAO;
    PersonDAO personDAO;
    EventDAO eventDAO;


    /**
     * process the load request and return a result
     * @param request
     * @return Result object
     */
    public Result processRequest(LoadRequest request) {
        logger.info("PROCESSING REQUEST");

        //initializing database
        db = new Database();

        //loading an empty result
        result = new Result();

        try {
            if(db.getConnection().isClosed()){
                db.getConnection();
            }
            db.clearTables();
            db.closeConnection(true);
            if(!containsBadUserData(request.getUsers()) && !containsBadPersonData(request.getPersons()) &&
                    !containsBadEventData(request.getEvents())){

                LoadUsers(request.getUsers());
                LoadPeople(request.getPersons());
                LoadEvents(request.getEvents());

                result.setSuccess(true);
                result.setMessage("Successfully added " + request.getUsers().size() +  " users, " +
                        request.getPersons().size() + " persons, and " +
                        request.getEvents().size() + " events to the database.");

            } else {
                throw new Exception("Bad data in request");
            }
            return result;
        } catch (Exception ex){
            ex.printStackTrace();
            logger.info("exception thrown");
            result.setSuccess(false);
            result.setMessage("Error: " + ex.getMessage());
            logger.info(result.toString());
        } finally {
            try {
                Connection conn = db.getConnection();
                if (!conn.isClosed()) {
                    db.closeConnection(false);
                }
            } catch(SQLException ex){
                logger.info(ex.getMessage());
            } catch(DataAccessException ex){
                logger.info(ex.getMessage());
            }
        }
        return result;
    }

    /**
     * Load the users into a table
     * @param users
     */
    private void LoadUsers(ArrayList<User> users) throws DataAccessException {
        logger.info("inserting into users");
        userDAO = new UserDAO(db.getConnection());
        for(int i = 0; i < users.size(); i++){
            userDAO.insert(users.get(i));
        }
        db.closeConnection(true);
    }

    /**
     * Load the people into a table
     * @param people
     */
    private void LoadPeople(ArrayList<Person> people) throws DataAccessException {
        logger.info("inserting into person: " + people.size() );
        personDAO = new PersonDAO(db.getConnection());
        for(int i = 0; i < people.size(); i++){
            logger.info(people.get(i).toString());
            personDAO.insert(people.get(i));
        }
        db.closeConnection(true);
    }

    /**
     * Load the events into a table
     * @param events
     */
    private void LoadEvents(ArrayList<Event> events) throws DataAccessException {
        logger.info("inserting into events");
        eventDAO = new EventDAO(db.getConnection());
        for(Event event : events){
            eventDAO.insert(event);
        }
        db.closeConnection(true);
    }

    private boolean containsBadUserData(ArrayList<User> users){
        if((users.size() > 0) && (users.get(0).getClass() != User.class) || (users == null)){
            return true;
        }
        return false;
    }

    private boolean containsBadPersonData(ArrayList<Person> persons){
        if((persons.size() > 0) && (persons.get(0).getClass() != Person.class) || (persons == null)){
            return true;
        }
        return false;
    }

    private boolean containsBadEventData(ArrayList<Event> events) {
        if((events.size() > 0) && (events.get(0).getClass() != Event.class) || (events == null)){
            return true;
        }
        return false;
    }
}
