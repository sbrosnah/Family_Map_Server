package result;

public class RegisterResult extends Result{
    private String authtoken;
    private String username;
    private String personID;


    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    @Override
    public String toString() {
        return "Username: " + username + ", personID: " + personID + ", authToken: " + authtoken + ", success: " + isSuccess() + ", message: " +
                message;
    }
}
