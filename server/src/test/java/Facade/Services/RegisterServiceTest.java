package Facade.Services;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import DataAccess.DataAccessObject.UserDAO;
import DataAccess.Database;
import Facade.Request.RegisterRequest;
import Facade.Result.RegisterResult;
import Facade.Service.RegisterService;
import Model.User;

import static org.junit.Assert.*;

/**
 * WARNING: This class will clear the database.
 */
public class RegisterServiceTest {
// BOOKENDS
    /**
     * SET UP
     */
    @Before
    public void setUp() throws Exception {
        Database.getInstance().openConnection();
        UserDAO.create(new User("taken_username", "password", "jd@email.com", "John", "Doe", "m"));
        Database.getInstance().closeConnection(true);
    }

    /**
     * TEAR DOWN
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        Database.getInstance().openConnection();
        Database.getInstance().clear();
        Database.getInstance().closeConnection(true);
    }

// TESTS
    /**
     * TEST REGISTER USER:
     * Tests the functionality of the RegisterService's sole method, registerUser, when passed
     * requests for users both registered and not registered with the database. Note, that this
     * method, like all other service tests, depends on the functionality of individual DAO methods,
     * as well as the functionality of the Database methods as a whole. Considering that the
     * correspondence between service and database methods is nearly one-to-one, only unit tests are
     * performed on the service methods. The DAOs are all tested individually, however, and their
     * tests can be found in their respective folder.
     * @throws Exception
     */
    @Test
    public void RegisterUser() throws Exception {
        RegisterRequest register_request;
        RegisterResult register_result;

        // attempt bad register
        register_request = new RegisterRequest("taken_username", "password", "jd@email.com", "John",
                "Doe", "m");
        register_result = RegisterService.getInstance().registerUser(register_request);
        assertEquals(register_result.getToken(), null);
        assertEquals(register_result.getUsername(), null);
        assertEquals(register_result.getPersonID(), null);
        assertNotEquals(register_result.getMessage(), null);

        // attempt valid register
        register_request = new RegisterRequest("username", "password", "jd@email.com", "John",
                "Doe", "m");
        register_result = RegisterService.getInstance().registerUser(register_request);
        assertNotEquals(register_result.getToken(), null);
        assertEquals(register_result.getUsername(), "username");
        assertNotEquals(register_result.getPersonID(), null);
        assertEquals(register_result.getMessage(), null);
    }
}
