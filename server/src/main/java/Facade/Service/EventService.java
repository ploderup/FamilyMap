package Facade.Service;
import java.util.ArrayList;
import DataAccess.Database;
import Facade.Result.PersonResult;
import Model.Event;
import Facade.Request.EventRequest;
import Facade.Result.EventResult;

public class EventService {
// CONSTRUCTORS
    /**
     * PRIVATE CONSTRUCTOR:
     * Constructs a EventService object.
     */
    private EventService() {}


// METHODS
    /**
     * GET EVENT(S):
     * Handles a variety of EventRequest objects. In the case that the request includes just an
     * authorization token, the result will include an ArrayList of Event objects; on the other
     * hand, if the request includes both a token and an event ID, then the result will contain
     * only a single Event object (see "EventRequest.java").
     *
     * @param event_request, an EventRequest object
     * @return an EventResult object
     * @see EventRequest
     */
    public EventResult getEvent(EventRequest event_request) {
        // check input
        if(event_request == null)
            return new EventResult("Event retrieval failed. Null pointer passed to " +
                                   "EventService.getEvent.");

        // declarations
        EventResult event_result;
        Database database = null;

        try {
            // declarations
            String username;

            // get request data
            String token    = event_request.getToken();
            String event_id = event_request.getEventID();

            // open connection
            database = Database.getInstance();
            database.openConnection();

            // verify auth token
            username = database.verifyAuthToken(token);
            if(username == null)
                return new EventResult("Event retrieval failed. Bad authentication token " +
                        "provided.");

            // get one event
            if(event_id != null) {
                // get event
                Event event = database.getEvent(username, event_id);

                // construct result
                event_result = new EventResult(event);

            // get many persons
            } else {
                // get family
                ArrayList<Event> events = database.getFamilyEvents(username);

                // construct result
                event_result = new EventResult(events);
            }
            
            // commit changes
            database.closeConnection(true);

        } catch(Exception e) {
            // rollback changes
            if(database != null) if(database.getConnection() != null)
                database.closeConnection(false);
            
            // construct error result
            event_result = new EventResult(e.getMessage());
        }
        
        return event_result;
    }


// MEMBERS
    /**
     * INSTANCE:
     * A singleton EventService object.
     */
    private static EventService instance;
    public static EventService getInstance() {
        if(instance == null) {
            instance = new EventService();
        }

        return instance;
    }
}
