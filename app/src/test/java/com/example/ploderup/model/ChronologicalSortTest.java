package com.example.ploderup.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Model.Event;

import static org.junit.Assert.*;

/**
 * Note, that the tests in this class are very specific to the compareTo function called on the
 * Event class. Rather than writing code to try and sort through all of the intricacies required to
 * sort events (e.g. alphabetically and chronologically, with qualifiers on birth and death events),
 * I modified the Event class so as to implement the Comparable interface and allow for a more
 * simple sort. So, for that reason, most tests here test the compareTo method itself.
 *
 * @see Event especially the compareTo method
 */
public class ChronologicalSortTest {
// MEMBERS
    private Event birth;
    private Event death;
    private Event early_year;
    private Event late_year;
    private Event a_alpha;
    private Event z_alpha;

    private List<Event> events;


// TEST BOOKENDS
    @Before
    public void setUp() {
        birth =         new Event(null, null, null, null, null, null, null, "birth", 2000);
        death =         new Event(null, null, null, null, null, null, null, "death", 1000);
        early_year =    new Event(null, null, null, null, null, null, null, "immigrated", 10);
        late_year =     new Event(null, null, null, null, null, null, null, "Mars landing", 2050);
        a_alpha =       new Event(null, null, null, null, null, null, null, "a", 0);
        z_alpha =       new Event(null, null, null, null, null, null, null, "z", 0);

        events =        new ArrayList<>();
        events.add(birth);
        events.add(death);
        events.add(early_year);
        events.add(late_year);
        events.add(a_alpha);
        events.add(z_alpha);
    }

    @After
    public void tearDown() {
        birth = null;
        death = null;
        early_year = null;
        late_year = null;
        a_alpha = null;
        z_alpha = null;
    }


// UNIT TESTS
    /**
     * Tests a variety of one-to-one comparisons outlined explicitly the specifications for this
     * project, as follows...
     * 1. Birth events, if present, are always first (whether they have a year or not)
     * 2. Events with years, sorted primarily by year, and secondarily by description normalized to
     *    lower-case
     * 3. Events without years sorted by description normalized to lower-case
     * 4. Death events, if present, are always last (whether they have a year or not)
     */
    @Test
    public void testAllComparisons() {
        // Compare birth against all other events
        assertTrue(birth.compareTo(birth) == 0);
        assertTrue(birth.compareTo(death) < 0);
        assertTrue(birth.compareTo(early_year) < 0);
        assertTrue(birth.compareTo(late_year) < 0);
        assertTrue(birth.compareTo(a_alpha) < 0);
        assertTrue(birth.compareTo(z_alpha) < 0);

        // Compare death against all other events
        assertTrue(death.compareTo(birth) > 0);
        assertTrue(death.compareTo(death) == 0);
        assertTrue(death.compareTo(early_year) > 0);
        assertTrue(death.compareTo(late_year) > 0);
        assertTrue(death.compareTo(a_alpha) > 0);
        assertTrue(death.compareTo(z_alpha) > 0);

        // Compare an event with an early year against all other events
        assertTrue(early_year.compareTo(birth) > 0);
        assertTrue(early_year.compareTo(death) < 0);
        assertTrue(early_year.compareTo(early_year) == 0);
        assertTrue(early_year.compareTo(late_year) < 0);
        assertTrue(early_year.compareTo(a_alpha) < 0);
        assertTrue(early_year.compareTo(z_alpha) < 0);

        // Compare an event with a late year against all other events
        assertTrue(late_year.compareTo(birth) > 0);
        assertTrue(late_year.compareTo(death) < 0);
        assertTrue(late_year.compareTo(early_year) > 0);
        assertTrue(late_year.compareTo(late_year) == 0);
        assertTrue(late_year.compareTo(a_alpha) < 0);
        assertTrue(late_year.compareTo(z_alpha) < 0);

        // Compare an event w/o a valid year, whose type starts with 'a' against all other events
        assertTrue(a_alpha.compareTo(birth) > 0);
        assertTrue(a_alpha.compareTo(death) < 0);
        assertTrue(a_alpha.compareTo(early_year) > 0);
        assertTrue(a_alpha.compareTo(late_year) > 0);
        assertTrue(a_alpha.compareTo(a_alpha) == 0);
        assertTrue(a_alpha.compareTo(z_alpha) < 0);

        // Compare an event w/o a valid year, whose type starts with 'a' against all other events
        assertTrue(z_alpha.compareTo(birth) > 0);
        assertTrue(z_alpha.compareTo(death) < 0);
        assertTrue(z_alpha.compareTo(early_year) > 0);
        assertTrue(z_alpha.compareTo(late_year) > 0);
        assertTrue(z_alpha.compareTo(a_alpha) > 0);
        assertTrue(z_alpha.compareTo(z_alpha) == 0);
    }

    /**
     * Tests the sort method, which utilizes compareTo in the Event class.
     */
    @Test
    public void testSort() {
        // Sort the events in the array-list
        Collections.sort(events);

        // Check the ordering of the events post-sort
        assertTrue(events.get(0).equals(birth));
        assertTrue(events.get(1).equals(early_year));
        assertTrue(events.get(2).equals(late_year));
        assertTrue(events.get(3).equals(a_alpha));
        assertTrue(events.get(4).equals(z_alpha));
        assertTrue(events.get(5).equals(death));
    }
}
