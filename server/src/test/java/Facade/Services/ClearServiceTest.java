package Facade.Services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import DataAccess.Database;
import Facade.Result.ClearResult;
import Facade.Service.ClearService;

import static org.junit.Assert.*;

/**
 * WARNING: This class will clear the database.
 */
public class ClearServiceTest {
// BOOKENDS
    /**
     * SET UP
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        final String sql1 = "INSERT INTO AuthTokenTable VALUES (\"token\", \"username\");";
        final String sql2 = "INSERT INTO EventTable VALUES (\"event_id\", \"username\", " +
                "\"person_id\", 101.76, 87.09, \"Norway\", \"Oslo\", \"birth\", 1994);";
        final String sql3 = "INSERT INTO PersonTable VALUES (\"person_id\", \"username\", " +
                "\"John\", \"Doe\", \"m\", null, null, null);";
        final String sql4 = "INSERT INTO UserTable VALUES (\"username\", \"password\", " +
                "\"email@website.com\", \"John\", \"Doe\", \"m\", \"person_id\");";

        PreparedStatement stmt;

        // add to database
        Database.getInstance().openConnection();
        stmt = Database.getInstance().getConnection().prepareStatement(sql1);
        stmt.executeUpdate();
        stmt = Database.getInstance().getConnection().prepareStatement(sql2);
        stmt.executeUpdate();
        stmt = Database.getInstance().getConnection().prepareStatement(sql3);
        stmt.executeUpdate();
        stmt = Database.getInstance().getConnection().prepareStatement(sql4);
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
     * TEST CLEAR DATABASE:
     * Tests the functionality of the ClearService's sole method, clearDatabase. Note, that this
     * method, like all other service tests, depends on the functionality of individual DAO methods,
     * as well as the functionality of the Database methods as a whole. Considering that the
     * correspondence between service and database methods is nearly one-to-one, only unit tests are
     * performed on the service methods. The DAOs are all tested individually, however, and their
     * tests can be found in their respective folder.
     * @throws Exception
     */
    @Test
    public void testClearDatabase() throws Exception {
        final String TABLE1 = "AuthTokenTable";
        final String TABLE2 = "EventTable";
        final String TABLE3 = "PersonTable";
        final String TABLE4 = "UserTable";

        ClearResult cr;
        PreparedStatement ps;
        ResultSet rs;

        // clear the database
        cr = ClearService.getInstance().clearDatabase();
        assertEquals(cr.getMessage(), "Clear succeeded.");

        // read from database
        Database.getInstance().openConnection();
        ps = Database.getInstance().getConnection().prepareStatement("SELECT * FROM " + TABLE1 +
                ";");
        rs = ps.executeQuery();
        assertFalse(rs.next());
        ps = Database.getInstance().getConnection().prepareStatement("SELECT * FROM " + TABLE2 +
                ";");
        rs = ps.executeQuery();
        assertFalse(rs.next());
        ps = Database.getInstance().getConnection().prepareStatement("SELECT * FROM " + TABLE3 +
                ";");
        rs = ps.executeQuery();
        assertFalse(rs.next());
        ps = Database.getInstance().getConnection().prepareStatement("SELECT * FROM " + TABLE4 +
                ";");
        rs = ps.executeQuery();
        assertFalse(rs.next());
    }
}
