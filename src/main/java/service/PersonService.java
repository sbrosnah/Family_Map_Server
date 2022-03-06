package service;

import dao.*;
import result.PersonResult;
import model.Person;
import model.AuthToken;


import java.sql.Connection;
import java.sql.SQLException;

import java.util.logging.Logger;
import dao.DataAccessException;
import model.User;



public class PersonService {
    private static Logger logger = Logger.getLogger("PersonService");

    PersonResult result;
    Database db;
    PersonDAO personDAO;
    AuthTokenDAO authTokenDAO;
    User user;
    AuthToken authTokenObject;
    UserDAO userDAO;
    Person person;

    /**
     * Returns the single Person object with the specified ID (if the person is associated with the current user).
     * The current user is determined by the provided authtoken.
     * @return the personResult
     */
    public PersonResult processRequest(String personID, String authToken) {
        logger.info("PROCESSING REQUEST");

        //initializing database
        db = new Database();

        //loading an empty result
        result = new PersonResult();

        try {
            logger.info("opening connection, connecting dao, and loading data from request");
            personDAO = new PersonDAO(db.getConnection());



            logger.info("Checking info from request");
            if(checkAuthToken(authToken)){
                if(checkPersonID(personID)){
                    return getResultInfo();
                } else {
                   throw new Exception("Invalid personID");
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

    private boolean checkPersonID(String personID) throws DataAccessException {
        boolean isValid;

        personDAO = new PersonDAO(db.getConnection());
        person = personDAO.find(personID);
        db.closeConnection(false);

        if(person != null){
            if(user.getUsername().equals(person.getAssociatedUsername())){
                isValid = true;
            } else {
                isValid = false;
            }
        } else {
            isValid = false;
        }
        return isValid;
    }

    private PersonResult getResultInfo() {
        PersonResult result = new PersonResult();

        result.setAssociatedUsername(person.getAssociatedUsername());
        result.setPersonID(person.getPersonID());
        result.setFirstName(person.getFirstName());
        result.setLastName(person.getLastName());
        result.setGender(String.valueOf(person.getGender()));

        String fatherID = person.getFatherID();
        if(fatherID.equals("")) {
            result.setFatherID(null);
        } else {
            result.setFatherID(fatherID);
        }

        String motherID = person.getMotherID();
        if (motherID.equals("")) {
            result.setMotherID(null);
        } else {
            result.setMotherID(motherID);
        }

        String spouseID = person.getSpouseID();
        if (spouseID.equals("")) {
            result.setSpouseID(null);
        } else {
            result.setSpouseID(spouseID);
        }

        result.setSuccess(true);

        return result;
    }

}
