package Facade.Request;
import  java.util.ArrayList;
import  Model.Event;
import  Model.Person;
import  Model.User;

public class LoadRequest {
// CONSTRUCTORS
    /**
     * CONSTRUCTOR:
     * Constructs a LoadRequest object.
     *
     * @param us, an ArrayList of User objects
     * @param ps, an ArrayList of Person objects
     * @param es, an ArrayList of Event objects
     */
    public LoadRequest(ArrayList<User> us, ArrayList<Person> ps, ArrayList<Event> es) {
        // init members
        users   = us;
        persons = ps;
        events  = es;
    }


// MEMBERS
    /**
     * EVENTS:
     * An array list of Event objects.
     */
    private ArrayList<Event> events;
    public ArrayList<Event> getEvents() { return events; }

    /**
     * USERS:
     * An array list of User objects.
     */
    private ArrayList<User> users;
    public ArrayList<User> getUsers() { return users; }

    /**
     * PEOPLE:
     * An array list of Person objects.
     */
    private ArrayList<Person> persons;
    public ArrayList<Person> getPeople() { return persons; }
}
