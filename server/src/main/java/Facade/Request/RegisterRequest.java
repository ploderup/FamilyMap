package Facade.Request;

public class RegisterRequest {
    /**
     * METHOD
     * Default Constructor
     *
     * FUNCTIONALITY
     * Creates a RegisterRequest object.
     *
     * PARAMETERS
     * @param un, non-empty string
     * @param pwd, non-empty string
     * @param e, non-empty string
     * @param fn, non-empty string
     * @param ln, non-empty string
     * @param g, non-empty string, "m" or "f"
     */
    public RegisterRequest(String un, String pwd, String e, String fn, String ln, String g) {
        userName    = un;
        password    = pwd;
        email       = e;
        firstName   = fn;
        lastName    = ln;
        gender      = g;
    }


// MEMBERS
    /**
     * USERNAME
     */
    private String userName;
    public String getUsername()    { return userName; }
    public void setUsername(String un)     { userName = un; }
    
    /**
     * PASSWORD
     */
    private String password;
    public String getPassword()    { return password; }
    public void setPassword(String pwd)    { password = pwd; }
    
    /**
     * EMAIL
     */
    private String email;
    public String getEmail()       { return email; }
    public void setEmail(String em)        { email = em; }
    
    /**
     * FIRST NAME
     */
    private String firstName;
    public String getFirstName()   { return firstName; }
    public void setFirstName(String fn)    { firstName = fn; }
    
    /**
     * LAST NAME
     */
    private String lastName;
    public String getLastName()    { return lastName; }
    public void setLastName(String ln)     { lastName = ln; }

    /**
     * GENDER
     */
    private String gender;
    public String getGender()      { return gender; }
    public void setGender(String g)        { gender = g; }
}
