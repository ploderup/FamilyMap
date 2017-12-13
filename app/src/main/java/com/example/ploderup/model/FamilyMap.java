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
     * @return whether the event should be filtered or not
     * @see Filter
     */
    public boolean isFiltered(Event event) {
        if (findPersonByID(event.getPersonID()) == null) {
            Log.e(TAG, "Event passed to isFiltered is not associated with any person in mAllPeople");
            return false;
        }

        // Is the event's type filtered?
        if (mFilter.getFilterBaptismEvents() && event.getEventType().equals("baptism"))
            return true;
        if (mFilter.getFilterBirthEvents() && event.getEventType().equals("birth"))
            return true;
        if (mFilter.getFilterCensusEvents() && event.getEventType().equals("census"))
            return true;
        if (mFilter.getFilterChristeningEvents() && event.getEventType().equals("christening"))
            return true;
        if (mFilter.getFilterDeathEvents() && event.getEventType().equals("death"))
            return true;
        if (mFilter.getFilterMarriageEvents() && event.getEventType().equals("marriage"))
            return true;

        // Is the gender of the person associated with the event filtered?
        if (mFilter.getFilterMaleEvents() &&
                findPersonByID(event.getPersonID()).getGender().equals("m"))
            return true;
        if (mFilter.getFilterFemaleEvents() &&
                findPersonByID(event.getPersonID()).getGender().equals("f"))
            return true;

        // TODO: Is the event associated with a side of the tree currently filtered?

        return false;
    }

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
        ArrayList<Event> events = new ArrayList<>(mAllEvents);

        // Are there any family-side filters applied?
        try {
            if (mFilter.getFilterFathersSide())
                events = filterEventsByFamilySide(events,
                        findPersonByID(mRootPersonID).getFatherID());
            if (mFilter.getFilterMothersSide())
                events = filterEventsByFamilySide(events,
                        findPersonByID(mRootPersonID).getMotherID());

            // Are there any gender-filters applied?
            if (mFilter.getFilterMaleEvents())
                events = filterEventsByGender(events, "m");
            if (mFilter.getFilterFemaleEvents())
                events = filterEventsByGender(events, "f");

            // Are there any event-type filters applied?
            if (mFilter.getFilterBaptismEvents())
                events = filterEventsByEventType(events, "baptism");
            if (mFilter.getFilterBirthEvents())
                events = filterEventsByEventType(events, "birth");
            if (mFilter.getFilterCensusEvents())
                events = filterEventsByEventType(events, "census");
            if (mFilter.getFilterChristeningEvents())
                events = filterEventsByEventType(events, "christening");
            if (mFilter.getFilterDeathEvents())
                events = filterEventsByEventType(events, "death");
            if (mFilter.getFilterMarriageEvents())
                events = filterEventsByEventType(events, "marriage");


        } catch(Exception e) {
            Log.e(TAG, e.getMessage());
        }

        return events;
    }

    /**
     * Filters all events associated with a given person ID and, if the person connected to that ID
     * has parents, the method recurses on the IDs of that person's parents.
     * @param events an non-empty array-list of event objects
     * @param person_id a non-empty string
     * @return a filtered array-list of event objects
     * @throws Exception when given invalid input
     */
    private ArrayList<Event> filterEventsByFamilySide(ArrayList<Event> events, String person_id)
            throws Exception {
        if (events == null || person_id == null)
            throw new Exception("Null pointer passed to filterEventsByFamilySide");
        if (events.size() == 0)
            throw new Exception("Empty array-list passed to filterEventsByFamilySide");
        if (person_id.equals(""))
            throw new Exception("Empty string passed to filterEventsByFamilySide");
        if (findPersonByID(person_id) == null)
            throw new Exception("Identifier passed to filterEventsByFamilySide not found in tree");

        // Remove all events associated with this person
        for(Iterator<Event> iterator = events.iterator(); iterator.hasNext();)
            // Is this event associated with the given person ID?
            if (iterator.next().getPersonID().equals(person_id))
                // Then remove it
                iterator.remove();

        // Does the person have a father in the family tree?
        if (findPersonByID(person_id).getFatherID() != null)
            events = filterEventsByFamilySide(events, findPersonByID(person_id).getFatherID());

        // Does the person have a mother in the family tree?
        if (findPersonByID(person_id).getMotherID() != null)
            events = filterEventsByFamilySide(events, findPersonByID(person_id).getMotherID());

        return events;
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
        if (events == null || gender == null)
            throw new Exception("Null pointer passed to filterEventsByGender");
        if (events.size() == 0)
            throw new Exception("Empty array-list passed to filterEventsByGender");
        if (!gender.equals("m") && !gender.equals("f"))
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
}
