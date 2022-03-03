package service;

import model.Event;
import dao.EventDAO;
import model.AuthToken;
import dao.AuthTokenDAO;
import result.EventResult;
import result.Result;


public class EventService {
    /**
     * Returns the single Event object with the specified ID (if the event is associated with the current user).
     * The current user is determined by the provided authtoken.
     * @param authToken
     * @param eventID
     * @return
     */
    public Result processRequest(String authToken, String eventID) {return null;}

    /**
     * validates that the event is associated with the current user
     * @param authToken
     * @param eventID
     * @return
     */
    private boolean validateAssociation(String authToken, String eventID) {return false;}
}
