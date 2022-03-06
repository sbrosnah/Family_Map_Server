package dao;

import model.Person;
import model.User;

import java.sql.*;
import java.util.ArrayList;

public class UserDAO extends DAO{

    /**
     * This is the connection to the database
     */
    //private final Connection conn;

    public UserDAO(Connection conn) {
        this.conn = conn;
        tableType = "user";
        ID = "username";
    }

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
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
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
                user = createNewUserFromResultSet(rs);
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


    private User createNewUserFromResultSet(ResultSet rs) throws SQLException {
        return new User(rs.getString("username"), rs.getString("pWord"),
                rs.getString("email"), rs.getString("firstname"),
                rs.getString("lastname"), rs.getString("gender").charAt(0),
                rs.getString("personID"));
    }

    public ArrayList<User> getAll() throws DataAccessException {
        User user;
        ResultSet rs = null;
        String sql = "SELECT * FROM user;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            rs = stmt.executeQuery();
            ArrayList<User> allUsers = new ArrayList<>();
            while(rs.next()){
                user = createNewUserFromResultSet(rs);
                allUsers.add(user);
            }
            return allUsers;
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

}
