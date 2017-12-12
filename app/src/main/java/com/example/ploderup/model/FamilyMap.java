package com.example.ploderup.model;

import android.util.Log;

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
    public void setRootPersonID(String mRootPersonID) { this.mRootPersonID = mRootPersonID; }

    /**
     * All of the people in the logged in user's family tree. This member is reloaded every time
     * the app syncs its data with the FamilyMap server. It is set to null on logout.
     */
    private ArrayList<Person> mAllPeople;
    public ArrayList<Person> getAllPeople() { return mAllPeople; }
    public void setAllPeople(ArrayList<Person> mAllPeople) { this.mAllPeople = mAllPeople; }

    /**
     * All of the events connected to people in the logged in user's family tree. This member is
     * reloaded every time the app syncs its data with the FamilyMap server. It is set to null on
     * logout.
     */
    private ArrayList<Event> mAllEvents;
    public ArrayList<Event> getAllEvents() { return mAllEvents; }
    public void setAllEvents(ArrayList<Event> mAllEvents) { this.mAllEvents = mAllEvents; }

    /**
     * Retrieves all events not currently filtered out by the user.
     * @return a not-null, but potentially empty array list of event objects
     * @see Filter
     */
    public ArrayList<Event> getNotFilteredEvents() {
        if(mAllEvents == null || mAllPeople == null || mRootPersonID == null) {
            Log.e(TAG, "Unable to run getNotFilteredEvents due to null class members");
            return null;
        }

        // Start with all events
        ArrayList<Event> events = mAllEvents;

        // Are there any family-side filters applied?
        try {
            if (mFilter.getFilterFathersSide())
                events = filterEventsByFamilySide(events, findPersonByID(mRootPersonID).getFatherID());
            if (mFilter.getFilterMothersSide())
                events = filterEventsByFamilySide(events, findPersonByID(mRootPersonID).getMotherID());

            // Are there any gender-filters applied?
            if (mFilter.getFilterMaleEvents())
                events = filterEventsByGender(events, "m");
            if (mFilter.getFilterFemaleEvents())
                events = filterEventsByGender(events, "f");

            // Are there any event-type filters applied?
            if (mFilter.getFilterBaptismEvents())
                events = filterEventsByEventType("baptism");
            if (mFilter.getFilterBirthEvents())
                events = filterEventsByEventType("birth");
            if (mFilter.getFilterCensusEvents())
                events = filterEventsByEventType("census");
            if (mFilter.getFilterChristeningEvents())
                events = filterEventsByEventType("christening");
            if (mFilter.getFilterDeathEvents())
                events = filterEventsByEventType("death");
            if (mFilter.getFilterMarriageEvents())
                events = filterEventsByEventType("marriage");


        } catch(Exception e) {
            // TODO: Handle exceptions thrown
        }


        return events;
    }

    /**
     * Filters all events associated with a given person ID and, if the person connected to that ID
     * has parents, the method recurses on the IDs of that person's parents.
     * @param events an non-empty array-list of event objects
     * @param parent_id a non-empty string
     * @return a filtered array-list of event objects
     * @throws Exception when given invalid input
     */
    private ArrayList<Event> filterEventsByFamilySide(ArrayList<Event> events, String parent_id)
            throws Exception {
        return null;
    }

    /**
     * Filters all events associated with a person of a given gender out of an array-list of events.
     * Note, this method assumes all events in mAllEvents correspond with a unique person in
     * mAllPeople.
     * @param events a non-empty array-list of event objects
     * @return a filtered array-list of event objects
     * @throws Exception when given invalid input
     */
    private ArrayList<Event> filterEventsByGender(ArrayList<Event> events, String gender)
            throws Exception {
        if(events == null || gender == null)
            throw new Exception("Null pointer passed to filterEventsByGender");
        if(events.size() == 0)
            throw new Exception("Empty array-list passed to filterEventsByGender");
        if(!gender.equals("m") && !gender.equals("f"))
            throw new Exception("Invalid gender passed to filterEventsByGender");

        for(Iterator<Event> iterator = events.iterator(); iterator.hasNext();) {
            Event event = iterator.next();

            // Does the gender of the person associated with this event match the given gender?
            if(findPersonByID(event.getPersonID()).getGender().equals(gender))
                // Then remove the event from the array-list
                iterator.remove();
        }

        return events;
    }

    /**
     * Filters all events of a given type (e.g., "birth", "baptism", "death").
     * @param events a non-empty array-list of event objects
     * @param event_type a non-empty string
     * @return a filtered array-list of event objects
     * @throws Exception when given invalid input
     */
    private ArrayList<Event> filterEventsByEventType(ArrayList<Event> events, String event_type)
            throws Exception {
        if(events == null || events.size() == 0)
            throw new Exception("Invalid events passed to filterEventsByEventType");
        if(event_type == null || event_type.equals(""))
            throw new Exception("Invalid event_type passed to filterEventsByEventType");

        for(Iterator<Event> iterator = events.iterator(); iterator.hasNext();) {
            // Does the type of the event match the given type?
            if(iterator.next().getEventType().equals(event_type))
                // Then remove the event from the array-list
                iterator.remove();
        }

        return events;
    }

    /**
     * Searches for a person with a given ID in mAllPeople. Note, this method assumes unique person
     * IDs for all people in mAllPeople.
     * @param id a non-empty string
     * @return a person object if found, null otherwise
     * @throws Exception when given invalid input
     */
    private Person findPersonByID(String id) {
        // Iterate through the array-list of people
        for(Person person : mAllPeople) {
            // Was a person with the ID found?
            if (person.getPersonID().equals(id)) return person;
        }

        // No one was found with the given ID
        return null;
    }

}
