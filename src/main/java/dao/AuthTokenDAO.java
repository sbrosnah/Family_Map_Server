package dao;

import model.AuthToken;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthTokenDAO {
    private final Connection conn;

    /**
     * Construct an AuthTokenDAO object by passing in a connection to the database
     * @param conn
     */
    public AuthTokenDAO(Connection conn) {this.conn = conn;}

    /**
     * Insert an Authtoken object into the database
     * @param authToken
     * @throws DataAccessException
     */
    public void insert(AuthToken authToken) throws DataAccessException {}

    /**
     * Find an associatedUsername for a given authToken in the database
     * @param authToken
     * @return a username string
     * @throws DataAccessException
     */
    public String find(String authToken) throws DataAccessException {return null; }

    /**
     * Delete an authtoken object from the database
     * @param authToken
     * @throws DataAccessException
     */
    public void delete(String authToken) throws DataAccessException {}

    /**
     * Clear the authtoken table from the database
     * @throws DataAccessException
     */
    public void clear() throws DataAccessException {}
}
