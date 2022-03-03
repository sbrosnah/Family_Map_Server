package service;

import dao.DataAccessException;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.RegisterResult;
import model.User;
import dao.UserDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Logger;

import dao.Database;

public class RegisterService {
    private static Logger logger = Logger.getLogger("RegisterService");

    UserDAO userDAO;
    Database db;
    RegisterResult result;
    /**
     * Takes a request, processes it, and then returns the result
     * @return a RegisterResult object
     */
    public RegisterResult processRequest(RegisterRequest request){
        logger.info("PROCESSING REQUEST");

        //initializing database
        db = new Database();

        //loading an empty result
        result = new RegisterResult();



        try {
            logger.info("opening connection, connecting dao, and loading data from request");
            db.openConnection();
            userDAO = new UserDAO(db.getConnection());

            String username = request.getUsername();
            String password = request.getPassword();
            String email = request.getEmail();
            String firstname = request.getFirstname();
            String lastname = request.getLastname();
            String gender = request.getGender();


            logger.info("Checking info from request");
            if (username != null && password != null && email != null && firstname != null && lastname != null && gender != null){
                logger.info("info was good");

                String personID = UUID.randomUUID().toString();

                logger.info("initializing model");
                User user = new User(username, password, email, firstname, lastname, gender.charAt(0), personID);

                logger.info("Checking if username is already in database");
                if(checkIfUsernameDoesntExists(user.getUsername())){
                    logger.info("Username not already in database. Inserting user to database");
                    userDAO.insert(user);
                    logger.info("user inserted");
                    db.closeConnection(true);

                    logger.info("logging user in");


                    LoginRequest loginRequest = new LoginRequest();
                    loginRequest.setUsername(user.getUsername());
                    loginRequest.setPassword(user.getPassword());
                    LoginService loginService = new LoginService();
                    LoginResult loginResult = loginService.processRequest(loginRequest);

                    logger.info("loading result info");
                    result.setAuthtoken(loginResult.getAuthToken());
                    result.setUsername(user.getUsername());
                    result.setPersonID(user.getPersonID());
                    result.setSuccess(true);
                    logger.info("Result: " + result.toString());
                } else {
                    throw new Exception("User already in database");
                }
            } else {
                throw new Exception("Bad data in request");
            }

        } catch(Exception ex){
            ex.printStackTrace();
            logger.info("exception thrown");
            result.setSuccess(false);
            result.setMessage("Error: " + ex.getMessage());
            logger.info(result.toString());
        }finally {
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
     * Checks to make sure that the username doesn't already exist in the database
     * @param username
     * @return
     */
    private boolean checkIfUsernameDoesntExists(String username) throws DataAccessException {
        User temp = null;
        temp = userDAO.find(username);
        if(temp != null) {
            return false;
        }
        return true;
    }

    /**
     * This will call the fill function to fill the ancestors of that person.
     * @param username
     */
    private void generateAncestors(String username) {}
}
