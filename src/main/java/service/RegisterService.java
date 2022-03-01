package service;

import dao.DataAccessException;
import request.RegisterRequest;
import result.RegisterResult;
import model.User;
import dao.UserDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Logger;
import request.Request;
import result.Result;

import dao.Database;

public class RegisterService implements Service {
    private static Logger logger = Logger.getLogger("RegisterService");
    /**
     * Takes a request, processes it, and then returns the result
     * @return a RegisterResult object
     */
    public Result processRequest(Request incomingRequest){
        logger.info("PROCESSING REQUEST");

        //initializing database connection
        Database db = new Database();

        //loading an empty result
        RegisterResult result = new RegisterResult();

        //cast the request to RegisterRequest
        RegisterRequest request;
        request = (RegisterRequest) incomingRequest;

        try {
            logger.info("opening connection and loading data from request");
            db.openConnection();
            String username = request.getUsername();
            String password = request.getPassword();
            String email = request.getEmail();
            String firstname = request.getFirstname();
            String lastname = request.getLastname();
            String gender = request.getGender();

            logger.info("Checking info from request");
            if (username != null || password != null || email != null || firstname != null || lastname != null || gender != null){
                logger.info("info was good");

                //TODO: check for invalid values

                String personID = UUID.randomUUID().toString();

                logger.info("initializing model and dao");
                User user = new User(username, password, email, firstname, lastname, gender.charAt(0), personID);
                UserDAO userDAO = new UserDAO(db.getConnection());

                if(userDAO.find(username) == null){
                    logger.info("inserting user to database");
                    userDAO.insert(user);
                    logger.info("user inserted");

                    logger.info("loading result info");
                    result.setUsername(user.getUsername());
                    result.setPersonID(user.getPersonID());
                    result.setSuccess(true);
                    logger.info("Result: " + result.toString());
                    db.closeConnection(true);
                } else {
                    throw new Exception("User already in database");
                }
            } else {
                throw new Exception("Bad data in request");
            }

        } catch(Exception ex){
            logger.info("exception thrown");
            result.setSuccess(false);
            result.setMessage("Error: " + ex.getMessage());
            logger.info(result.toString());
        }

        try {
            Connection conn = db.getConnection();
            if (conn.isClosed()) {
                db.closeConnection(false);
            }
        } catch(SQLException ex){
            logger.info(ex.getMessage());
        } catch(DataAccessException ex){
            logger.info(ex.getMessage());
        }

        return result;
    }

    /**
     * Checks to make sure that the username doesn't already exist in the database
     * @param username
     * @return
     */
    private boolean checkUsername(String username){return false;}

    /**
     * This will call the fill function to fill the ancestors of that person.
     * @param username
     */
    private void generateAncestors(String username) {}
}
