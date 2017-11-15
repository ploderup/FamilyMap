package Facade.Result;
import  java.util.ArrayList;
import  Model.Person;

public class PersonResult {
    /**
     * PEOPLE CONSTRUCTOR:
     * Constructs a PersonResult object.
     *
     * @param da, an ArrayList of Person objects
     */
    public PersonResult(ArrayList<Person> da) {
        data        = da;
        person_id   = null;
        descendant  = null;
        first_name  = null;
        last_name   = null;
        gender      = null;
        father_id   = null;
        mother_id   = null;
        spouse_id   = null;
        message     = null;
    }

    /**
     * PERSON CONSTRUCTOR:
     * Constructs a PersonResult object.
     *
     * @param p, a not-null Person object
     */
    public PersonResult(Person p) {
        data        = null;
        person_id   = p.getPersonID();
        descendant  = p.getDescendant();
        first_name  = p.getFirstName();
        last_name   = p.getLastName();
        gender      = p.getGender();
        father_id   = p.getFatherID();
        mother_id   = p.getMotherID();
        spouse_id   = p.getSpouseID();
        message     = null;
    }

    /**
     * ERROR CONSTRUCTOR:
     * Constructs an error-containing PersonResult object in the case of an error caught in the
     * PersonService.
     *
     * @param msg, a non-empty string describing the error
     */
    public PersonResult(String msg) {
        data        = null;
        person_id   = null;
        descendant  = null;
        first_name  = null;
        last_name   = null;
        gender      = null;
        father_id   = null;
        mother_id   = null;
        spouse_id   = null;
        message     = msg;
    }


// Class Members
    /**
     * DATA:
     * An array list of person objects. Null in case of call to /person/[personID] protocol or
     * service failure.
     */
    private ArrayList<Person> data;
    public ArrayList<Person> getData() { return data; }
    public void setPersons(ArrayList<Person> da) { data = da; }
    
    /**
     * PERSON ID:
     * A non-empty string. The person's unique ID Null in case of call to /person API or service
     * failure.
     */
    private String person_id;
    public String getPersonID() { return person_id; }
    public void setPersonID(String id) { person_id = id; }
    
    /**
     * DESCENDANT:
     * A non-empty string. The username of the user this person belongs to. Null in case of call to
     * /person API or service failure.
     */
    private String descendant;
    public String getDescendant() { return descendant; }
    public void setDescendant(String dt) { descendant = dt; }
    
    /**
     * FIRST NAME:
     * A non-empty string. Null in case of call to /person API or service failure.
     */
    private String first_name;
    public String getFirstName() { return first_name; }
    public void setFirstName(String fn) { first_name = fn; }
    
    /**
     * LAST NAME:
     * A non-empty string. Null in case of call to /person API or service failure.
     */
    private String last_name;
    public String getLastName() { return last_name; }
    public void setLastName(String ln) { last_name = ln; }
    
    /**
     * GENDER:
     * A non-empty string ("m" or "f"). Null in case of call to /person API or service failure.
     */
    private String gender;
    public String getGender() { return gender; }
    public void setGender(String gr) { gender = gr; }
    
    /**
     * FATHER ID:
     * A non-empty string. ID of person's father. Null in case of call to /person API or service
     * failure.
     */
    private String father_id;
    public String getFatherID() { return father_id; }
    public void setFatherID(String id) { father_id = id; }
    
    /**
     * MOTHER ID:
     * A non-empty string. ID of person's mother Null in case of call to /person API or service
     * failure.
     */
    private String mother_id;
    public String getMotherID() { return mother_id; }
    public void setMotherID(String id) { mother_id = id; }
    
    /**
     * SPOUSE ID:
     * A non-empty string. ID of person's spouse. Null in case of call to /person API or service
     * failure.
     */
    private String spouse_id;
    public String getSpouseID() { return spouse_id; }
    public void setSpouseID(String id) { spouse_id = id; }

    /**
     * MESSAGE:
     * Not-null only in the case of PersonService failure. A string detailing the reason for
     * failure.
     */
    private String message;
    public String getMessage() { return message; }
    public void setMessage(String msg) { message = msg; }
}
