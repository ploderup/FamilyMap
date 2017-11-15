package Facade.Result;
import  java.util.ArrayList;
import  Model.Event;

public class EventResult {
    /**
     * EVENTS CONSTRUCTOR:
     * Constructs a EventResult object.
     *
     * @param da, an ArrayList of Event objects
     */
    public EventResult(ArrayList<Event> da) {
        // init members
        data        = da;
        event_id    = null;
        person_id   = null;
        descendant  = null;
        latitude    = null;
        longitude   = null;
        country     = null;
        city        = null;
        event_type  = null;
        year        = null;
        message     = null;
    }

    /**
     * EVENT CONSTRUCTOR:
     * Constructs a EventResult object.
     *
     * @param e, a Event object
     */
    public EventResult(Event e) {
        // init members
        data        = null;
        event_id    = e.getEventID();
        person_id   = e.getPersonID();
        descendant  = e.getDescendant();
        latitude    = e.getLatitude();
        longitude   = e.getLongitude();
        country     = e.getCountry();
        city        = e.getCity();
        event_type  = e.getEventType();
        year        = e.getYear();
        message     = null;
    }

    /**
     * ERROR CONSTRUCTOR:
     * Constructs an error-containing EventResult object in the case of an error caught in the
     * EventService.
     *
     * @param em, a non-empty string describing the error encountered
     */
    public EventResult(String em) {
        data        = null;
        event_id    = null;
        person_id   = null;
        descendant  = null;
        latitude    = null;
        longitude   = null;
        country     = null;
        city        = null;
        event_type  = null;
        year        = null;
        message     = em;
    }


// Class Members
    /**
     * DATA:
     * An array of events. Null in the case of call to /event/[eventID] API or service failure.
     */
    private ArrayList<Event> data;
    public ArrayList<Event> getData() { return data; }
    public void setData(ArrayList<Event> da) { data = da; }

    /**
     * EVENT ID:
     * A non-empty string, unique event ID. Null in the case of call to /event API or service
     * failure.
     */
    private String event_id;
    public String getEventID() { return event_id; }
    public void setEventID(String ei) { event_id = ei; }
    
    /**
     * PERSON ID:
     * A non-empty string. The person's unique ID Null in case of call to /event API or service
     * failure.
     */
    private String person_id;
    public String getPersonID() { return person_id; }
    public void setPersonID(String id) { person_id = id; }
    
    /**
     * DESCENDANT:
     * A non-empty string. The username of the user this event belongs to. Null in case of call to
     * /event API or service failure.
     */
    private String descendant;
    public String getDescendant() { return descendant; }
    public void setDescendant(String dt) { descendant = dt; }

    /**
     * LATITUDE:
     * A non-empty string, unique event ID. Null in the case of call to /event API or service
     * failure.
     */
    private Double latitude;
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double la) { latitude = la; }

    /**
     * LONGITUDE:
     * Longitude at which the event occurred. Null in the case of call to /event API or service
     * failure.
     */
    private Double longitude;
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double lo) { longitude = lo; }

    /**
     * COUNTRY:
     * A non-empty string, country in which the event occurred. Null in the case of call to /event 
     * API or service failure.
     */
    private String country;
    public String getCountry() { return country; }
    public void setCountry(String co) { country = co; }

    /**
     * CITY:
     * A non-empty string, city in which the event occurred. Null in the case of call to /event API 
     * or service failure.
     */
    private String city;
    public String getCity() { return city; }
    public void setCity(String ci) { city = ci; }

    /**
     * EVENT TYPE:
     * A non-empty string, type of event. Null in the case of call to /event API or service failure.
     */
    private String event_type;
    public String getEventType() { return event_type; }
    public void setEventType(String tp) { event_type = tp; }

    /**
     * YEAR:
     * Year the event occurred. Null in the case of call to /event API or service failure.
     */
    private Integer year;
    public Integer getYear() { return year; }
    public void setYear(Integer yr) { year = yr; }

    /**
     * ERROR MESSAGE:
     * A string detailing the error encountered.
     */
    private String message;
    public String getMessage() { return message; }
    public void setMessage(String msg) { message = msg; }
}
