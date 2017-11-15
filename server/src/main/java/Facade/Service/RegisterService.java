package Facade.Service;
import DataAccess.Database;
import DataAccess.DatabaseException;
import Model.AuthToken;
import Facade.Request.RegisterRequest;
import Facade.Result.RegisterResult;

public class RegisterService {
// CONSTRUCTORS
    /**
     * PRIVATE CONSTRUCTOR:
     * Constructs a new RegisterService object.
     */
    private RegisterService() {}


// Class Methods
    /**
     * REGISTER USER:
     * Creates a new user account, generates four generations of ancestor data for the new user,
     * logs the user in, and returns a RegisterResult--including an authorization token.
     *
     * @param register_request, a register request
     * @return a register result
     */
    public RegisterResult registerUser(RegisterRequest register_request) {
        // check input
        if (register_request == null)
            return new RegisterResult("Registration failed. Null pointer passed to " +
                                      "RegisterService.registerUser.");

        // declarations
        RegisterResult register_result;
        Database database = null;

        try {
            // declarations
            AuthToken auth_token;
            String token;
            String person_id;

            // get user info
            String username     = register_request.getUsername();
            String password     = register_request.getPassword();
            String email        = register_request.getEmail();
            String first_name   = register_request.getFirstName();
            String last_name    = register_request.getLastName();
            String gender       = register_request.getGender();

            // open connection
            database = Database.getInstance();
            database.openConnection();

            // register user
            database.registerUser(username, password, email, first_name, last_name, gender);

            // login user
            auth_token = database.loginUser(username, password);
            token = auth_token.getToken();
            person_id = auth_token.getPersonID();

            // commit changes
            database.closeConnection(true);

            // construct result
            register_result = new RegisterResult(token, username, person_id);

        } catch (DatabaseException e) {
            // rollback changes
            if(database != null) if(database.getConnection() != null)
                database.closeConnection(false);

            // construct error result
            return new RegisterResult("RegisterService.registerUser failed. Database exception " +
                    "encountered. " + e.getMessage());
        }

        return register_result;
    }


// MEMBERS
    /**
     * INSTANCE:
     * A singleton RegisterService object.
     */
    private static RegisterService instance;
    public static RegisterService getInstance() {
        if(instance == null) {
            instance = new RegisterService();
        }

        return instance;
    }
}
