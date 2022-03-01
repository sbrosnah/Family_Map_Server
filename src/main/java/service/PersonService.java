package service;

import result.PersonResult;
import model.Person;
import model.AuthToken;
import dao.AuthTokenDAO;
import dao.PersonDAO;

import result.Result;
import request.Request;

public class PersonService implements Service{
    /**
     * Returns the single Person object with the specified ID (if the person is associated with the current user).
     * The current user is determined by the provided authtoken.
     * @param personID
     * @return the personResult
     */
    public Result processRequest(Request request) {return null;}


}
