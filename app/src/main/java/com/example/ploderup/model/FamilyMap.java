package com.example.ploderup.model;

import android.util.Log;

import com.example.ploderup.userinterface.BuildConfig;

import java.util.ArrayList;
import java.util.Iterator;

import Model.Event;
import Model.Person;

public class FamilyMap {
// SINGLETON STUFF
    private static FamilyMap mInstance = new FamilyMap();
    public static FamilyMap getInstance() { return mInstance; }
    private FamilyMap() {}

// MEMBERS
    private Filter mFilter = Filter.getInstance();
    private final String TAG = "FamilyMap";

    /**
     * The ID of the person associated with the currently logged in user. This member is reloaded
     * every time the app syncs its data with the FamilyMap server. It is set to null on logout.
     */
    private String mRootPersonID;
    public String getRootPersonID() { return mRootPersonID; }
    public void setRootPersonID(String mRootPersonID) {
        this.mRootPersonID = mRootPersonID;
        Log.i(TAG, "mRootPersonID set to " + mRootPersonID);
    }

    /**
     * All of the people in the logged in user's family tree. This member is reloaded every time
     * the app syncs its data with the FamilyMap server. It is set to null on logout.
     */
    private ArrayList<Person> mAllPeople;
    public ArrayList<Person> getAllPeople() { return mAllPeople; }
    public void setAllPeople(ArrayList<Person> mAllPeople) {
        this.mAllPeople = mAllPeople;
        Log.i(TAG, "mAllPeople modified");
    }

    /**
     * All of the events connected to people in the logged in user's family tree. This member is
     * reloaded every time the app syncs its data with the FamilyMap server. It is set to null on
     * logout.
     */
    private ArrayList<Event> mAllEvents;
    public ArrayList<Event> getAllEvents() { return mAllEvents; }
    public void setAllEvents(ArrayList<Event> mAllEvents) {
        this.mAllEvents = mAllEvents;
        Log.i(TAG, "mAllEvents modified");
    }

    /**
     * A flag indicating whether the DataSyncTask has completed, or is currently running.
     */
    private boolean mDataSyncDone;
    public boolean getDataSyncDone() { return mDataSyncDone; }
    public void setDataSyncDone(boolean mDataSyncDone) { this.mDataSyncDone = mDataSyncDone; }

    /**
     * Checks whether or not a given event should be filtered from the MapFragment, based on filters
     * applied by the user.
     * @param event a not-null event
     * @return whether the event should be filtered
     * @see Filter
     */
    public boolean isFiltered(Event event) {
        if (findPersonByID(event.getPersonID()) == null) {
            Log.e(TAG, "Event passed to isFiltered is not associated with any person in mAllPeople");
            return false;
        }

        // Is the event's type filtered?
        if (isFilteredByType(event.getEventType())) return true;

        // Is the gender of the person associated with the event filtered?
        if (isFilteredByGender(findPersonByID(event.getPersonID()).getGender())) return true;

        // Is the event associated with a side of the tree currently filtered?
        if (mFilter.getFilterFathersSide())
            if (isFilteredBySide(event.getPersonID(),
                    findPersonByID(mRootPersonID).getFatherID()))
                return true;
        if (mFilter.getFilterMothersSide())
            if (isFilteredBySide(event.getPersonID(),
                    findPersonByID(mRootPersonID).getMotherID()))
                return true;

        // No filters apply to the event given
        return false;
    }

    /**
     * Checks whether or not a given event should be filtered from the MapFragment, based on event-
     * type filters applied by the user.
     * @param event_type a not-null string
     * @return whether the event should be filtered
     */
    private boolean isFilteredByType(String event_type) {
        if(event_type == null) {
            Log.e(TAG, "Null pointer passed to isFilteredByType");
        }

        // What is the type of the event?
        switch(event_type) {
            case "baptism": return mFilter.getFilterBaptismEvents();
            case "birth": return mFilter.getFilterBirthEvents();
            case "census": return mFilter.getFilterCensusEvents();
            case "christening": return mFilter.getFilterChristeningEvents();
            case "death": return mFilter.getFilterDeathEvents();
            case "marriage": return mFilter.getFilterMarriageEvents();
            default: return false;
        }
    }


    /**
     * Checks whether or not a given event should be filtered from the MapFragment, based on gender
     * filters applied by the user.
     * @param gender a not-null string
     * @return whether the event should be filtered
     */
    private boolean isFilteredByGender(String gender) {
        if (gender == null) {
            Log.i(TAG, "Null pointer passed to isFilteredByGender");
        }

        // What is the gender of the person associated with the event?
        switch(gender) {
            case "m": return mFilter.getFilterMaleEvents();
            case "f": return mFilter.getFilterFemaleEvents();
            default: return false;
        }
    }

    /**
     * Checks whether or not a given event should be filtered from the MapFragment, based on gender
     * filters applied by the user.
     * @param person_id a non-empty string
     * @param parent_id a non-empty string
     * @return whether the event should be filtered
     */
    private boolean isFilteredBySide(String person_id, String parent_id) {
        if (person_id == null) {
            Log.e(TAG, "Null pointer passed to filterEventsByFamilySide");
            return false;
        }
        if (person_id.equals("")) {
            Log.e(TAG, "Empty string passed to filterEventsByFamilySide");
            return false;
        }

        // Does this person's ID match the ID provided?
        if (person_id.equals(parent_id)) return true;

        // Does this person has a father in the family tree?
        if (findPersonByID(parent_id).getFatherID() != null)
            // Does the given ID exist on the father's side of the tree?
            if (isFilteredBySide(person_id, findPersonByID(parent_id).getFatherID())) return true;

        // Does this person has a mother in the family tree?
        if (findPersonByID(parent_id).getMotherID() != null)
            // Does the given ID exist on the mother's side of the tree?
            if (isFilteredBySide(person_id, findPersonByID(parent_id).getMotherID())) return true;

        return false;
    }

    /**
     * Searches for a person with a given ID in mAllPeople.
     * @param id a non-empty string
     * @return a person object if found, null otherwise (including if multiples are found)
     * @throws Exception when given invalid input
     */
    public Person findPersonByID(String id) {
        Person result = null;

        // Iterate through the array-list of people
        for(Person person : mAllPeople) {
            // Was a person with the ID found?
            if (person.getPersonID().equals(id))
                if(result == null) result = person; else return null;
        }

        // No one was found with the given ID
        return result;
    }

    /**
     * Retrieves the earliest event associated with the given person, where events are ordered
     * chronologically, as follows: Birth events, events with years (ordered by event-type,
     * alphabetically), events without years (ordered by event-type, alphabetically), death events.
     */
    public Event getPersonsEarliestEvent(String person_id) {
        if (person_id == null) {
            Log.e(TAG, "Null pointer passed to getPersonsEarliestEvent");
            return null;
        }
        if (person_id.equals("")) {
            Log.e(TAG, "Empty string passed to getPersonsEarliestEvent");
            return null;
        }

        ArrayList<Event> events = new ArrayList<>();
        Event earliest_event = null;

        // Get all events associated with the person associated with the given ID
        for (Event event : mAllEvents) {
            if (event.getPersonID().equals(person_id)) events.add(event);
        }

        // How many events were found?
        switch(events.size()) {
            case 0: return null;
            case 1: return events.get(0);
            default: // Multiple events were found
        }

        // Does this person have a birth event?
        for (Event event : events) {
            if (event.getEventType().equalsIgnoreCase("birth")) return event;
        }

        // Do any of the events have years associated with them?
        for (Event event : events) {
            // Does the event have a valid year?
            if (event.getYear() > 0) {
                // Does 'earliest event' need to be initialized?
                if (earliest_event == null) {
                    if (!event.getEventType().equalsIgnoreCase("death")) earliest_event = event;

                // It's already initialized; is its year later than 'event's?
                } else if (event.getYear() < earliest_event.getYear()) {
                    // Then set it as the new 'earliest event
                    if (!event.getEventType().equalsIgnoreCase("death")) earliest_event = event;

                // Is it's year equal to 'event's?
                } else if (event.getYear() == earliest_event.getYear()) {
                    // Alphabetically, does 'event's type precede 'earliest event's type?
                    if (event.getEventType().compareToIgnoreCase(earliest_event.getEventType()) < 0)
                        if (!event.getEventType().equalsIgnoreCase("death")) earliest_event = event;
                }
            }
        }

        // Was an event found?
        if (earliest_event != null) return earliest_event;

        // Are there any not-death events without years?
        for (Event event : events) {
            // Does 'earliest event' need to be initialized?
            if (earliest_event == null) {
                if (!event.getEventType().equalsIgnoreCase("death")) earliest_event = event;

            // It's already initialized; is 'event's type lexicographically less than 'earliest's?
            } else {
                if (event.getEventType().compareToIgnoreCase(earliest_event.getEventType()) < 0)
                    if (!event.getEventType().equalsIgnoreCase("death")) earliest_event = event;
            }
        }

        // Was an event found?
        if (earliest_event != null) return earliest_event;

        // At this point, if there was no birth event, nor any not-death events there could only
        // have been one event: death. A single death event would have been caught by the switch
        // performed earlier. So, this point in code should never be reached.
        Log.e(TAG, "Event checks failed");
        return null;
    }
}
