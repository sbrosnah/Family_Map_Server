package dao;

import model.User;

import java.sql.*;

public class UserDAO {
    private final Connection conn;

    /**
     * Construct a UserDAO object by passing in a database connection
     * @param conn
     */
    public UserDAO(Connection conn) {this.conn = conn;}

    /**
     * Insert a user into the database
     * @param user a User object
     * @throws DataAccessException
     */
    public void insert(User user) throws DataAccessException {

        String sql = "INSERT INTO user (username, pWord, email, firstname, lastname, " +
                     "gender, personID) VALUES(?,?,?,?,?,?,?)";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstname());
            stmt.setString(5, user.getLastname());
            stmt.setString(6, Character.toString(user.getGender()));
            stmt.setString(7, user.getPersonID());

            stmt.executeUpdate();
        } catch (SQLException e){
            throw new DataAccessException("Error encountered while inserting into the database");
        }

    }

    /**
     * Find a person in the database
     * @param username a string
     * @return
     * @throws DataAccessException
     */
    public User find(String username) throws DataAccessException {
        User user;
        ResultSet rs = null;
        String sql = "SELECT * FROM user WHERE username = ?;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if(rs.next()){
                user = new User(rs.getString("username"), rs.getString("pWord"),
                                rs.getString("email"), rs.getString("firstname"),
                                rs.getString("lastname"), rs.getString("gender").charAt(0),
                                rs.getString("personID"));
                return user;
            }
        }catch(SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding user");
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

    /**
     * Delete a person in the database
     * @param username string to identify which person
     * @throws DataAccessException
     */
    public void delete(String username) throws DataAccessException {}

    /**
     * Clear the User Table in the database
     * @throws DataAccessException
     */
    public void clear() throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM user";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }

}
