package Facade.Result;

public class LoadResult {
// CONSTRUCTORS
    /**
     * DEFAULT CONSTRUCTOR:
     * Constructs a LoadResult object.
     *
     * @param nu, an integer denoting the number of users added to the database
     * @param np, an integer denoting the number of people added to the database
     * @param ne, an integer denoting the number of events added to the database
     */
    public LoadResult(int ne, int np, int nu) {
        // init members
        message = "Successfully added " + nu + " users, " + np + " persons, and " + ne +
                  " events to the database.";
    }

    /**
     * ERROR CONSTRUCTOR:
     * Constructs an error-containing LoadResult object in the case of an error caught in the
     * LoadService.
     *
     * @param msg, a non-empty string describing the error
     */
    public LoadResult(String msg) {
        // init members
        message = msg;
    }


// MEMBERS
    /**
     * MESSAGE:
     * The message to be returned to the handler.
     */
    private String message;
    public String getMessage() { return message; }
    public void setMessage(String msg) { message = msg; }
}
