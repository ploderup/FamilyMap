package Facade.Services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import DataAccess.DataAccessObject.UserDAO;
import DataAccess.Database;
import Facade.Request.LoginRequest;
import Facade.Result.LoginResult;
import Facade.Service.LoginService;
import Model.User;

import static org.junit.Assert.*;

/**
 * WARNING: This class will clear the database.
 */
public class LoginServiceTest {
// BOOKENDS
    /**
     * SET UP
     */
    @Before
    public void setUp() throws Exception {
        Database.getInstance().openConnection();
        UserDAO.create(new User("username", "password", "jd@email.com", "John", "Doe", "m"));
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
     * TEST LOGIN USER:
     * Tests the functionality of the LoginService's sole method, loginUser, when passed requests
     * for users both registered and not registered with the database. Note, that this method, like
     * all other service tests, depends on the functionality of individual DAO methods, as well as
     * the functionality of the Database methods as a whole. Considering that the correspondence
     * between service and database methods is nearly one-to-one, only unit tests are performed on
     * the service methods. The DAOs are all tested individually, however, and their tests can be
     * found in their respective folder.
     * @throws Exception
     */
    @Test
    public void testLoginUser() throws Exception {
        LoginRequest login_request;
        LoginResult login_result;

        // attempt bad login
        login_request = new LoginRequest("unregistered_username", "password");
        login_result = LoginService.getInstance().loginUser(login_request);
        assertEquals(login_result.getToken(), null);
        assertEquals(login_result.getUsername(), null);
        assertEquals(login_result.getPersonID(), null);
        assertNotEquals(login_result.getMessage(), null);

        // attempt valid login
        login_request = new LoginRequest("username", "password");
        login_result = LoginService.getInstance().loginUser(login_request);
        assertNotEquals(login_result.getToken(), null);
        assertEquals(login_result.getUsername(), "username");
        assertNotEquals(login_result.getPersonID(), null);
        assertEquals(login_result.getMessage(), null);
    }
}
