package DataAccess.DataAccessObject;

import org.junit.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import DataAccess.Database;
import Model.Event;

import static org.junit.Assert.*;

/**
 * EVENT DAO TEST:
 * Used for unit testing the EventDAO. Note, that as all DAO methods are static, no DAO object
 * is initialized here. Also, note that while most Data Access Objects have each of the four Create,
 * Read, Update, and Delete (CRUD) methods, none of the DAOs in this project have an update method
 * as the method is unnecessary for the server to function.
 */
public class EventDAOTest {
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
     * Note, that this test assumes no other events exist in the database with an ID of "xyz-890".
     * @throws Exception
     */
    @Test
    public void testCreate() throws Exception {
        Event e;
        PreparedStatement ps;
        ResultSet rs;

        // create event
        e = new Event("xyz-890", "abc-123", "ploderup", 1.5, 2.9, "Norway", "Oslo", "birth", 1976);
        EventDAO.create(e);

        // read from database
        ps = database.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE " +
                "event_id = ?;");
        ps.setString(1, "xyz-890");
        rs = ps.executeQuery();

        // check assertions
        assertTrue(rs.next());
        assertEquals(rs.getString("event_id"), "xyz-890");
        assertEquals(rs.getString("person_id"), "abc-123");
        assertEquals(rs.getString("descendant"), "ploderup");
        assertTrue(rs.getDouble("latitude") == 1.5);
        assertTrue(rs.getDouble("longitude") == 2.9);
        assertEquals(rs.getString("country"), "Norway");
        assertEquals(rs.getString("city"), "Oslo");
        assertEquals(rs.getString("event_type"), "birth");
        assertEquals(rs.getInt("year"), 1976);
        assertFalse(rs.next());
    }

    /**
     * TEST READ:
     * Note, that this test assumes no other events exist in the database with an ID of "xyz-890".
     * @throws Exception
     */
    @Test
    public void testRead() throws Exception {
        PreparedStatement ps;
        Event at;

        // create event
        ps = database.getConnection().prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?," +
                "?);");
        ps.setString(1, "abc-123");
        ps.setString(2, "ploderup");
        ps.executeUpdate();

        // read from database
        at = EventDAO.read("abc-123");

        // check assertions
        assertEquals(at.getToken(), "abc-123");
        assertEquals(at.getUsername(), "ploderup");
        assertEquals(at.getPersonID(), null);
    }


    /**
     * TEST DELETE:
     * Note, that this test assumes no other events exist in the database with an ID of "xyz-890".
     * @throws Exception
     */
    @Test
    public void testDelete() throws Exception {
        PreparedStatement ps;
        ResultSet rs;

        // create event
        ps = database.getConnection().prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?," +
                "?);");
        ps.setString(1, "abc-123");
        ps.setString(2, "ploderup");
        ps.executeUpdate();

        // delete from database
        EventDAO.delete("abc-123");

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
        EventDAO.deleteAll();

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
    private final String TABLE_NAME = "EventTable";
}
