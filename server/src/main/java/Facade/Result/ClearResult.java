package Facade.Result;

public class ClearResult {
// CONSTRUCTORS
    /**
     * DEFAULT CONSTRUCTOR:
     * Constructs a ClearResult object.
     */
    public ClearResult() {
        message = "Clear succeeded.";
    }

    /**
     * ERROR CONSTRUCTOR:
     * Constructs an error-containing ClearResult object in the case of an error caught in the
     * ClearService.
     *
     * PARAMETERS
     * @param msg, a non-empty string describing the error
     */
    public ClearResult(String msg) {
        message = msg;
    }
    

// MEMBERS
    /**
     * MESSAGE:
     * A non-empty string.
     */
    private String message;
    public String getMessage() { return message; }
    public void setMessage(String msg) { message = msg; }
}