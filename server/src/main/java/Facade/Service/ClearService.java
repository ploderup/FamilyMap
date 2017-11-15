package Facade.Service;
import  DataAccess.Database;
import  Facade.Result.ClearResult;

public class ClearService {
// Constructors
    /**
     * PRIVATE CONSTRUCTOR:
     * Constructs a ClearService object.
     */
    private ClearService() {}


// Class Methods
    /**
     * CLEAR DATABASE:
     * Deletes ALL data from the database, including user accounts, authorization tokens, and
     * generated person and event data. If unsuccessful, the result returned contains a message
     * describing the error encountered.
     *
     * @return  a ClearResult object
     * @see     ClearResult
     * @see     Database
     */
    public ClearResult clearDatabase() {
        // declarations
        ClearResult clear_result;
		Database database = null;

        try {
			// open connection
			database = Database.getInstance();
			database.openConnection();

            // tell db to delete tables
            database.clear();

			// commit changes
			database.closeConnection(true);
			
            // construct result
            clear_result = new ClearResult();

        } catch(Exception e) {
			// rollback changes
            if(database != null) if(database.getConnection() != null)
			    database.closeConnection(false);
			
            // construct error result
            clear_result = new ClearResult(e.getMessage());
        }
		
        return clear_result;
    }


// MEMBERS
    /**
     * INSTANCE:
     * A singleton ClearService object.
     */
    private static ClearService instance;
    public static ClearService getInstance() {
        if(instance == null) {
            instance = new ClearService();
        }

        return instance;
    }
}
