package service;

import dao.AuthTokenDAO;
import dao.DataAccessException;
import model.AuthToken;
import request.LoginRequest;
import result.LoginResult;
import model.User;
import dao.UserDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Logger;

import dao.Database;
import result.RegisterResult;

public class LoginService {
    private static Logger logger = Logger.getLogger("LoginService");

    UserDAO userDAO;
    Database db;
    LoginResult result;
    User user;

    /**
     * Processes the request and validates the username and password
     * @param request
     * @return
     */
    public LoginResult processRequest(LoginRequest request) {
        logger.info("PROCESSING REQUEST");

        //initializing database
        db = new Database();

        //loading an empty result
        result = new LoginResult();

        AuthTokenDAO authTokenDAO;

        try {
            logger.info("opening connection, connecting dao, and loading data from request");
            userDAO = new UserDAO(db.getConnection());

            logger.info("Checking info from request");
            if(request.getUsername() != null && request.getPassword() != null){
                logger.info("info was good");

                logger.info("getting user from database");
                if(checkIfUsernameExists(request.getUsername())){
                    logger.info("Username found, generating authtoken and inserting to database");

                    db.closeConnection(true);

                    String authToken = UUID.randomUUID().toString();

                    AuthToken authTokenObj = new AuthToken(authToken, request.getUsername());

                    authTokenDAO = new AuthTokenDAO(db.getConnection());

                    authTokenDAO.insert(authTokenObj);
                    result.setAuthToken(authToken);
                    result.setUsername(request.getUsername());
                    result.setPersonID(user.getPersonID());
                    result.setSuccess(true);

                    db.closeConnection(true);
                } else {
                    throw new Exception("User already in database");
                }

            } else {
                throw new Exception("Bad data in request");
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
    private boolean checkIfUsernameExists(String username) throws DataAccessException {
        user = userDAO.find(username);
        if(user != null) {
            return true;
        }
        return false;
    }
}
