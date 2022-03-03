package dao;

import model.Event;

import java.sql.*;
import java.util.ArrayList;

public class EventDAO extends DAO{
    /**
     * This is the connection to the database
     */
    //private final Connection conn;

    /**
     * Construct the EventDAO object
     * @param conn the database connection
     */
    public EventDAO(Connection conn) {
        this.conn = conn;
        tableType = "event";
        ID = "eventID";
    }

    /**
     * Insert an event into the database
     * @param event
     * @throws DataAccessException
     */
    public void insert(Event event) throws DataAccessException {
        String sql = "INSERT INTO event (eventID, associatedUsername, personID, latitude, longitude, " +
                "country, city, eventType, year) VALUES(?,?,?,?,?,?,?,?,?)";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, event.getEventID());
            stmt.setString(2, event.getUsername());
            stmt.setString(3, event.getPersonID());
            stmt.setString(4, Double.toString(event.getLatitude()));
            stmt.setString(5, Double.toString(event.getLongitude()));
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setString(9, Integer.toString(event.getYear()));

            stmt.executeUpdate();
        } catch (SQLException e){
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * Find an Event in the database
     * @param eventID
     * @return an Event object
     * @throws DataAccessException
     */
    public Event find(String eventID) throws DataAccessException {
        Event event;
        ResultSet rs = null;
        String sql = "SELECT * FROM event WHERE eventID = ?;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if(rs.next()){
                event = new Event(rs.getString("eventID"), rs.getString("associatedUsername"),
                        rs.getString("personID"), rs.getFloat("latitude"),
                        rs.getFloat("longitude"), rs.getString("country"),
                        rs.getString("city"), rs.getString("eventType"),
                        rs.getInt("year"));
                return event;
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
     * Get all of the Events in the database
     * @return an ArrayList of all the events
     * @throws DataAccessException
     */
    public ArrayList<Event> getAll() throws DataAccessException {
        Event event;
        ResultSet rs = null;
        String sql = "SELECT * FROM event;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            rs = stmt.executeQuery();
            ArrayList<Event> allEvents = new ArrayList<>();
            while(rs.next()){
                event = new Event(rs.getString("eventID"), rs.getString("associatedUsername"),
                        rs.getString("personID"), rs.getFloat("latitude"),
                        rs.getFloat("longitude"), rs.getString("country"),
                        rs.getString("city"), rs.getString("eventType"),
                        rs.getInt("year"));
                allEvents.add(event);
            }
            return allEvents;
        }catch(SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding all events");
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

    /**
     * Gets all of the events associated to a given username
     * @param username
     * @return
     * @throws DataAccessException
     */
    public ArrayList<Event> getAllAssociated(String username) throws DataAccessException {
        Event event;
        ResultSet rs = null;
        String sql = "SELECT * FROM event WHERE associatedUsername = ?;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            ArrayList<Event> selectedEvents = new ArrayList<>();
            while(rs.next()){
                event = new Event(rs.getString("eventID"), rs.getString("associatedUsername"),
                        rs.getString("personID"), rs.getFloat("latitude"),
                        rs.getFloat("longitude"), rs.getString("country"),
                        rs.getString("city"), rs.getString("eventType"),
                        rs.getInt("year"));
                selectedEvents.add(event);
            }
            return selectedEvents;
        }catch(SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding selected events");
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

    /**
     * Delete a specific event in the Database
     * @param eventID
     * @throws DataAccessException
     */
    /*
    public void delete(String eventID) throws DataAccessException {
        String sql = "DELETE FROM event WHERE eventID = ?;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, eventID);
            stmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while deleting event");
        }
    }
     */

    /**
     * Clear all of the events in the database
     * @throws DataAccessException
     */
    /*
    public void clear() throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM event";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing event table");
        }
    }
    */
}
