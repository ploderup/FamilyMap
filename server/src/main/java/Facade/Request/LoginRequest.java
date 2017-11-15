package Facade.Request;

public class LoginRequest {
// Constructors
    /**
     * DEFAULT CONSTRUCTOR:
     * Constructs a LoginRequest object.
     *
     * @param un, non-empty string
     * @param pw, non-empty string
     */
    public LoginRequest(String un, String pw) {
        // init members
        userName = un;
        password = pw;
    }


// Class Members
    /**
     * USERNAME:
     * A non-empty string.
     */
    private String userName;
    public String getUsername() { return userName; }
    public void setUsername(String un) { userName = un; }

    /**
     * PASSWORD
     * A non-empty string.
     */
    private String password;
    public String getPassword() { return password; }
    public void setPassword(String pw) { password = pw; }
}
