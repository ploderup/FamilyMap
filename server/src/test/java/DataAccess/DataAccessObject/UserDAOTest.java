package DataAccess.DataAccessObject;

import org.junit.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import DataAccess.Database;
import Model.User;

import static org.junit.Assert.*;

/**
 * USER DAO TEST:
 * Used for unit testing the UserDAO. Note, that as all DAO methods are static, no DAO object
 * is initialized here. Also, note that while most Data Access Objects have each of the four Create,
 * Read, Update, and Delete (CRUD) methods, none of the DAOs in this project have an update method
 * as the method is unnecessary for the server to function.
 */
public class UserDAOTest {
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
     * Note, that this test assumes no other users exist in the database with an ID of "username".
     * Also, note that this test first adds a person to the database, and then adds an user. This
     * is because users cannot be added to the database unless a person with a matching person ID
     * already exists.
     * @throws Exception
     */
    @Test
    public void testCreate() throws Exception {
        PreparedStatement ps;
        ResultSet rs;

        // create user
        UserDAO.create(new User("username", "password", "address@website.com", "John", "Doe", "m"));

        // read from database
        ps = database.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE " +
                "username = ?;");
        ps.setString(1, "username");
        rs = ps.executeQuery();

        // check assertions
        assertTrue(rs.next());
        assertEquals(rs.getString("username"), "username");
        assertEquals(rs.getString("password"), "password");
        assertEquals(rs.getString("email"), "address@website.com");
        assertEquals(rs.getString("first_name"), "John");
        assertEquals(rs.getString("last_name"), "Doe");
        assertEquals(rs.getString("gender"), "m");
        assertNotEquals(rs.getString("person_id"), null);
        assertFalse(rs.next());
    }

    /**
     * TEST READ:
     * Note, that this test assumes no other users exist in the database with an ID of "username".
     *
     * @throws Exception
     */
    @Test
    public void testRead() throws Exception {
        PreparedStatement ps;
        User u;

        // create user
        ps = database.getConnection().prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?," +
                "?, ?, ?, ?, ?, ?);");
        ps.setString(1, "username");
        ps.setString(2, "password");
        ps.setString(3, "address@website.com");
        ps.setString(4, "John");
        ps.setString(5, "Doe");
        ps.setString(6, "m");
        ps.setString(7, "person_id");
        ps.executeUpdate();

        // read from database
        u = UserDAO.read("username");

        // check assertions
        assertEquals(u.getUsername(), "username");
        assertEquals(u.getEmail(), "address@website.com");
        assertEquals(u.getPassword(), "password");
        assertEquals(u.getFirstName(), "John");
        assertEquals(u.getLastName(), "Doe");
        assertEquals(u.getGender(), "m");
        assertNotEquals(u.getPersonID(), null);
    }

    /**
     * TEST DELETE:
     * Note, that this test assumes no other users exist in the database with an ID of "username".
     *
     * @throws Exception
     */
    @Test
    public void testDelete() throws Exception {
        PreparedStatement ps;
        ResultSet rs;

        // create user
        ps = database.getConnection().prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?," +
                "?, ?, ?, ?, ?, ?);");
        ps.setString(1, "username");
        ps.setString(2, "password");
        ps.setString(3, "address@website.com");
        ps.setString(4, "John");
        ps.setString(5, "Doe");
        ps.setString(6, "m");
        ps.setString(7, "person_id");
        ps.executeUpdate();

        // delete from database
        UserDAO.delete("username");

        // retrieve from database
        ps = database.getConnection().prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE " +
                "username = ?");
        ps.setString(1, "username");
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

        // add multiple users to db
        ps = database.getConnection().prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?," +
                "?, ?, ?, ?, ?, ?);");
        ps.setString(1, "username1");
        ps.setString(2, "password");
        ps.setString(3, "address@website.com");
        ps.setString(4, "John");
        ps.setString(5, "Doe");
        ps.setString(6, "m");
        ps.setString(7, "person_id");
        ps.executeUpdate();
        ps = database.getConnection().prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?," +
                "?, ?, ?, ?, ?, ?);");
        ps.setString(1, "username2");
        ps.setString(2, "password");
        ps.setString(3, "address@website.com");
        ps.setString(4, "John");
        ps.setString(5, "Doe");
        ps.setString(6, "m");
        ps.setString(7, "person_id");
        ps.executeUpdate();
        ps = database.getConnection().prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?," +
                "?, ?, ?, ?, ?, ?);");
        ps.setString(1, "username3");
        ps.setString(2, "password");
        ps.setString(3, "address@website.com");
        ps.setString(4, "John");
        ps.setString(5, "Doe");
        ps.setString(6, "m");
        ps.setString(7, "person_id");
        ps.executeUpdate();

        // delete all users
        UserDAO.deleteAll();

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
    private final String TABLE_NAME = "UserTable";
}
