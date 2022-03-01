package dao;

import model.Event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EventDAO {
    /**
     * This is the connection to the database
     */
    private final Connection conn;

    /**
     * Construct the EventDAO object
     * @param conn the database connection
     */
    public EventDAO(Connection conn) {this.conn = conn;}

    /**
     * Insert an event into the database
     * @param event
     * @throws DataAccessException
     */
    public void insert(Event event) throws DataAccessException {}

    /**
     * Find an Event in the database
     * @param eventID
     * @return an Event object
     * @throws DataAccessException
     */
    public Event find(String eventID) throws DataAccessException { return null; }

    /**
     * Get all of the Events in the database
     * @return an ArrayList of all the events
     * @throws DataAccessException
     */
    public ArrayList<Event> getAll() throws DataAccessException { return null; }

    /**
     * Gets all of the events associated to a given username
     * @param username
     * @return
     * @throws DataAccessException
     */
    public ArrayList<Event> getAllAssociated(String username) throws DataAccessException {return null;}

    /**
     * Delete a specific event in the Database
     * @param eventID
     * @throws DataAccessException
     */
    public void delete(String eventID) throws DataAccessException {}

    /**
     * Clear all of the events in the database
     * @throws DataAccessException
     */
    public void clear() throws DataAccessException {}

}
