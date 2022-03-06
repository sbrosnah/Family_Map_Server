package dao;

import model.AuthToken;
import model.Person;
import model.User;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;

public class AuthTokenDAO extends DAO{
    //private final Connection conn;

    /**
     * Construct an AuthTokenDAO object by passing in a connection to the database
     * @param conn
     */
    public AuthTokenDAO(Connection conn) {
        this.conn = conn;
        tableType = "authtoken";
        ID = "authtoken";
    }

    /**
     * Insert an Authtoken object into the database
     * @param authToken
     * @throws DataAccessException
     */
    public void insert(AuthToken authToken) throws DataAccessException {
        String sql = "INSERT INTO authtoken (authtoken, associatedUsername) VALUES(?,?)";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, authToken.getAuthtoken());
            stmt.setString(2, authToken.getUsername());

            stmt.executeUpdate();
        } catch (SQLException e){
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * Find an associatedUsername for a given authToken in the database
     * @param authToken
     * @return an authtoken object
     * @throws DataAccessException
     */
    public AuthToken find(String authToken) throws DataAccessException {
        AuthToken authTokenObj;
        ResultSet rs = null;
        String sql = "SELECT * FROM authtoken WHERE authtoken = ?;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, authToken);
            rs = stmt.executeQuery();
            if(rs.next()){
                authTokenObj = new AuthToken(rs.getString("authtoken"), rs.getString("associatedUsername"));
                return authTokenObj;
            }
        }catch(SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding authtoken");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public ArrayList<AuthToken> getAll() throws DataAccessException {
        AuthToken authToken;
        ResultSet rs = null;
        String sql = "SELECT * FROM authtoken;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            rs = stmt.executeQuery();
            ArrayList<AuthToken> allAuthTokens = new ArrayList<>();
            while(rs.next()){
                authToken = createNewAuthTokenFromResultSet(rs);
                allAuthTokens.add(authToken);
            }
            return allAuthTokens;
        }catch(SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding person");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private AuthToken createNewAuthTokenFromResultSet(ResultSet rs) throws SQLException {
        return new AuthToken(rs.getString("authtoken"), rs.getString("associatedUsername"));
    }

    /**
     * Delete an authtoken object from the database
     * @param authToken
     * @throws DataAccessException
     */
    /*
    public void delete(String authToken) throws DataAccessException {
        String sql = "DELETE FROM authtoken WHERE authtoken = ?;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, authToken);
            stmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while deleting authtoken");
        }
    }
     */

    /**
     * Clear the authtoken table from the database
     * @throws DataAccessException
     */
    /*
    public void clear() throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM authtoken";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }
     */
}
