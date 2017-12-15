package DataAccess.DataAccessObject;

import org.junit.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import DataAccess.Database;
import Model.Event;
import Model.Person;
import Model.User;

import static javax.swing.text.html.HTML.Tag.UL;
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
        PreparedStatement ps;
        ResultSet rs;

        // create user and person
        UserDAO.create(new User("johndoe", "password", "jd@gmail.com", "John", "Doe", "m"));
        PersonDAO.create(new Person("abc-123", "johndoe", "John", "Doe", "m", null, null, null));

        // create event
        EventDAO.create(new Event("xyz-890", "johndoe", "abc-123", 1.5, 2.9, "Norway", "Oslo", "birth", 1976));

        // read from database
        ps = database.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE " +
                "event_id = ?;");
        ps.setString(1, "xyz-890");
        rs = ps.executeQuery();

        // check assertions
        assertTrue(rs.next());
        assertEquals(rs.getString("event_id"), "xyz-890");
        assertEquals(rs.getString("descendant"), "johndoe");
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
     * function, but also for each createEvent method (e.g., createBirth, createDeath, etc.). Also,
     * note that this method depends on having a family tree already established;
     * PersonDAO.createRecursive is used here to accomplish that. That method has been tested and
     * shown to function correctly.
     */
    @Test
    public void testCreateRecursive() throws Exception {
        final int NUM_GENERATIONS = 4;
        final double MIN_NUM_EVENTS = 92;

        String un;
        Person p;
        String pi;
        PreparedStatement ps;
        ResultSet rs;
        int i;

        // create tree
        un = "username";
        UserDAO.create(new User(un, "password", "email@mail.net", "John", "Smith", "m"));
        p = new Person(un, "John", "Smith", "m");
        pi = p.getPersonID();
        PersonDAO.createRecursive(un, p, NUM_GENERATIONS);

        // create events
        EventDAO.createRecursive(un, pi, 1800);

        // retrieve all events
        ps = database.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE " +
                "descendant = ?;");
        ps.setString(1, un);
        rs = ps.executeQuery();
        i = 0;
        while(rs.next()) i++;

        // check assertions
        assertTrue(i >= MIN_NUM_EVENTS);
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
        ps.setString(2, "johndoe");
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
        assertEquals(e.getDescendant(), "johndoe");
        assertEquals(e.getPersonID(), "abc-123");
        assertTrue(e.getLatitude() == 1.5);
        assertTrue(e.getLongitude() == 2.9);
        assertEquals(e.getCountry(), "Norway");
        assertEquals(e.getCity(), "Oslo");
        assertEquals(e.getEventType(), "birth");
        assertEquals(e.getYear(), Integer.valueOf(1976));
    }

    /**
     * TEST READ FAMILY EVENTS
     *
     * @throws Exception
     */
    @Test
    public void testReadFamilyEvents() throws Exception {
        PreparedStatement ps;
        ArrayList<Event> es;

        // create event
        ps = database.getConnection().prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?," +
                "?, ?, ?, ?, ?, ?, ?, ?);");
        ps.setString(1, "xyz-890");
        ps.setString(2, "johndoe");
        ps.setString(3, "abc-123");
        ps.setDouble(4, 1.5);
        ps.setDouble(5, 2.9);
        ps.setString(6, "Norway");
        ps.setString(7, "Oslo");
        ps.setString(8, "birth");
        ps.setInt(9, 1976);
        ps.executeUpdate();

        // create event for another user
        ps = database.getConnection().prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?," +
                "?, ?, ?, ?, ?, ?, ?, ?);");
        ps.setString(1, "abc-123");
        ps.setString(2, "aloderup");
        ps.setString(3, "xyz-890");
        ps.setDouble(4, 1.5);
        ps.setDouble(5, 2.9);
        ps.setString(6, "Norway");
        ps.setString(7, "Oslo");
        ps.setString(8, "birth");
        ps.setInt(9, 1976);
        ps.executeUpdate();

        // read from database
        es = EventDAO.readFamilyEvents("johndoe");

        // check assertions
        assertEquals(es.size(), 1);
        assertEquals(es.get(0).getEventID(), "xyz-890");
        assertEquals(es.get(0).getDescendant(), "johndoe");
        assertEquals(es.get(0).getPersonID(), "abc-123");
        assertTrue(es.get(0).getLatitude() == 1.5);
        assertTrue(es.get(0).getLongitude() == 2.9);
        assertEquals(es.get(0).getCountry(), "Norway");
        assertEquals(es.get(0).getCity(), "Oslo");
        assertEquals(es.get(0).getEventType(), "birth");
        assertEquals(es.get(0).getYear(), Integer.valueOf(1976));
    }

    /**
     * TEST READ EVENT
     *
     * @throws Exception
     */
    public void testReadEvent() throws Exception {
        PreparedStatement ps;
        Event e;

        // create event
        ps = database.getConnection().prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?," +
                "?, ?, ?, ?, ?, ?, ?, ?);");
        ps.setString(1, "xyz-890");
        ps.setString(2, "johndoe");
        ps.setString(3, "abc-123");
        ps.setDouble(4, 1.5);
        ps.setDouble(5, 2.9);
        ps.setString(6, "Norway");
        ps.setString(7, "Oslo");
        ps.setString(8, "birth");
        ps.setInt(9, 1976);
        ps.executeUpdate();

        // read from database
        e = EventDAO.readEvent("bad_username", "xyz-890");
        assertEquals(e, null);

        // read from database
        e = EventDAO.readEvent("johndoe", "bad_event_id");
        assertEquals(e, null);

        // read from database
        e = EventDAO.readEvent("johndoe", "xyz-890");

        // check assertions
        assertEquals(e.getEventID(), "xyz-890");
        assertEquals(e.getDescendant(), "johndoe");
        assertEquals(e.getPersonID(), "abc-123");
        assertTrue(e.getLatitude() == 1.5);
        assertTrue(e.getLongitude() == 2.9);
        assertEquals(e.getCountry(), "Norway");
        assertEquals(e.getCity(), "Oslo");
        assertEquals(e.getEventType(), "birth");
        assertEquals(e.getYear(), Integer.valueOf(1976));
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
        ps.setString(2, "johndoe");
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
        ps.setString(2, "johndoe");
        ps.setString(3, "person_id      ");
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
        ps.setString(2, "wongjackman");
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
        ps.setString(1, "bnd-007");
        ps.setString(2, "jamesbond");
        ps.setString(3, "person_id");
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
    @Test
    public void testDeleteConditional() throws Exception{
        PreparedStatement ps;
        ResultSet rs;

        // create event
        ps = database.getConnection().prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?," +
                "?, ?, ?, ?, ?, ?, ?, ?);");
        ps.setString(1, "xyz-890");
        ps.setString(2, "johndoe");
        ps.setString(3, "abc-123");
        ps.setDouble(4, 1.5);
        ps.setDouble(5, 2.9);
        ps.setString(6, "Norway");
        ps.setString(7, "Oslo");
        ps.setString(8, "birth");
        ps.setInt(9, 1976);
        ps.executeUpdate();

        // delete from database
        EventDAO.deleteConditional("person_id", "xyz-890");

        // retrieve from database
        ps = database.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE " +
                "event_id = ?");
        ps.setString(1, "xyz-890");
        rs = ps.executeQuery();

        // check assertions
        assertTrue(rs.next());

        // delete from database
        EventDAO.deleteConditional("event_id", "xyz-890");

        // retrieve from database
        ps = database.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE " +
                "event_id = ?");
        ps.setString(1, "xyz-890");
        rs = ps.executeQuery();

        // check assertions
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
