package com.example.ploderup.model;

import java.util.ArrayList;
import java.util.List;

import Model.Event;
import Model.Person;

public class Search {
    private static Search sInstance = new Search();
    public static Search getInstance() { return sInstance; }
    private Search() {}

    private FamilyMap sFamilyMap = FamilyMap.getInstance();

    /**
     * Search results are set with each successful call to searchFamilyMap, for convenient access to
     * the list of objects found.
     */
    private List<Object> mSearchResults;
    public List<Object> getSearchResults() { return mSearchResults; }
    public void resetSearchResults() { mSearchResults = null; }

    /**
     * Searches through FamilyMap's people and events for anyone or anything whose name, event type,
     * or city or country matches some given string of characters.
     */
    public List<Object> searchFamilyMap(String query) {
        if (query == null) return null;
        if (query.equals("")) return null;

        List<Object> results = new ArrayList<>();

        // Set query to lower case
        query = query.toLowerCase();

        List<Person> people = sFamilyMap.getAllPeople();
        for (Person p : people) {
            // Does the person's name contain the given string?
            if (p.getFullName().toLowerCase().contains(query)) results.add(p);
        }

        List<Event> events = sFamilyMap.getAllEvents();
        for (Event e : events) {
            // Does the event's type, city or country contain the given string?
            if (e.getEventType().toLowerCase().contains(query) ||
                    e.getCity().toLowerCase().contains(query) ||
                    e.getCountry().toLowerCase().contains(query))
                results.add(e);

            // If not, then does the query exist in the name of the person associated w/ the event?
            else if (sFamilyMap.findPersonByID(e.getPersonID())
                    .getFullName().toLowerCase().contains(query))
                results.add(e);
        }

        return results;
    }
}
