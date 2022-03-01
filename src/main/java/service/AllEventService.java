package service;

import model.Event;
import java.util.ArrayList;
import dao.EventDAO;
import model.AuthToken;
import dao.AuthTokenDAO;
import request.Request;
import result.AllEventResult;
import result.Result;


public class AllEventService {
    /**
     * Returns ALL events for ALL family members of the current user. The current user is determined from the provided auth token.
     * @param authToken
     * @return
     */
    public Result processRequest(Request request) {return null;}

}
