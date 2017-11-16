package Facade.Services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.PreparedStatement;

import DataAccess.Database;
import Facade.Request.EventRequest;
import Facade.Result.EventResult;
import Facade.Service.EventService;

import static org.junit.Assert.*;

/**
 * WARNING: This class will clear the database.
 */
public class EventServiceTest {
// BOOKENDS
    /**
     * SET UP
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        final String sql0 = "DELETE FROM AuthTokenTable;";
        final String sql1 = "DELETE FROM EventTable;";
        final String sql2 = "INSERT INTO AuthTokenTable VALUES (\"auth_token1\", \"username1\");";
        final String sql3 = "INSERT INTO AuthTokenTable VALUES (\"auth_token2\", \"username2\");";
        final String sql4 = "INSERT INTO EventTable VALUES (\"event_id1\", \"username1\", " +
                "\"person_id\", 101.76, 87.09, \"Norway\", \"Oslo\", \"birth\", 1994);";
        final String sql5 = "INSERT INTO EventTable VALUES (\"event_id2\", \"username1\", " +
                "\"person_id\", 101.76, 87.09, \"Norway\", \"Oslo\", \"birth\", 1994);";
        final String sql6 = "INSERT INTO EventTable VALUES (\"event_id3\", \"username2\", " +
                "\"person_id\", 101.76, 87.09, \"Norway\", \"Oslo\", \"birth\", 1994);";

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
     */
    @After
    public void tearDown() throws Exception {
        Database.getInstance().openConnection();
        Database.getInstance().clear();
        Database.getInstance().closeConnection(true);
    }


// TESTS
    /**
     * TEST GET EVENT:
     * Tests the functionality of the EventService's sole method, getEvent when passed a request for
     * a single event. Note, that this method, like all other service tests, depends on the
     * functionality of individual DAO methods, as well as the functionality of the Database methods
     * as a whole. Considering that the correspondence between service and database methods is
     * nearly one-to-one, only unit tests are performed on the service methods. The DAOs are all
     * tested individually, however, and their tests can be found in their respective folder.
     * @throws Exception
     */
    @Test
    public void testGetEvent() throws Exception {
        EventRequest erqst;
        EventResult erslt;

        // get event from other user
        erqst = new EventRequest("auth_token2", "event_id1");
        erslt = EventService.getInstance().getEvent(erqst);
        assertEquals(erslt.getEventID(), null);
        assertEquals(erslt.getData(), null);
        assertNotEquals(erslt.getMessage(), null);

        // get event from correct user
        erqst = new EventRequest("auth_token1", "event_id1");
        erslt = EventService.getInstance().getEvent(erqst);
        assertEquals(erslt.getEventID(), "event_id1");
        assertEquals(erslt.getData(), null);
        assertEquals(erslt.getMessage(), null);
    }


    /**
     * TEST GET EVENTS:
     * Tests the functionality of the EventService's sole method, getEvent when passed a request for
     * multiple events. See also note on testGetEvent method.
     * @throws Exception
     */
    @Test
    public void testGetEvents() throws Exception {
        EventRequest erqst;
        EventResult erslt;

        // get event from user 1
        erqst = new EventRequest("auth_token1");
        erslt = EventService.getInstance().getEvent(erqst);
        assertEquals(erslt.getEventID(), null);
        assertEquals(erslt.getData().size(), 2);
        assertEquals(erslt.getMessage(), null);

        // get event from correct user
        erqst = new EventRequest("auth_token2");
        erslt = EventService.getInstance().getEvent(erqst);
        assertEquals(erslt.getEventID(), null);
        assertEquals(erslt.getData().size(), 1);
        assertEquals(erslt.getMessage(), null);
    }
}
