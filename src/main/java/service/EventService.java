package service;

import dao.*;
import model.Event;
import model.AuthToken;
import model.Person;
import model.User;
import result.EventResult;
import result.PersonResult;
import result.Result;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;


public class EventService {
    static private Logger logger = Logger.getLogger("EventService");

    EventResult result;
    Database db;
    EventDAO eventDAO;
    AuthTokenDAO authTokenDAO;
    User user;
    AuthToken authTokenObject;
    UserDAO userDAO;
    Event event;
    /**
     * Returns the single Event object with the specified ID (if the event is associated with the current user).
     * The current user is determined by the provided authtoken.
     * @param authToken
     * @param eventID
     * @return
     */
    public EventResult processRequest(String authToken, String eventID) {
        logger.info("PROCESSING REQUEST");

        //initializing database
        db = new Database();

        //loading an empty result
        result = new EventResult();

        try {
            logger.info("opening connection, connecting dao, and loading data from request");
            eventDAO = new EventDAO(db.getConnection());



            logger.info("Checking info from request");
            if(checkAuthToken(authToken)){
                if(checkEventID(eventID)){
                    return getResultInfo();
                } else {
                    throw new Exception("Invalid eventID");
                }
            } else {
                throw new Exception("Invalid authtoken");
            }
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

    private EventResult getResultInfo() {
        EventResult eventResult = new EventResult();

        eventResult.setEventID(event.getEventID());
        eventResult.setAssociatedUsername(event.getUsername());
        eventResult.setPersonID(event.getPersonID());
        eventResult.setLatitude(event.getLatitude());
        eventResult.setLongitude(event.getLongitude());
        eventResult.setCountry(event.getCountry());
        eventResult.setCity(event.getCity());
        eventResult.setEventType(event.getEventType());
        eventResult.setYear(event.getYear());
        eventResult.setSuccess(true);

        return eventResult;
    }

    private boolean checkAuthToken(String authToken) throws DataAccessException {
        boolean exists;

        authTokenDAO = new AuthTokenDAO(db.getConnection());
        authTokenObject = authTokenDAO.find(authToken);
        db.closeConnection(false);

        if(authTokenObject == null) {
            exists = false;
        } else {
            userDAO = new UserDAO(db.getConnection());
            user = userDAO.find(authTokenObject.getUsername());
            db.closeConnection(false);
            exists = true;
        }

        return exists;
    }

    private boolean checkEventID(String eventID) throws DataAccessException {
        boolean isValid;

        eventDAO = new EventDAO(db.getConnection());
        event = eventDAO.find(eventID);
        db.closeConnection(false);

        if(event != null){
            if(user.getUsername().equals(event.getUsername())){
                isValid = true;
            } else {
                isValid = false;
            }
        } else {
            isValid = false;
        }
        return isValid;
    }

}
