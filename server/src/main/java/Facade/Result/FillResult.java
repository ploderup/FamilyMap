package Facade.Result;

public class FillResult {
// Constructors
    /**
     * SUCCESS CONSTRUCTOR:
     * Constructs a FillResult object.
     *
     * @param np, an integer greater than or equal to zero denoting number of people
     * @param ne, an integer greater than or equal to zero denoting number of events
     */
    public FillResult(int np, int ne) {
        final String SUCCESS_MSG_1 = "Successfully added ";
        final String SUCCESS_MSG_2 = " persons and ";
        final String SUCCESS_MSG_3 = " events to the database.";

        message = SUCCESS_MSG_1 + np + SUCCESS_MSG_2 + ne + SUCCESS_MSG_3;
    }

    /**
     * ERROR CONSTRUCTOR:
     * Constructs an error-containing FillResult object in the case of an error caught in the
     * FillService.
     *
     * @param msg, a non-empty string describing the error
     */
    public FillResult(String msg) {
        message = msg;
    }


// Class Members
    /**
     * MESSAGE:
     * A non-empty string.
     */
    private String message;
    public String getMessage() { return message; }
    public void setMessage(String msg) { message = msg; }

}
