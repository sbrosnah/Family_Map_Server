package service;

import result.AllPersonResult;
import model.AuthToken;
import model.Person;
import model.User;

public class AllPersonService {
    /**
     * Returns ALL family members of the current user. The current user is determined by the provided authtoken.
     * @param authToken
     * @return
     */
    public AllPersonResult processRequest(String authToken) {return null;}

    /**
     * Gets the personID of the user of the authToken
     * @param authToken
     * @return
     */
    private String getAssciatedPersonID(String authToken) {return null;}
}
