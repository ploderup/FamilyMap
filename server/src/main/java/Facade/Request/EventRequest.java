package Facade.Request;

public class EventRequest {
// Constructors
    /**
     * EVENTS CONSTRUCTOR:
     * Constructs a EventRequest object. When sent to EventService, the presence of an authorization
     * token and lack of an event ID denotes that an array of all Events associated with the user
     * associated with the given token is requested.
     *
     * @param tn, an authorization token string
     */
    public EventRequest(String tn) {
        // init members
        token       = tn;
        event_id    = null;
    }

    /**
     * EVENT CONSTRUCTOR:
     * Constructs a EventRequest object. When sent to EventService, the presence of both an
     * authorization token and an event ID denotes that only a single Event object (the one
     * associated with the given ID) is requested.
     *
     * @param tn, an authorization token string
     * @param id, a non-empty string representing an event's ID
     */
    public EventRequest(String tn, String id) {
        // init members
        token       = tn;
        event_id    = id;
    }


// Class Members
    /**
     * AUTHENTICATION TOKEN:
     * An authentication token object. Note, that auth_token.person_id should be null.
     */
    private String token;
    public String getToken() { return token; }
    public void setToken(String tn) { token = tn; }

    /**
     * EVENT ID:
     * A non-empty (but potentially null) string. See constructor commentary.
     */
    private String event_id;
    public String getEventID() { return event_id; }
    public void setEventID(String id) { event_id = id; }
}
