package Facade.Services;

import org.junit.After;
import org.junit.Test;

import DataAccess.Database;
import Facade.Request.LoadRequest;
import Facade.Result.LoadResult;
import Facade.Service.LoadService;
import Server.JSONDecoder;

import static org.junit.Assert.*;

/**
 * WARNING: This class will clear the database.
 */
public class LoadServiceTest {
// BOOKENDS
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
     * TEST LOAD DATABASE:
     * Tests the functionality of the LoadService's sole method, loadDatabase when passed a request
     * containing people, events and users. Note, that this method, like all other service tests,
     * depends on the functionality of individual DAO methods, as well as the functionality of the
     * Database methods as a whole. Considering that the correspondence between service and database
     * methods is nearly one-to-one, only unit tests are performed on the service methods. The DAOs
     * are all tested individually, however, and their tests can be found in their respective
     * folder. WARNING: This method changes the values in the database.
     * @throws Exception
     */
    @Test
    public void testLoadDatabase() throws Exception {
        final String json =
                "{\n" +
                "  \"users\": [\n" +
                "    {\n" +
                "      \"userName\": \"sheila\",\n" +
                "      \"password\": \"parker\",\n" +
                "      \"email\": \"sheila@parker.com\",\n" +
                "      \"firstName\": \"Sheila\",\n" +
                "      \"lastName\": \"Parker\",\n" +
                "      \"gender\": \"f\",\n" +
                "      \"personID\": \"Sheila_Parker\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"persons\": [\n" +
                "    {\n" +
                "      \"firstName\": \"Sheila\",\n" +
                "      \"lastName\": \"Parker\",\n" +
                "      \"gender\": \"f\",\n" +
                "      \"personID\": \"Sheila_Parker\",\n" +
                "      \"father\": \"Patrick_Spencer\",\n" +
                "      \"mother\": \"Im_really_good_at_names\",\n" +
                "      \"descendant\": \"sheila\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"firstName\": \"Patrick\",\n" +
                "      \"lastName\": \"Spencer\",\n" +
                "      \"gender\": \"m\",\n" +
                "      \"personID\":\"Patrick_Spencer\",\n" +
                "      \"spouse\": \"Im_really_good_at_names\",\n" +
                "      \"descendant\": \"sheila\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"firstName\": \"CS240\",\n" +
                "      \"lastName\": \"JavaRocks\",\n" +
                "      \"gender\": \"f\",\n" +
                "      \"personID\": \"Im_really_good_at_names\",\n" +
                "      \"spouse\": \"Patrick_Spencer\",\n" +
                "      \"descendant\": \"sheila\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"events\": [\n" +
                "    {\n" +
                "      \"eventType\": \"started family map\",\n" +
                "      \"personID\": \"Sheila_Parker\",\n" +
                "      \"city\": \"Salt Lake City\",\n" +
                "      \"country\": \"United States\",\n" +
                "      \"latitude\": 40.7500,\n" +
                "      \"longitude\": -110.1167,\n" +
                "      \"year\": 2016,\n" +
                "      \"eventID\": \"Sheila_Family_Map\",\n" +
                "      \"descendant\":\"sheila\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"eventType\": \"fixed this thing\",\n" +
                "      \"personID\": \"Patrick_Spencer\",\n" +
                "      \"city\": \"Provo\",\n" +
                "      \"country\": \"United States\",\n" +
                "      \"latitude\": 40.2338,\n" +
                "      \"longitude\": -111.6585,\n" +
                "      \"year\": 2017,\n" +
                "      \"eventID\": \"I_hate_formatting\",\n" +
                "      \"descendant\": \"sheila\"\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";

        LoadRequest load_request;
        LoadResult load_result;

        // perform load
        load_request = (LoadRequest)JSONDecoder.decodeRequest(json, LoadRequest.class);
        load_result = LoadService.getInstance().loadDatabase(load_request);

        // check assertions
        assertEquals(load_result.getMessage(), "Successfully added 1 users, 3 persons, and 2 " +
                "events to the database.");
    }
}
