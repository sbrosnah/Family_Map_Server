package model;

public class User {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private char gender;
    private String personID;

    /**
     * Construct a User object
     * @param username
     * @param password
     * @param email
     * @param firstname
     * @param lastname
     * @param gender
     * @param personID
     */
    public User(String username, String password, String email, String firstname, String lastname, char gender, String personID){
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstname;
        this.lastName = lastname;
        this.gender = gender;
        this.personID = personID;
    }

    /**
     * Get the username of the user
     * @return username string
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the username of the user
     * @param username String
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the password of the user
     * @return password String
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the password of the user
     * @param password String
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get the email of the user
     * @return email string
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the email of the user
     * @param email String
     */
    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Get the gender of the user. ('m' or 'f')
     * @return gender char
     */
    public char getGender() {
        return gender;
    }

    /**
     * Set the gender of the user
     * @param gender char ('m' or 'f')
     */
    public void setGender(char gender) {
        this.gender = gender;
    }

    /**
     * get the personID of the user
     * @return personID string
     */
    public String getPersonID() {
        return personID;
    }

    /**
     * Set the personID of the user
     * @param personID String
     */
    public void setPersonID(String personID) {
        this.personID = personID;
    }

    /**
     * @param o an object
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof User) {
            User oUser = (User) o;
            return oUser.getUsername().equals(getUsername()) &&
                    oUser.getEmail().equals(getEmail()) &&
                    oUser.getPersonID().equals(getPersonID()) &&
                    oUser.getFirstName().equals(getFirstName()) &&
                    oUser.getLastName().equals(getLastName()) &&
                    oUser.getPassword().equals(getPassword()) &&
                    oUser.getGender() == getGender();

        } else {
            return false;
        }
    }
}
