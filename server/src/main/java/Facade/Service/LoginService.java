package Facade.Service;
import DataAccess.Database;
import DataAccess.DatabaseException;
import Model.AuthToken;
import Facade.Request.LoginRequest;
import Facade.Result.LoginResult;

public class LoginService {
// CONSTRUCTORS
    /**
     * PRIVATE CONSTRUCTOR:
     * Constructs a new LoginService object.
     */
    private LoginService() {}


// Class Methods
    /**
     * LOGIN USER:
     * Logs the user in and returns an authorization token.
     *
     * @param login_request, a login request object
     * @return a login result object
     * @see LoginRequest
     * @see LoginResult
     */
    public LoginResult loginUser(LoginRequest login_request) {
        // check input
        if (login_request == null)
            return new LoginResult("Login failed. Null pointer passed to LoginService.loginUser.");

        // declarations
        LoginResult login_result;
        Database database = null;

        try {
            // declarations
            AuthToken auth_token;
            String person_id;
            String token;

            // get user info
            String username = login_request.getUsername();
            String password = login_request.getPassword();

            // open connection
            database = Database.getInstance();
            database.openConnection();

            // login user
            auth_token = database.loginUser(username, password);
            token = auth_token.getToken();
            person_id = auth_token.getPersonID();

            // commit changes
            database.closeConnection(true);

            // construct result
            login_result = new LoginResult(token, username, person_id);

        } catch (DatabaseException e) {
            // rollback changes
            if(database != null) if(database.getConnection() != null)
                database.closeConnection(false);

            // construct error result
            return new LoginResult("Login failed. Database exception encountered. " +
                                   e.getMessage());
        }

        return login_result;
    }


// MEMBERS
    /**
     * INSTANCE:
     * A singleton LoginService object.
     */
    private static LoginService instance;
    public static LoginService getInstance() {
        if(instance == null) {
            instance = new LoginService();
        }

        return instance;
    }
}
