package Facade.Services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import DataAccess.Database;
import Facade.Request.EventRequest;
import Facade.Result.EventResult;
import Facade.Service.EventService;

import static org.junit.Assert.*;

public class EventServiceTest {
// BOOKENDS
    /**
     * SET UP
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        final String sql1 = "INSERT INTO EventTable VALUES (\"event_id1\", \"username1\", " +
                "\"person_id\", 101.76, 87.09, \"Norway\", \"Oslo\", \"birth\", 1994);";
        final String sql2 = "INSERT INTO EventTable VALUES (\"event_id2\", \"username1\", " +
                "\"person_id\", 101.76, 87.09, \"Norway\", \"Oslo\", \"birth\", 1994);";
        final String sql3 = "INSERT INTO EventTable VALUES (\"event_id3\", \"username2\", " +
                "\"person_id\", 101.76, 87.09, \"Norway\", \"Oslo\", \"birth\", 1994);";

        PreparedStatement stmt;

        // add to database
        Database.getInstance().openConnection();
        stmt = Database.getInstance().getConnection().prepareStatement(sql1);
        stmt.executeUpdate();
        stmt = Database.getInstance().getConnection().prepareStatement(sql2);
        stmt.executeUpdate();
        stmt = Database.getInstance().getConnection().prepareStatement(sql3);
        stmt.executeUpdate();
    }

    /**
     * TEAR DOWN
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {

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
        erqst = new EventRequest("username2", "event_id1");
        erslt = EventService.getInstance().getEvent(erqst);
        System.out.println("lol: " + erslt.getMessage());
        assertEquals(erslt.getEventID(), null);
        assertEquals(erslt.getData(), null);
        assertNotEquals(erslt.getMessage(), null);

        // get event from correct user
        erqst = new EventRequest("username1", "event_id1");
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
//    @Test
    public void testGetEvents() throws Exception {
        EventRequest erqst;
        EventResult erslt;

        // construct result
        erqst = new EventRequest("username");

    }
}
