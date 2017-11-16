package Facade.Services;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import DataAccess.DataAccessObject.PersonDAO;
import DataAccess.DataAccessObject.UserDAO;
import DataAccess.Database;
import Facade.Request.FillRequest;
import Facade.Result.FillResult;
import Facade.Service.FillService;
import Model.Person;
import Model.User;

import static org.junit.Assert.*;

/**
 * WARNING: This class will clear the database.
 */
public class FillServiceTest {
// BOOKENDS
    /**
     * SET UP:
     * Sets up for all FillService testing. See DAO tests for verification of functionality. See
     * also note on method testFillDatabase, below.
     */
    @Before
    public void setUp() throws Exception {
        Database.getInstance().openConnection();
        UserDAO.deleteAll();
        PersonDAO.deleteAll();
        UserDAO.create(new User("username1", "password", "email@email.com", "John", "Doe", "m",
                "person_id1"));
        PersonDAO.create(new Person("person_id1", "username1", "John", "Doe", "m", null, null,
                null));
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
     * TEST FILL DATABASE:
     * Tests the functionality of the FillService's sole method, fillDatabase when passed a requests
     * varying numbers of generations. Note, that this method, like all other service tests, depends
     * on the functionality of individual DAO methods, as well as the functionality of the Database
     * methods as a whole. Considering that the correspondence between service and database methods
     * is nearly one-to-one, only unit tests are performed on the service methods. The DAOs are all
     * tested individually, however, and their tests can be found in their respective folder.
     * @throws Exception
     * @see DataAccess.DataAccessObject.EventDAO
     */
    @Test
    public void testFillDatabase() throws Exception {
        FillRequest fill_request;
        FillResult fill_result;

        // test default fill
        fill_request = new FillRequest("username1");
        fill_result = FillService.getInstance().fillDatabase(fill_request);
        assertEquals(fill_result.getMessage().substring(0, 29), "Successfully added 31 " +
                "persons"); // don't check num events (somewhat random)

        // test parameterized fill
        fill_request = new FillRequest("username1", 10);
        fill_result = FillService.getInstance().fillDatabase(fill_request);
        assertEquals(fill_result.getMessage().substring(0, 31), "Successfully added 2047 " +
                "persons"); // don't check num events (somewhat random)
    }
}
