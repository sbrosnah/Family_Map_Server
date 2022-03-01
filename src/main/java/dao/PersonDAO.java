package dao;

import model.Person;
import model.User;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;

public class PersonDAO {
    private final Connection conn;

    /**
     * Construct the PersonDAO object by passing in a connection to the database
     * @param conn
     */
    public PersonDAO(Connection conn) {this.conn = conn;}

    /**
     * Insert a person into the database
     * @param person a person object
     * @throws DataAccessException
     */
    public void insert(Person person) throws DataAccessException {
        String sql = "INSERT INTO person (personID, associatedUsername, firstname, lastname, gender, " +
                "fatherID, motherID, spouseID) VALUES(?,?,?,?,?,?,?, ?)";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, person.getPersonID());
            stmt.setString(2, person.getAssociatedUsername());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, Character.toString(person.getGender()));
            stmt.setString(6, person.getFatherID());
            stmt.setString(7, person.getMotherID());
            stmt.setString(8, person.getSpouseID());

            stmt.executeUpdate();
        } catch (SQLException e){
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * Find a person in the database
     * @param personID a personID string
     * @return
     * @throws DataAccessException
     */
    public Person find(String personID) throws DataAccessException {
        Person person;
        ResultSet rs = null;
        String sql = "SELECT * FROM person WHERE personID = ?;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if(rs.next()){
                person = new Person(rs.getString("personID"), rs.getString("associatedUsername"),
                        rs.getString("firstname"), rs.getString("lastname"),
                        rs.getString("gender").charAt(0), rs.getString("fatherID"),
                        rs.getString("motherID"), rs.getString("spouseID"));
                return person;
            }
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
        return null;
    }

    /**
     * Get all the people in the database
     * @return an ArrayList of Person objects
     * @throws DataAccessException
     */
    public ArrayList<Person> getAll() throws DataAccessException { return null; }

    /**
     * Gets all of the people associated with the user.
     * @return
     * @throws DataAccessException
     */
    public ArrayList<Person> getAllAssociated() throws DataAccessException { return null; }

    /**
     * Delete a person from the database
     * @param personID a String
     * @throws DataAccessException
     */
    public void delete(String personID) throws DataAccessException {}

    /**
     * Clear the Person table in the database
     * @throws DataAccessException
     */
    public void clear() throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM person";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }

}
