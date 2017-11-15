package Facade.Service;
import DataAccess.Database;
import DataAccess.DatabaseException;
import  Facade.Request.FillRequest;
import  Facade.Result.FillResult;

public class FillService {
// CONSTRUCTORS
    /**
     * PRIVATE CONSTRUCTOR:
     * Constructs a new FillService object.
     */
    private FillService() {}

// METHODS
    /**
     * FILL RESULT:
     * Populates the server's database with generated data for the specified username (given by
     * the fill request). The username must be a user already registered with the server. If there
     * is any data in the database already associated with the given username, it is deleted. The
     * optional inclusion of an integer, 'num_generations', value with the request allows the user
     * to specify the number of generations with which to fill their tree.
     *
     * @param fill_request, a FillRequest object
     * @return a FillResult object
     */
    public FillResult fillDatabase(FillRequest fill_request) {
        if(fill_request == null)
            return new FillResult("Fill failed. Service.Request was a null pointer.");

        Database database = null;

        try {
            final int NUM_PEOPLE_INDEX = 0;
            final int NUM_EVENTS_INDEX = 1;

            // get request info
            String username = fill_request.getUsername();
            int num_generations = fill_request.getNumGenerations();

            // open connection
            database = Database.getInstance();
            database.openConnection();

            // fill user's tree
            int[] result = database.fillUserTree(username, num_generations);

            // commit changes
            database.closeConnection(true);

            return new FillResult(result[NUM_PEOPLE_INDEX], result[NUM_EVENTS_INDEX]);

        } catch(DatabaseException e) {
            // rollback changes
            if(database != null) if(database.getConnection() != null)
                database.closeConnection(false);

            return new FillResult("Fill failed. Database exception encountered. " + e.getMessage());
        }
    }


// MEMBERS
    /**
     * INSTANCE:
     * A singleton FillService object.
     */
    private static FillService instance;
    public static FillService getInstance() {
        if(instance == null) {
            instance = new FillService();
        }

        return instance;
    }
}
