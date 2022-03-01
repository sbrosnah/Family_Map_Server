package service;

import dao.Database;
import model.User;
import model.Person;
import model.Event;
import dao.UserDAO;
import dao.PersonDAO;
import dao.EventDAO;
import request.LoadRequest;
import request.Request;
import result.Result;

import java.util.ArrayList;

public class LoadService {
    /**
     * process the load request and return a result
     * @param request
     * @return Result object
     */
    public Result processRequest(Request request) {return null;}

    /**
     * Load the users into a table
     * @param users
     */
    private void LoadUsers(ArrayList<User> users){}

    /**
     * Load the people into a table
     * @param people
     */
    private void LoadPeople(ArrayList<Person> people) {}

    /**
     * Load the events into a table
     * @param events
     */
    private void LoadEvents(ArrayList<Event> events) {}

}
