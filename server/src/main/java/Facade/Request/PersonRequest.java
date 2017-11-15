package Facade.Request;

public class PersonRequest {
// Constructors
    /**
     * PERSON CONSTRUCTOR:
     * Constructs a PersonRequest object. When sent to PersonService, the presence of both an
     * authorization token and a person ID denotes that only a single Person object (the one
     * associated with the given ID) is requested.
     *
     * @param tn, an authorization token string
     * @param id, a non-empty string representing a person's ID
     */
    public PersonRequest(String tn, String id) {
        // init members
        token       = tn;
        person_id   = id;
    }

    /**
     * PERSONS CONSTRUCTOR:
     * Constructs a PersonRequest object. When sent to PersonService, the presence of an
     * authorization token and lack of a person ID denotes that an array of all Persons associated
     * with the user associated with the given token is requested.
     *
     * @param tn, an authorization token string
     */
    public PersonRequest(String tn) {
        // init members
        token  = tn;
        person_id   = null;
    }


// Class Members
    /**
     * TOKEN:
     * An authentication token string.
     */
    private String token;
    public String getToken() { return token; }
    public void setToken(String tn) { token = tn; }
    
    /**
     * PERSON ID:
     * A non-empty (but potentially null) string. See constructor commentary.
     */
    private String person_id;
    public String getPersonID() { return person_id; }
    public void setPersonID(String id) { person_id = id; }
}
