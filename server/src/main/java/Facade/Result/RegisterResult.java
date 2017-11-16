package Facade.Result;

public class RegisterResult {
// Constructors
    /**
     * SUCCESS CONSTRUCTOR:
     * Constructs a RegisterResult object.
     *
     * @param tn, authorization token string
     * @param un, non-empty string
     * @param pi, non-empty string
     */
    public RegisterResult(String tn, String un, String pi) {
        // init members
        token       = tn;
        username    = un;
        person_id   = pi;
        message     = null;
    }

    /**
     * ERROR CONSTRUCTOR:
     * Constructs an error-containing RegisterResult object in the case of an error caught in the
     * RegisterService.
     *
     * @param msg, a non-empty string describing the error
     */
    public RegisterResult(String msg) {
        // init members
        token       = null;
        username    = null;
        person_id   = null;
        message     = msg;
    }


// Class Members
    /**
     * TOKEN:
     * A non-empty authentication token string. Set to null in the case of registration failure.
     */
    private String token;
    public String getToken() { return token; }
    public void setToken(String tn) { token = tn; }

    /**
     * USERNAME:
     * A non-empty string corresponding with a user in the database. Set to null in the case of
     * registration failure.
     */
    private String username;
    public String getUsername() { return username; }
    public void setUsername(String un) { username = un; }

    /**
     * PERSON ID:
     * A non-empty string corresponding with a person in the database. Set to null in the case of
     * registration failure.
     */
    private String person_id;
    public String getPersonID() { return person_id; }
    public void setPersonID(String id) { person_id = id; }

    /**
     * MESSAGE:
     * A non-empty string detailing the reason for registration failure. Set to null in the case of
     * a successful registration.
     */
    private String message;
    public String getMessage() { return message; }
    public void setMessage(String msg) { message = msg; }
}
