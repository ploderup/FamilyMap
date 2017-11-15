package Facade.Service;
import  java.util.ArrayList;
import  DataAccess.Database;
import  DataAccess.DatabaseException;
import  Facade.Request.LoadRequest;
import  Facade.Result.LoadResult;
import  Model.*;

public class LoadService {
// CONSTRUCTORS
    /**
     * PRIVATE CONSTRUCTOR:
     * Constructs a new LoadService object.
     */
    private LoadService() {}

// METHODS
    /**
     * LOAD DATABASE:
     * Clears all data from the database, and then loads the posted user, person, and event data
     * into the database.
     *
     * @param load_request  a LoadRequest object
     * @return              a LoadResult object
     */
    public LoadResult loadDatabase(LoadRequest load_request) {
        // check input
        if(load_request == null)
            return new LoadResult("Load failed. Null pointer passed to LoadService.loadDatabase.");

        // declarations
        LoadResult load_result;
		Database database = null;
		
        // get arrays
        ArrayList<Event> events     = load_request.getEvents();
        ArrayList<Person> people    = load_request.getPeople();
        ArrayList<User> users       = load_request.getUsers();

        try {
			// open connection
			database = Database.getInstance();
			database.openConnection();

            // load database
            database.load(events, people, users);

			// commit changes
			database.closeConnection(true);
			
            // construct result
            load_result = new LoadResult(events.size(), people.size(), users.size());
        }
        catch(DatabaseException e) {
			// rollback changes
			if(database != null) if(database.getConnection() != null)
                database.closeConnection(false);
			
            // construct error result
            load_result = new LoadResult(e.getMessage());
        }

        return load_result;
    }


// MEMBERS
    /**
     * INSTANCE:
     * A singleton LoadService object.
     */
    private static LoadService instance;
    public static LoadService getInstance() {
        if(instance == null) {
            instance = new LoadService();
        }

        return instance;
    }
}
