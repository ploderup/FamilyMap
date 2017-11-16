package DataAccess.DataAccessObject;

import org.junit.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import DataAccess.Database;
import Model.Person;
import Model.Person;
import Model.User;

import static org.junit.Assert.*;

/**
 * PERSON DAO TEST:
 * Used for unit testing the PersonDAO. Note, that as all DAO methods are static, no DAO object
 * is initialized here. Also, note that while most Data Access Objects have each of the four Create,
 * Read, Update, and Delete (CRUD) methods, none of the DAOs in this project have an update method
 * as the method is unnecessary for the server to function.
 */
public class PersonDAOTest {
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
     * Note, that this test assumes no other persons exist in the database with an ID of "person_id".
     * Also, note that this test first adds a person to the database, and then adds an person. This
     * is because persons cannot be added to the database unless a person with a matching person ID
     * already exists.
     * @throws Exception
     */
    @Test
    public void testCreate() throws Exception {
        PreparedStatement ps;
        ResultSet rs;

        // create user
        UserDAO.create(new User("username", "password", "address@website.com", "John", "Doe", "m"));

        // create person
        PersonDAO.create(new Person("person_id", "username", "John", "Doe", "m", null, null, null));

        // read from database
        ps = database.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE " +
                "person_id = ?;");
        ps.setString(1, "person_id");
        rs = ps.executeQuery();

        // check assertions
        assertTrue(rs.next());
        assertEquals(rs.getString("person_id"), "person_id");
        assertEquals(rs.getString("descendant"), "username");
        assertEquals(rs.getString("first_name"), "John");
        assertEquals(rs.getString("last_name"), "Doe");
        assertEquals(rs.getString("gender"), "m");
        assertEquals(rs.getString("father_id"), null);
        assertEquals(rs.getString("mother_id"), null);
        assertEquals(rs.getString("spouse_id"), null);
        assertFalse(rs.next());
    }

    /**
     * TEST CREATE (RECURSIVE):
     * Note, that this test functions as a comprehensive test for not only the recursive create
     * function, but also for each createPerson method (e.g., createBirth, createDeath, etc.). Also,
     * note that this method depends on having a family tree already established;
     * PersonDAO.createRecursive is used here to accomplish that. That method has been tested and
     * shown to function correctly.
     */
    @Test
    public void testCreateRecursive() throws Exception {
        final int NUM_GENERATIONS = 4;
        final double NUM_PERSONS = 31;

        PreparedStatement ps;
        ResultSet rs;
        int i;

        // create user and person
        UserDAO.create(new User("username", "password", "address@website.com", "John", "Doe", "m"));

        // create people
        PersonDAO.createRecursive("username", new Person("person_id", "username", "John", "Doe",
                "m", null, null, null), NUM_GENERATIONS);

        // retrieve all persons
        ps = database.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE " +
                "descendant = ?;");
        ps.setString(1, "username");
        rs = ps.executeQuery();
        i = 0;
        while(rs.next()) i++;

        // check assertions
        assertTrue(i == NUM_PERSONS);
    }

    /**
     * TEST READ:
     * Note, that this test assumes no other persons exist in the database with an ID of "person_id".
     *
     * @throws Exception
     */
    @Test
    public void testRead() throws Exception {
        PreparedStatement ps;
        Person p;

        // create person
        ps = database.getConnection().prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?," +
                "?, ?, ?, ?, ?, ?, ?);");
        ps.setString(1, "person_id");
        ps.setString(2, "username");
        ps.setString(3, "John");
        ps.setString(4, "Doe");
        ps.setString(5, "m");
        ps.setString(6, null);
        ps.setString(7, null);
        ps.setString(8, null);
        ps.executeUpdate();

        // read from database
        p = PersonDAO.read("person_id");

        // check assertions
        assertEquals(p.getPersonID(), "person_id");
        assertEquals(p.getDescendant(), "username");
        assertEquals(p.getFirstName(), "John");
        assertEquals(p.getLastName(), "Doe");
        assertEquals(p.getGender(), "m");
        assertEquals(p.getFatherID(), null);
        assertEquals(p.getMotherID(), null);
        assertEquals(p.getSpouseID(), null);
    }

    /**
     * TEST READ FAMILY PERSONS
     *
     * @throws Exception
     */
    @Test
    public void testReadFamily() throws Exception {
        PreparedStatement ps;
        ArrayList<Person> f;

        // create person
        ps = database.getConnection().prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?," +
                "?, ?, ?, ?, ?, ?, ?);");
        ps.setString(1, "person_id1");
        ps.setString(2, "username1");
        ps.setString(3, "John");
        ps.setString(4, "Doe");
        ps.setString(5, "m");
        ps.setString(6, null);
        ps.setString(7, null);
        ps.setString(8, null);
        ps.executeUpdate();

        // create person for another user
        ps = database.getConnection().prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?," +
                "?, ?, ?, ?, ?, ?, ?);");
        ps.setString(1, "person_id2");
        ps.setString(2, "username2");
        ps.setString(3, "John");
        ps.setString(4, "Doe");
        ps.setString(5, "m");
        ps.setString(6, null);
        ps.setString(7, null);
        ps.setString(8, null);
        ps.executeUpdate();

        // read from database
        f = PersonDAO.readFamily("username1");

        // check assertions
        assertEquals(f.size(), 1);
        assertEquals(f.get(0).getPersonID(), "person_id1");
        assertEquals(f.get(0).getDescendant(), "username1");
        assertEquals(f.get(0).getFirstName(), "John");
        assertEquals(f.get(0).getLastName(), "Doe");
        assertEquals(f.get(0).getGender(), "m");
        assertEquals(f.get(0).getFatherID(), null);
        assertEquals(f.get(0).getMotherID(), null);
        assertEquals(f.get(0).getSpouseID(), null);
    }

    /**
     * TEST READ PERSON
     *
     * @throws Exception
     */
    public void testReadPerson() throws Exception {
        PreparedStatement ps;
        Person p;

        // create person
        ps = database.getConnection().prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?," +
                "?, ?, ?, ?, ?, ?, ?);");
        ps.setString(1, "person_id");
        ps.setString(2, "username");
        ps.setString(3, "John");
        ps.setString(4, "Doe");
        ps.setString(5, "m");
        ps.setString(6, null);
        ps.setString(7, null);
        ps.setString(8, null);
        ps.executeUpdate();

        // read from database
        p = PersonDAO.readPerson("bad_username", "person_id");
        assertEquals(p, null);

        // read from database
        p = PersonDAO.readPerson("username", "bad_person_id");
        assertEquals(p, null);

        // read from database
        p = PersonDAO.readPerson("username", "person_id");

        // check assertions
        assertEquals(p.getPersonID(), "person_id");
        assertEquals(p.getDescendant(), "username");
        assertEquals(p.getFirstName(), "John");
        assertEquals(p.getLastName(), "Doe");
        assertEquals(p.getGender(), "m");
        assertEquals(p.getFatherID(), null);
        assertEquals(p.getMotherID(), null);
        assertEquals(p.getSpouseID(), null);
    }

    /**
     * TEST DELETE:
     * Note, that this test assumes no other persons exist in the database with an ID of "person_id".
     *
     * @throws Exception
     */
    @Test
    public void testDelete() throws Exception {
        PreparedStatement ps;
        ResultSet rs;

        // create person
        ps = database.getConnection().prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?," +
                "?, ?, ?, ?, ?, ?, ?);");
        ps.setString(1, "person_id");
        ps.setString(2, "username");
        ps.setString(3, "John");
        ps.setString(4, "Doe");
        ps.setString(5, "m");
        ps.setString(6, null);
        ps.setString(7, null);
        ps.setString(8, null);
        ps.executeUpdate();

        // delete from database
        PersonDAO.delete("person_id");

        // retrieve from database
        ps = database.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE " +
                "person_id = ?");
        ps.setString(1, "person_id");
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

        // add multiple persons to db
        ps = database.getConnection().prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?," +
                "?, ?, ?, ?, ?, ?, ?);");
        ps.setString(1, "person_id1");
        ps.setString(2, "username1");
        ps.setString(3, "John");
        ps.setString(4, "Doe");
        ps.setString(5, "m");
        ps.setString(6, null);
        ps.setString(7, null);
        ps.setString(8, null);
        ps.executeUpdate();
        ps = database.getConnection().prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?," +
                "?, ?, ?, ?, ?, ?, ?);");
        ps.setString(1, "person_id2");
        ps.setString(2, "username2");
        ps.setString(3, "John");
        ps.setString(4, "Doe");
        ps.setString(5, "m");
        ps.setString(6, null);
        ps.setString(7, null);
        ps.setString(8, null);
        ps.executeUpdate();
        ps = database.getConnection().prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?," +
                "?, ?, ?, ?, ?, ?, ?);");
        ps.setString(1, "person_id3");
        ps.setString(2, "username3");
        ps.setString(3, "John");
        ps.setString(4, "Doe");
        ps.setString(5, "m");
        ps.setString(6, null);
        ps.setString(7, null);
        ps.setString(8, null);
        ps.executeUpdate();

        // delete all persons
        PersonDAO.deleteAll();

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

        // create person
        ps = database.getConnection().prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?," +
                "?, ?, ?, ?, ?, ?, ?);");
        ps.setString(1, "person_id");
        ps.setString(2, "username");
        ps.setString(3, "John");
        ps.setString(4, "Doe");
        ps.setString(5, "m");
        ps.setString(6, null);
        ps.setString(7, null);
        ps.setString(8, null);
        ps.executeUpdate();

        // delete from database
        PersonDAO.deleteConditional("person_id", "bad_person_id");

        // retrieve from database
        ps = database.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE " +
                "person_id = ?");
        ps.setString(1, "person_id");
        rs = ps.executeQuery();

        // check assertions
        assertTrue(rs.next());

        // delete from database
        PersonDAO.deleteConditional("person_id", "person_id");

        // retrieve from database
        ps = database.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE " +
                "person_id = ?");
        ps.setString(1, "person_id");
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
    private final String TABLE_NAME = "PersonTable";
}
