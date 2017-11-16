package Facade.Services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.PreparedStatement;

import DataAccess.Database;
import Facade.Request.PersonRequest;
import Facade.Result.PersonResult;
import Facade.Service.PersonService;

import static org.junit.Assert.*;

/**
 * WARNING: This class will clear the database.
 */
public class PersonServiceTest {
// BOOKENDS
    /**
     * SET UP
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        final String sql0 = "DELETE FROM AuthTokenTable;";
        final String sql1 = "DELETE FROM PersonTable;";
        final String sql2 = "INSERT INTO AuthTokenTable VALUES (\"auth_token1\", \"username1\");";
        final String sql3 = "INSERT INTO AuthTokenTable VALUES (\"auth_token2\", \"username2\");";
        final String sql4 = "INSERT INTO PersonTable VALUES (\"person_id1\", \"username1\", " +
                "\"John\", \"Doe\", \"m\", null, null, null);";
        final String sql5 = "INSERT INTO PersonTable VALUES (\"person_id2\", \"username1\", " +
                "\"John\", \"Doe\", \"m\", null, null, null);";
        final String sql6 = "INSERT INTO PersonTable VALUES (\"person_id3\", \"username2\", " +
                "\"John\", \"Doe\", \"m\", null, null, null);";

        PreparedStatement stmt;

        // add to database
        Database.getInstance().openConnection();
        stmt = Database.getInstance().getConnection().prepareStatement(sql0);
        stmt.executeUpdate();
        stmt = Database.getInstance().getConnection().prepareStatement(sql1);
        stmt.executeUpdate();
        stmt = Database.getInstance().getConnection().prepareStatement(sql2);
        stmt.executeUpdate();
        stmt = Database.getInstance().getConnection().prepareStatement(sql3);
        stmt.executeUpdate();
        stmt = Database.getInstance().getConnection().prepareStatement(sql4);
        stmt.executeUpdate();
        stmt = Database.getInstance().getConnection().prepareStatement(sql5);
        stmt.executeUpdate();
        stmt = Database.getInstance().getConnection().prepareStatement(sql6);
        stmt.executeUpdate();
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
     * TEST GET PERSON:
     * Tests the functionality of the PersonService's sole method, getPerson when passed a request for
     * a single person. Note, that this method, like all other service tests, depends on the
     * functionality of individual DAO methods, as well as the functionality of the Database methods
     * as a whole. Considering that the correspondence between service and database methods is
     * nearly one-to-one, only unit tests are performed on the service methods. The DAOs are all
     * tested individually, however, and their tests can be found in their respective folder.
     * @throws Exception
     */
    @Test
    public void testGetPerson() throws Exception {
        PersonRequest prqst;
        PersonResult prslt;

        // get person from other user
        prqst = new PersonRequest("auth_token2", "person_id1");
        prslt = PersonService.getInstance().getPerson(prqst);
        assertEquals(prslt.getPersonID(), null);
        assertEquals(prslt.getData(), null);
        assertNotEquals(prslt.getMessage(), null);

        // get person from correct user
        prqst = new PersonRequest("auth_token1", "person_id1");
        prslt = PersonService.getInstance().getPerson(prqst);
        assertEquals(prslt.getPersonID(), "person_id1");
        assertEquals(prslt.getData(), null);
        assertEquals(prslt.getMessage(), null);
    }


    /**
     * TEST GET FAMILY:
     * Tests the functionality of the PersonService's sole method, getPerson when passed a request for
     * multiple persons. See also note on testGetPerson method.
     * @throws Exception
     */
    @Test
    public void testGetFamily() throws Exception {
        PersonRequest prqst;
        PersonResult prslt;

        // get person from user 1
        prqst = new PersonRequest("auth_token1");
        prslt = PersonService.getInstance().getPerson(prqst);
        assertEquals(prslt.getPersonID(), null);
        assertEquals(prslt.getData().size(), 2);
        assertEquals(prslt.getMessage(), null);

        // get person from correct user
        prqst = new PersonRequest("auth_token2");
        prslt = PersonService.getInstance().getPerson(prqst);
        assertEquals(prslt.getPersonID(), null);
        assertEquals(prslt.getData().size(), 1);
        assertEquals(prslt.getMessage(), null);
    }
}
