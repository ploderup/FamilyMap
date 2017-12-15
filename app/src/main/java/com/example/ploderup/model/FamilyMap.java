package com.example.ploderup.model;

import android.util.Log;

import com.example.ploderup.userinterface.BuildConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
     * logout. Note, it is also sorted as specified in the app.
     */
    private ArrayList<Event> mAllEvents;
    public ArrayList<Event> getAllEvents() { return mAllEvents; }
    public void setAllEvents(ArrayList<Event> mAllEvents) {
        this.mAllEvents = mAllEvents;

        // If the array-list was just initialized (and not set to null), then sort it
        if(this.mAllEvents != null) Collections.sort(this.mAllEvents);
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
        if (event == null) {
            Log.d(TAG, "Null pointer passed to isFiltered");
            return false;
        }
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
     * Retrieves all parents, spouse and children for the person associated with the given ID.
     */
    public ArrayList<Person> getPersonsFamily(String person_id) {
        if (person_id == null) {
            Log.e(TAG, "Null pointer passed to getPersonsFamily");
            return null;
        }
        if (person_id.equals("")) {
            Log.e(TAG, "Empty string passed to getPersonsFamily");
            return null;
        }
        if (findPersonByID(person_id) == null) {
            Log.e(TAG, "Person ID passed to getPersonsFamily not connected to any people in " +
                    "app memory");
        }

        ArrayList<Person> family = new ArrayList<>();
        Person person = findPersonByID(person_id);

        // Does the person have a father?
        if (person.getFatherID() != null)
            family.add(findPersonByID(person.getFatherID()));

        // Does the person have a mother?
        if (person.getMotherID() != null)
            family.add(findPersonByID(person.getMotherID()));

        // Does the person have a spouse?
        if (person.getSpouseID() != null)
                family.add(findPersonByID(person.getSpouseID()));

        // Does the person have any children?
        for (Person p : mAllPeople) {
            if (p.getFatherID() != null && p.getFatherID().equals(person_id) ||
                    p.getMotherID() != null && p.getMotherID().equals(person_id)) {
                family.add(p);
            }
        }

        return family;
    }

    /**
     * Retrieves all events associated with a person connected to a given ID.
     */
    public ArrayList<Event> getPersonsEvents(String person_id) {
        if (person_id == null) {
            Log.e(TAG, "Null pointer passed to getPersonsEvents");
            return null;
        }
        if (person_id.equals("")) {
            Log.e(TAG, "Empty string passed to getPersonsEvents");
            return null;
        }

        ArrayList<Event> events = new ArrayList<>();

        // Get all events associated with the person connected to the given ID
        for (Event event : mAllEvents) {
            if (event.getPersonID().equals(person_id)) events.add(event);
        }

        return events;
    }

    /**
     * Calculates the relationship between two given people. If the first person is the daughter of
     * the second, for instance, then "Daughter" will be returned (rather than "Mother" or
     * "Father").
     */
    public String calculateRelationship(String person_a, String person_b) {
        if (person_a == null || person_b == null)
            return null;
        if (person_a.equals("") || person_b.equals(""))
            return null;

        // Retrieve the people connected to the given IDs
        final Person PERSON_A = findPersonByID(person_a);
        final Person PERSON_B = findPersonByID(person_b);

        // Is A B's father?
        if (person_a.equals(PERSON_B.getFatherID())) return "Father";

        // Is A B's mother?
        if (person_a.equals(PERSON_B.getMotherID())) return "Mother";

        // Is A B's spouse?
        if (person_a.equals(PERSON_B.getSpouseID()))
            if (PERSON_A.getGender().equals("m")) return "Husband"; else return "Wife";

        // Does A have parents?
        if (PERSON_A.getFatherID() != null)
            // Is A B's child?
            if (PERSON_A.getFatherID().equals(person_b))
                if (PERSON_A.getGender().equals("m")) return "Son"; else return "Daughter";
        if (PERSON_A.getMotherID() != null)
            // Is A B's child?
            if (PERSON_A.getMotherID().equals(person_b))
                if (PERSON_A.getGender().equals("m")) return "Son"; else return "Daughter";

        // A and B are not nuclear-ly related
        return "Not related";
    }
}
