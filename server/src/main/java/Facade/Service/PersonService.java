package Facade.Service;
import java.util.ArrayList;
import DataAccess.Database;
import Model.Person;
import  Facade.Request.PersonRequest;
import  Facade.Result.PersonResult;

public class PersonService {
// CONSTRUCTORS
    /**
     * PRIVATE CONSTRUCTOR:
     * Constructs a new PersonService object.
     */
    private PersonService() {}


// Class Methods
    /**
     * GET PERSON(S):
     * Handles a variety of PersonRequest objects. In the case that the request includes just an
     * authorization token, the result will include an ArrayList of Person objects; on the other
     * hand, if the request includes both a token and a person ID, then the result will contain
     * only a single Person object (see "PersonRequest.java").
     *
     * @param person_request, a PersonService object
     * @return a PersonResult object
     */
    public PersonResult getPerson(PersonRequest person_request) {
        // check input
        if(person_request == null)
            return new PersonResult("Person retrieval failed. Null pointer passed to " +
                                    "PersonService.getPerson.");
        // declarations
        PersonResult person_result;
        Database database = null;

        try {
            String username;

            // get request data
            String token        = person_request.getToken();
            String person_id    = person_request.getPersonID();

            // open connection
            database = Database.getInstance();
            database.openConnection();

            // verify auth token
            username = database.verifyAuthToken(token);
            if(username == null)
                return new PersonResult("Person retrieval failed. Bad authentication token " +
                        "provided.");

            // get one person
            if(person_id != null) {
                // get person
                Person person = database.getPerson(username, person_id);

                // construct result
                person_result = new PersonResult(person);

            // get many persons
            } else {
                // get family
                ArrayList<Person> people = database.getFamily(username);

                // construct result
                person_result = new PersonResult(people);
            }
            
            // commit changes
            database.closeConnection(true);

        } catch(Exception e) {
            // rollback changes
            if(database != null) if(database.getConnection() != null)
                database.closeConnection(false);
            
            // construct error result
            person_result = new PersonResult(e.getMessage());
        }
        
        return person_result;
    }


// MEMBERS
    /**
     * INSTANCE:
     * A singleton PersonService object.
     */
    private static PersonService instance;
    public static PersonService getInstance() {
        if(instance == null) {
            instance = new PersonService();
        }

        return instance;
    }
}
