package model;

public class Person {
    private String personID;
    private String associatedUsername;
    private String firstName;
    private String lastName;
    private char gender;
    private String fatherID;
    private String motherID;
    private String spouseID;


    /**
     * Construct a Person object
     * @param personID
     * @param associatedUsername
     * @param firstName
     * @param lastName
     * @param gender
     * @param fatherID
     * @param motherID
     * @param spouseID
     */
    public Person(String personID, String associatedUsername, String firstName, String lastName, char gender, String fatherID,
                  String motherID, String spouseID){
        this.personID = personID;
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    /**
     * get the personID string
     * @return personID string
     */
    public String getPersonID() {
        return personID;
    }

    /**
     * Set the personID string
     * @param personID
     */
    public void setPersonID(String personID) {
        this.personID = personID;
    }

    /**
     * get the username of the current user to whom the person is a relative
     * @return associatedUsername String
     */
    public String getAssociatedUsername() {
        return associatedUsername;
    }

    /**
     * Set the username of the current user to whom the person is a relative
     * @param associatedUsername
     */
    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    /**
     * get the firstname of the person
     * @return firstname string
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Set the first name of the person
     * @param firstName String
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * get the last name of the person
     * @return last name string
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Set the last name of the person
     * @param lastName String
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Get the gender of the person
     * @return gender char
     */
    public char getGender() {
        return gender;
    }

    /**
     * Set the gender of the person
     * @param gender char
     */
    public void setGender(char gender) {
        this.gender = gender;
    }

    /**
     * Get the fatherID of the person
     * @return fatherID string
     */
    public String getFatherID() {
        return fatherID;
    }

    /**
     * Set the fatherID of the person
     * @param fatherID string
     */
    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    /**
     * Get the motherID of the person
     * @return motherID string
     */
    public String getMotherID() {
        return motherID;
    }

    /**
     * Set the MotherID of the person
     * @param motherID string
     */
    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    /**
     * get the spouseID of the person
     * @return spouseID string
     */
    public String getSpouseID() {
        return spouseID;
    }

    /**
     * Set the spouseID of the person
     * @param spouseID string
     */
    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }

    /**
     * @param o an object
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof Person) {
            Person oPerson = (Person) o;
            return oPerson.getPersonID().equals(getPersonID()) &&
                    oPerson.getAssociatedUsername().equals(getAssociatedUsername()) &&
                    oPerson.getFirstName().equals(getFirstName()) &&
                    oPerson.getLastName().equals(getLastName()) &&
                    oPerson.getGender() == (getGender()) &&
                    oPerson.getFatherID().equals(getFatherID()) &&
                    oPerson.getMotherID().equals(getMotherID()) &&
                    oPerson.getSpouseID().equals(getSpouseID());
        } else {
            return false;
        }
    }
}
