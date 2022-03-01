package model;

public class Event {
    private String eventID;
    private String associatedUsername;
    private String personID;
    private float latitude;
    private float longitude;
    private String country;
    private String city;
    private String eventType;
    private int year;


    /**
     * Constructor
     * @param eventID
     * @param username
     * @param personID
     * @param latitude
     * @param longitude
     * @param country
     * @param city
     * @param eventType
     * @param year
     */
    public Event(String eventID, String username, String personID, float latitude, float longitude,
                 String country, String city, String eventType, int year) {
        this.eventID = eventID;
        this.associatedUsername = username;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }


    /**
     * Get the Event ID
     *
     * @return an eventID string
     */
    public String getEventID() {
        return eventID;
    }


    /**
     * Set the Event ID
     * @param eventID an eventId string
     */
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }


    /**
     * Get the user's name
     * @return a username string
     */
    public String getUsername() {
        return associatedUsername;
    }


    /**
     * Set the user's name
     * @param username the username
     */
    public void setUsername(String username) {
        this.associatedUsername = username;
    }


    /**
     * Get the Person's ID
     * @return a personID string
     */
    public String getPersonID() {
        return personID;
    }


    /**
     * Set the Person's ID
     * @param personID the person identifier string
     */
    public void setPersonID(String personID) {
        this.personID = personID;
    }


    /**
     * Get the Latitude
     * @return a latitude double
     */
    public double getLatitude() {
        return latitude;
    }


    /**
     * Set the Latitude
     * @param latitude
     */
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }


    /**
     * Get the Longitude
     * @return a longitude float
     */
    public double getLongitude() {
        return longitude;
    }


    /**
     * Set the Longitude
     * @param longitude a float
     */
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }


    /**
     * Get the Country
     * @return country string
     */
    public String getCountry() {
        return country;
    }


    /**
     * Set the Country
     * @param country string
     */
    public void setCountry(String country) {
        this.country = country;
    }


    /**
     * Get the City
     * @return country string
     */
    public String getCity() {
        return city;
    }


    /**
     * Set the City
     */
    public void setCity(String city) {
        this.city = city;
    }


    /**
     * Get Event Type
     * @return eventType string
     */
    public String getEventType() {
        return eventType;
    }


    /**
     * Set the Event Type
     * @param eventType string
     */
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }


    /**
     * Get the Year
     * @return year int
     */
    public int getYear() {
        return year;
    }


    /**
     * Set the Year
     * @param year integer
     */
    public void setYear(int year) {
        this.year = year;
    }


    /**
     * Check euquality by value of event objects
     * @param o an object
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof Event) {
            Event oEvent = (Event) o;
            return oEvent.getEventID().equals(getEventID()) &&
                    oEvent.getUsername().equals(getUsername()) &&
                    oEvent.getPersonID().equals(getPersonID()) &&
                    oEvent.getLatitude() == (getLatitude()) &&
                    oEvent.getLongitude() == (getLongitude()) &&
                    oEvent.getCountry().equals(getCountry()) &&
                    oEvent.getCity().equals(getCity()) &&
                    oEvent.getEventType().equals(getEventType()) &&
                    oEvent.getYear() == (getYear());
        } else {
            return false;
        }
    }
}
