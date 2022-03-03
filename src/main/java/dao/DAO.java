package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DAO {
    protected String tableType;
    protected String ID;
    /**
     * This is the connection to the database
     */
    protected Connection conn;

    /**
     * Delete a person in the database
     * @param identifier string to identify which line
     * @throws DataAccessException
     */
    public void delete(String identifier) throws DataAccessException {
        String sql = "DELETE FROM " + tableType + " WHERE " + ID + " = ?;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, identifier);
            stmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while deleting " + tableType);
        }
    }

    /**
     * Clear the User Table in the database
     * @throws DataAccessException
     */
    public void clear() throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM " + tableType;
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing " + tableType + " table");
        }
    }
}
