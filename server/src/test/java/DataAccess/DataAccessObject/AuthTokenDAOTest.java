package DataAccess.DataAccessObject;

import org.junit.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import DataAccess.Database;
import Model.AuthToken;

import static org.junit.Assert.*;

/**
 * AUTH TOKEN DAO TEST:
 * Used for unit testing the AuthTokenDAO. Note, that as all DAO methods are static, no DAO object
 * is initialized here. Also, note that while most Data Access Objects have each of the four Create,
 * Read, Update, and Delete (CRUD) methods, none of the DAOs in this project have an update method
 * as the method is unnecessary for the server to function.
 */
public class AuthTokenDAOTest {
// BOOKENDS
    /**
     * BEFORE
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        // setup database
        database = Database.getInstance();
        database.openConnection();
    }

    /**
     * TEAR DOWN
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        // tear down database
        database.closeConnection(false);

        // set to null
        database = null;
    }


// TESTS
    /**
     * TEST CREATE:
     * Note, that this test assumes no other authentication tokens exist in the database with a
     * token value of "abc-123".
     * @throws Exception
     */
    @Test
    public void testCreate() throws Exception {
        AuthToken at;
        PreparedStatement ps;
        ResultSet rs;

        // create auth token
        at = new AuthToken("abc-123", "ploderup", null);
        AuthTokenDAO.create(at);

        // read from database
        ps = database.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE " +
                "token = ?;");
        ps.setString(1, "abc-123");
        rs = ps.executeQuery();

        // check assertions
        assertTrue(rs.next());
        assertEquals(rs.getString("token"), "abc-123");
        assertEquals(rs.getString("username"), "ploderup");
        assertFalse(rs.next());
    }

    /**
     * TEST READ:
     * Note, that this test assumes no other authentication tokens exist in the database with a
     * token value of "abc-123".
     * @throws Exception
     */
    @Test
    public void testRead() throws Exception {
        PreparedStatement ps;
        AuthToken at;

        // create auth token
        ps = database.getConnection().prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?," +
                "?);");
        ps.setString(1, "abc-123");
        ps.setString(2, "ploderup");
        ps.executeUpdate();

        // read from database
        at = AuthTokenDAO.read("abc-123");

        // check assertions
        assertEquals(at.getToken(), "abc-123");
        assertEquals(at.getUsername(), "ploderup");
        assertEquals(at.getPersonID(), null);
    }


    /**
     * TEST DELETE:
     * Note, that this test assumes no other authentication tokens exist in the database with a
     * token value of "abc-123".
     * @throws Exception
     */
    @Test
    public void testDelete() throws Exception {
        PreparedStatement ps;
        ResultSet rs;

        // create auth token
        ps = database.getConnection().prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?," +
                "?);");
        ps.setString(1, "abc-123");
        ps.setString(2, "ploderup");
        ps.executeUpdate();

        // delete from database
        AuthTokenDAO.delete("abc-123");

        // retrieve from database
        ps = database.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE " +
                "token = ?");
        ps.setString(1, "abc-123");
        rs = ps.executeQuery();

        // check assertions
        assertFalse(rs.next());
    }

    /**
     * TEST DELETE ALL
     * @throws Exception
     */
    @Test
    public void testDeleteAll() throws Exception {
        PreparedStatement ps;
        ResultSet rs;

        // add multiple tokens to db
        ps = database.getConnection().prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?," +
                "?);");
        ps.setString(1, "abc-123");
        ps.setString(2, "ploderup");
        ps.executeUpdate();
        ps = database.getConnection().prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?," +
                "?);");
        ps.setString(1, "xyz-890");
        ps.setString(2, "aloderup");
        ps.executeUpdate();
        ps = database.getConnection().prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?," +
                "?);");
        ps.setString(1, "cat-808");
        ps.setString(2, "cloderup");
        ps.executeUpdate();

        // delete all tokens
        AuthTokenDAO.deleteAll();

        // retrieve from database
        ps = database.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME + ";");
        rs = ps.executeQuery();

        // make assertions
        assertFalse(rs.next());
    }


// MEMBERS
    /**
     * DATABASE
     */
    private Database database;

    /**
     * TABLE NAME
     */
    private final String TABLE_NAME = "AuthTokenTable";
}
