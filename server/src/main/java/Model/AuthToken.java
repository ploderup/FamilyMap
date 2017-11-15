package Model;
import  static java.util.UUID.randomUUID;

public class AuthToken {
// Constructors
    /**
     * DEFAULT CONSTRUCTOR:
     * Constructs an AuthToken object.
     */
    public AuthToken() {}

    /**
     * CONSTRUCTOR:
     * Constructs an AuthToken object by generating a unique token.
     *
     * @param un, a unique user id
     */
    public AuthToken(String un) {
        token       = randomUUID().toString();
        username    = un;
        person_id   = null;
    }

    /**
     * LOGIN CONSTRUCTOR:
     * Constructs an AuthToken object from given token and user ID values.  Note, that this method
     * is called exclusively by the Database.loginUser method so as to allow that same method to
     * return both a novel token and the person ID of the user associated with the token.
     */
    public AuthToken(String un, String pi) {
        token       = randomUUID().toString();
        username    = un;
        person_id   = pi;
    }

    /**
     * CONSTRUCTOR:
     * Constructs an AuthToken object.
     *
     * @param tn, a non-empty string
     * @param un, a non-empty string
     * @param pi, a (potentially null) string
     */
    public AuthToken(String tn, String un, String pi) {
        token = tn;
        username = un;
        person_id = pi;
    }


// Class Methods
    /**
     * MEMBERS VALID:
     * Checks whether current members are valid. Note, that this method does not perform any checks
     * regarding 'person_id'.
     *
     * @return boolean denoting result
     */
    public boolean membersValid() {
        // check members
        if(token == null || token.equals("")) return false;
        if(username == null || username.equals("")) return false;

        // tests passed
        return true;
    }

// Class Members
    /**
     * AUTHENTICATION TOKEN:
     * A unique authentication token.
     */
    private String token;
    public String getToken() { return token; }
    public void setToken(String tn) { token = tn; }

    /**
     * USERNAME:
     * The username of the user associated with the authentication token.
     */
    private String username;
    public String getUsername() { return username; }
    public void setUsername(String un) { username = un; }

    /**
     * PERSON ID:
     * This is used only in cases in which a service needs both a username and its associated person
     * id. Note, person IDs are never added to the AuthTokenTable.
     */
    private String person_id;
    public String getPersonID() { return person_id; }
    public void setPersonID(String pi) { person_id = pi; }
}
