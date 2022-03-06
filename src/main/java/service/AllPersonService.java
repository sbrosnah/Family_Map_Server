package service;

import dao.*;
import result.AllPersonResult;
import model.AuthToken;
import model.Person;
import model.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.logging.Logger;

public class AllPersonService {
    private static Logger logger = Logger.getLogger("AllPersonService");

    AllPersonResult result;
    Database db;
    PersonDAO personDAO;
    AuthTokenDAO authTokenDAO;
    User user;
    AuthToken authTokenObject;
    UserDAO userDAO;
    Person person;
    ArrayList<Person> allPersons;

    /**
     * Returns ALL family members of the current user. The current user is determined by the provided authtoken.
     * @param authToken
     * @return
     */
    public AllPersonResult processRequest(String authToken) {
        logger.info("PROCESSING REQUEST");

        db = new Database();

        result = new AllPersonResult();

        try {
            if(checkAuthToken(authToken)){
                personDAO = new PersonDAO(db.getConnection());
                allPersons = personDAO.getAllAssociated(user.getUsername());

                result.setData(allPersons);
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

    /**
     * Set the user of the authToken
     * @param authToken
     * @return
     */
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
