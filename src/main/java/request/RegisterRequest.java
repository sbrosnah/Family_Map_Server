package request;

public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;

    /**
     * get the username
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the username
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * get the password
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the password
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstName;
    }

    public void setFirstname(String firstName) {
        this.firstName = firstName;
    }

    public String getLastname() {
        return lastName;
    }

    public void setLastname(String lastname) {
        this.lastName = lastname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Username: " + username + ", Password: " + password + ", Email: " + email + ", Firstname: " + firstName + ", Lastname: " +
                lastName + ", Gender: " + gender;
    }
}
