package service;

import dao.*;
import model.Event;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

import model.AuthToken;
import model.User;
import result.AllEventResult;


public class AllEventService {
    private static Logger logger = Logger.getLogger("AllEventService");

    AllEventResult result;
    Database db;
    EventDAO eventDAO;
    AuthTokenDAO authTokenDAO;
    User user;
    AuthToken authTokenObject;
    UserDAO userDAO;
    Event event;
    ArrayList<Event> allEvents;
    /**
     * Returns ALL events for ALL family members of the current user. The current user is determined from the provided auth token.
     * @param authToken
     * @return
     */
    public AllEventResult processRequest(String authToken) {
        logger.info("PROCESSING REQUEST");

        db = new Database();

        result = new AllEventResult();

        try {
            if(checkAuthToken(authToken)){
                eventDAO = new EventDAO(db.getConnection());
                allEvents = eventDAO.getAllAssociated(user.getUsername());

                result.setData(allEvents);
                result.setSuccess(true);
            } else {
                throw new Exception("Invalid authtoken");
            }
        } catch (Exception ex) {
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

}
