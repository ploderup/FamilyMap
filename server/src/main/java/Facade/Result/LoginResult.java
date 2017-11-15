package Facade.Result;

public class LoginResult {
// Constructors
    /**
     * SUCCESS CONSTRUCTOR:
     * Constructs a LoginResult object.
     *
     * @param tn, an authorization token string
     * @param un, a non-empty string
     * @param id, a non-empty string
     */
    public LoginResult(String tn, String un, String id) {
        token           = tn;
        username        = un;
        person_id       = id;
        error_message   = null;
    }

    /**
     * ERROR CONSTRUCTOR:
     * Constructs an error-containing LoginResult object in the case of an error caught in the
     * LoginService.
     *
     * @param em, a non-empty string describing the error
     */
    public LoginResult(String em) {
        token           = null;
        username        = null;
        person_id       = null;
        error_message   = em;
    }


// Class Methods
    /**
     * TOKEN:
     * A non-empty authorization token string.
     */
    private String token;
    public String getToken() { return token;}
    public void setToken(String tn) { token = tn; }

    /**
     * USERNAME:
     * The username passed in by the request.
     */
    private String username;
    public String getUsername() { return username;}
    public void setUsername(String un) { username = un; }

    /**
     * PERSON ID:
     * A non-empty string containing the person ID of the user's generated person object.
     */
    private String person_id;
    public String getPersonID() { return person_id;}
    public void setPersonID(String id) { person_id = id; }

    /**
     * ERROR MESSAGE:
     * A description of the error encountered.
     */
    private String error_message;
    public String getErrorMessage() { return error_message;}
    public void setErrorMessage(String msg) { error_message = msg; }
}
