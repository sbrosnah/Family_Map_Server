package request;

import model.User;
import model.Person;
import model.Event;
import java.util.ArrayList;

public class LoadRequest {
    ArrayList<User> users;
    ArrayList<Person> people;
    ArrayList<Event> events;

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public ArrayList<Person> getPeople() {
        return people;
    }

    public void setPeople(ArrayList<Person> people) {
        this.people = people;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}
