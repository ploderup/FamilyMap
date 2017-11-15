package DataAccess;
import org.junit.*;

public class DatabaseTest {
// Before & After
    @Before
    public void setUp() throws DatabaseException {
        // get instance of db
        database = Database.getInstance();

        // open connection
        database.openConnection();
    }

    @After
    public void tearDown() throws DatabaseException {
        // close connection; !commit
        database.closeConnection(false);

        // set to null
        database = null;
    }


// Tests
    @Test
    public void testClearDatabase() throws DatabaseException {
    
    }


// Private Members
    /**
     * DATABASE:
     * A Database object for testing.
     */
    private Database database;
}
