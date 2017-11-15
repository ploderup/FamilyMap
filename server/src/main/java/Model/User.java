package Model;
import  static java.util.UUID.randomUUID;

public class User {
// Constructors
	/**
	 * DEFAULT CONSTRUCTOR:
	 * Constructs an User object.
	 */
	public User() {}

	/**
     * REGISTRATION CONSTRUCTOR:
     * Constructs a User object with values supplied by the register service.
     *
     * @param un	userName
     * @param pw	password
     * @param em	email
     * @param fn	first name
     * @param ln	last name
     * @param gr	gender
     */
    public User(String un, String pw, String em, String fn, String ln, String gr) {
		// import values
		userName    = un;
        password    = pw;
        email       = em;
        firstName   = fn;
        lastName    = ln;
        gender      = gr;

        // generate ID
        personID	= randomUUID().toString();
    }

	/**
	 * TOTAL CONSTRUCTOR
	 * Constructs a User object solely from supplied values (including IDs).
	 */
	public User(String un, String pw, String em, String fn, String ln, String gr, String pi) {
		userName	= un;
		password	= pw;
		email		= em;
		firstName	= fn;
		lastName	= ln;
		gender		= gr;
		personID	= pi;
	}
	
// Class Methods
	/**
	 * MEMBERS VALID:
	 * Checks whether the members of the User object are valid. Note, that this method checks simply
	 * for member existence and, in the case of IDs, does not check for uniqueness.
	 *
	 * @return	whether the members are valid or not
	 */
	public boolean membersValid() {
		// test members
		if(userName == null || userName.equals("")) return false;
		if(password == null || password.equals("")) return false;
		if(email == null	|| email.equals("") ||
		   !email.matches("[A-Za-z0-9]+@[A-Za-z0-9]+\\.[A-Za-z0-9]+")) return false;
		if(firstName == null || firstName.equals("")) return false;
		if(lastName == null || lastName.equals("")) return false;
		if(gender == null || !gender.equals("m") && !gender.equals("f")) return false;
		if(personID == null || personID.equals("")) return false;

		// tests passed
		return true;
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
	 * PASSWORD:
	 * A non-empty string.
	 */
	private String password;
    public String getPassword() { return password; }
    public void setPassword(String pw) { password = pw; }
	
	/**
	 * EMAIL:
	 * A non-empty string of valid email format.
	 */
	private String email;
    public String getEmail() { return email; }
    public void setEmail(String em) { email = em; }
    
	/**
	 * FIRST NAME:
	 * A non-empty string.
	 */
	private String firstName;  // non-empty string
    public String getFirstName() { return firstName; }
    public void setFirstName(String fn) { firstName = fn; }
    
	/**
	 * LAST NAME:
	 * A non-empty string.
	 */
	private String lastName;   // non-empty string
    public String getLastName() { return lastName; }
    public void setLastName(String ln) { lastName = ln; }
    
	/**
	 * GENDER:
	 * A non-empty string, either "m" or "f".
	 */
	private String gender;
    public String getGender() { return gender; }
    public void setGender(String gr) { gender = gr; }
    
	/**
	 * PERSON ID:
	 * A unique person ID.
	 */
	private String personID;
    public String getPersonID() { return personID; }
    public void setPersonID(String ud) { personID = ud; }
}

