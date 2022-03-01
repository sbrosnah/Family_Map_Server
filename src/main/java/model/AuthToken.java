package model;

public class AuthToken {
    private String authtoken;
    private String username;


    /**
     * Construct and AuthToken object
     * @param authtoken
     * @param username
     */
    public AuthToken(String authtoken, String username){
        this.authtoken = authtoken;
        this.username = username;
    }

    /**
     * get an authtoken string
     * @return authtoken string
     */
    public String getAuthtoken() {
        return authtoken;
    }

    /**
     * set an the authtoken string
     * @param authtoken
     */
    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    /**
     * get the associated username
     * @return username string
     */
    public String getUsername() {
        return username;
    }


    /**
     * set the associated username
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * check equality of authtoken objects
     * @param o an object
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof AuthToken) {
            AuthToken oAuthToken = (AuthToken) o;
            return oAuthToken.getAuthtoken().equals(getAuthtoken()) &&
                    oAuthToken.getUsername().equals(getUsername());
        } else {
            return false;
        }
    }
}
