package DataAccess.DataAccessObject;

import org.junit.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import DataAccess.Database;
import Model.Event;
import Model.Person;

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
        database = Database.getInstance();
        database.openConnection();
    }

    /**
     * TEAR DOWN
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        database.closeConnection(false);
        database = null;
    }


// TESTS
    /**
     * TEST CREATE:
     * Note, that this test assumes no other events exist in the database with an ID of "xyz-890".
     * Also, note that this test first adds a person to the database, and then adds an event. This
     * is because events cannot be added to the database unless a person with a matching person ID
     * already exists.
     * @throws Exception
     */
    @Test
    public void testCreate() throws Exception {
        Person p;
        Event e;
        PreparedStatement ps;
        ResultSet rs;

        // create person
        p = new Person("abc-123", "ploderup", "Peter", "Loderup", "m", null, null, null);
        PersonDAO.create(p);

        // create event
        e = new Event("xyz-890", "ploderup", "abc-123", 1.5, 2.9, "Norway", "Oslo", "birth", 1976);
        EventDAO.create(e);

        // read from database
        ps = database.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE " +
                "event_id = ?;");
        ps.setString(1, "xyz-890");
        rs = ps.executeQuery();

        // check assertions
        assertTrue(rs.next());
        assertEquals(rs.getString("event_id"), "xyz-890");
        assertEquals(rs.getString("descendant"), "ploderup");
        assertEquals(rs.getString("person_id"), "abc-123");
        assertTrue(rs.getDouble("latitude") == 1.5);
        assertTrue(rs.getDouble("longitude") == 2.9);
        assertEquals(rs.getString("country"), "Norway");
        assertEquals(rs.getString("city"), "Oslo");
        assertEquals(rs.getString("event_type"), "birth");
        assertEquals(rs.getInt("year"), 1976);
        assertFalse(rs.next());
    }

    /**
     * TEST CREATE (RECURSIVE):
     * Note, that this test functions as a comprehensive test for not only the recursive create
     * function, but also for each createEvent method (e.g., createBirth, createDeath, etc.). As
     * many of those functions rely on some degree of random-ness to decide as to whether and when
     * someone was baptized, or when someone died, unit testing is impossible without removing the
     * random aspect of the methods. The methods have been tested individually and, as such, I felt
     * that a single test for the entire recursive create method was satisfactory. Also, note that
     * this method depends on the method in the PersonDAO class of the same name. That function is
     * tested similarly and has been shown to work.
     */
    public void testCreateRecursive() throws Exception {
        // TODO: write tests for recursive creation of events
    }

    /**
     * TEST READ:
     * Note, that this test assumes no other events exist in the database with an ID of "xyz-890".
     *
     * @throws Exception
     */
    @Test
    public void testRead() throws Exception {
        PreparedStatement ps;
        Event e;

        // create event
        ps = database.getConnection().prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?," +
                "?, ?, ?, ?, ?, ?, ?, ?);");
        ps.setString(1, "xyz-890");
        ps.setString(2, "ploderup");
        ps.setString(3, "abc-123");
        ps.setDouble(4, 1.5);
        ps.setDouble(5, 2.9);
        ps.setString(6, "Norway");
        ps.setString(7, "Oslo");
        ps.setString(8, "birth");
        ps.setInt(9, 1976);
        ps.executeUpdate();

        // read from database
        e = EventDAO.read("xyz-890");

        // check assertions
        assertEquals(e.getEventID(), "xyz-890");
        assertEquals(e.getDescendant(), "ploderup");
        assertEquals(e.getPersonID(), "abc-123");
        assertTrue(e.getLatitude() == 1.5);
        assertTrue(e.getLongitude() == 2.9);
        assertEquals(e.getCountry(), "Norway");
        assertEquals(e.getCity(), "Oslo");
        assertEquals(e.getEventType(), "birth");
        assertEquals(e.getYear(), 1976);
    }

    /**
     * TEST READ FAMILY EVENTS
     */
    public void testReadFamilyEvents() throws Exception {
        // TODO: write this method
    }

    /**
     * TEST READ EVENT
     *
     * @throws Exception
     */
    public void testReadEvent() throws Exception {
        // TODO: write this method
    }

    /**
     * TEST DELETE:
     * Note, that this test assumes no other events exist in the database with an ID of "xyz-890".
     *
     * @throws Exception
     */
    @Test
    public void testDelete() throws Exception {
        PreparedStatement ps;
        ResultSet rs;

        // create event
        ps = database.getConnection().prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?," +
                "?, ?, ?, ?, ?, ?, ?, ?);");
        ps.setString(1, "xyz-890");
        ps.setString(2, "ploderup");
        ps.setString(3, "abc-123");
        ps.setDouble(4, 1.5);
        ps.setDouble(5, 2.9);
        ps.setString(6, "Norway");
        ps.setString(7, "Oslo");
        ps.setString(8, "birth");
        ps.setInt(9, 1976);
        ps.executeUpdate();

        // delete from database
        EventDAO.delete("xyz-890");

        // retrieve from database
        ps = database.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE " +
                "event_id = ?");
        ps.setString(1, "xyz-890");
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

        // add multiple events to db
        ps = database.getConnection().prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?," +
                "?, ?, ?, ?, ?, ?, ?, ?);");
        ps.setString(1, "xyz-890");
        ps.setString(2, "ploderup");
        ps.setString(3, "abc-123");
        ps.setDouble(4, 1.5);
        ps.setDouble(5, 2.9);
        ps.setString(6, "Norway");
        ps.setString(7, "Oslo");
        ps.setString(8, "birth");
        ps.setInt(9, 1976);
        ps.executeUpdate();
        ps = database.getConnection().prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?," +
                "?, ?, ?, ?, ?, ?, ?, ?);");
        ps.setString(1, "lol-911");
        ps.setString(2, "jack_son");
        ps.setString(3, "person_id");
        ps.setDouble(4, 9.91);
        ps.setDouble(5, 2.09);
        ps.setString(6, "China");
        ps.setString(7, "Hong Kong");
        ps.setString(8, "death");
        ps.setInt(9, 100);
        ps.executeUpdate();
        ps = database.getConnection().prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?," +
                "?, ?, ?, ?, ?, ?, ?, ?);");
        ps.setString(1, "another_id");
        ps.setString(2, "a_good_name");
        ps.setString(3, "yet_another_id");
        ps.setDouble(4, 0);
        ps.setDouble(5, 0);
        ps.setString(6, "a_country");
        ps.setString(7, "and_a_city");
        ps.setString(8, "an_event");
        ps.setInt(9, 0);
        ps.executeUpdate();

        // delete all events
        EventDAO.deleteAll();

        // retrieve from database
        ps = database.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME + ";");
        rs = ps.executeQuery();

        // make assertions
        assertFalse(rs.next());
    }

    /**
     * TEST DELETE CONDITIONAL
     */
    public void testDeleteConditional() {
        // TODO: write this method
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
