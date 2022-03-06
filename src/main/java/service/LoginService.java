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
            if(request.getUsername() != null && request.getPassword() != null){
                if(checkIfUsernameExists(request.getUsername())){
                    if(user.getPassword().equals(request.getPassword())) {
                        String authToken = UUID.randomUUID().toString();

                        AuthToken authTokenObj = new AuthToken(authToken, request.getUsername());

                        authTokenDAO = new AuthTokenDAO(db.getConnection());
                        authTokenDAO.insert(authTokenObj);
                        db.closeConnection(true);

                        result.setAuthToken(authToken);
                        result.setUsername(request.getUsername());
                        result.setPersonID(user.getPersonID());
                        result.setSuccess(true);

                    } else {
                        throw new Exception("Passwords do not match");
                    }
                } else {
                    throw new Exception("Username provided is not in database");
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
        userDAO = new UserDAO(db.getConnection());
        user = userDAO.find(username);
        db.closeConnection(false);
        if(user != null) {
            return true;
        }
        return false;
    }
}
