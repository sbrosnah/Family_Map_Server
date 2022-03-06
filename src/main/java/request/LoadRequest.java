package request;

import model.User;
import model.Person;
import model.Event;
import java.util.ArrayList;

public class LoadRequest {
    ArrayList<User> users;
    ArrayList<Person> persons;
    ArrayList<Event> events;

    public LoadRequest(ArrayList<User> users, ArrayList<Person> persons, ArrayList<Event> events){
        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public void setPersons(ArrayList<Person> persons) {
        this.persons = persons;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}
