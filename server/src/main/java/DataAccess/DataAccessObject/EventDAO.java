package DataAccess.DataAccessObject;
import static Server.JSONDecoder.getLocation;
import static java.lang.Math.random;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

import  DataAccess.Database;
import  DataAccess.DatabaseException;
import  Model.Event;
import Model.Person;

public class EventDAO {
// Class Methods
    /**
     * CREATE:
     * Corresponds with the INSERT statement in SQL. Checks first to confirm that the event given is
     * unique and that the person and descendant corresponding to the event exist already in the
     * database (and that they are connected). Finally, adds the event to the database.
	 * 
	 * @param event, an Event object
     * @throws DatabaseException detailing reason for failure
     */
    public static void create(Event event) throws DatabaseException {
        // check input
        if(event == null)
            throw new DatabaseException("Method EventDAO.create passed null pointer");
        if(!event.membersValid())
            throw new DatabaseException("Method EventDAO.create passed invalid event object.");
        if(read(event.getEventID()) != null)
            throw new DatabaseException("Method EventDAO.create passed event which already exists" +
                                        "in database.");
        if(PersonDAO.read(event.getPersonID()) == null)
            throw new DatabaseException("Method EventDAO.create passed event associated with a " +
                                        "person not currently in the database.");
        if(UserDAO.read(event.getDescendant()) == null)
			throw new DatabaseException("Method EventDAO.create passed event belonging to a user " +
                                        "not currently in the database.");
        if(!PersonDAO.read(event.getPersonID()).getDescendant().equals(event.getDescendant()))
            throw new DatabaseException("Event object with disjoint person and descendant data " +
                                        "passed to EventDAO.create.");

        // insert into db
        try {
            // declarations
            String sql;
            PreparedStatement statement;

            // prepare statement
            sql = "INSERT INTO " + TABLE_NAME + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
            statement = Database.getInstance().getConnection().prepareStatement(sql);

            // fill statement
            statement.setString(1, event.getEventID());
            statement.setString(2, event.getDescendant());
            statement.setString(3, event.getPersonID());
            statement.setDouble(4, event.getLatitude());
            statement.setDouble(5, event.getLongitude());
            statement.setString(6, event.getCountry());
            statement.setString(7, event.getCity());
            statement.setString(8, event.getEventType());
            statement.setInt(9, event.getYear());

            // execute statement
            statement.executeUpdate();

        } catch(SQLException e) {
            throw new DatabaseException("Unable to insert event into table. " + e.getMessage());
        }
    }

    /**
     * CREATE (RECURSIVE):
     * Climbs a given family tree, adding events to each person where appropriate. The person
     * relating to the ID given in the initial method call is assumed to currently be alive and to
     * be ~20 years-old. As the method climbs the tree, birthdays, baptisms, wedding  and death
     * dates are scaled and added. Finally, this function also "marries" existing parents to each
     * other (i.e., sets their 'spouse_id's respectively).
     *
     * @param username, a unique non-empty string connected to a user <i>already</i> in the database
     * @param person_id, the identifier of a person already in the database
     * @param birth_year, a positive integer less than the current year
     * @return the number of events added to the database
     * @throws DatabaseException on bad input or exception encountered
     */
    public static int createRecursive(String username, String person_id, int birth_year)
            throws DatabaseException {
        // check input
        if(username == null || person_id == null)
            throw new DatabaseException("Null pointer passed to EventDAO.createRecursive.");
        if(username.equals("") || person_id.equals(""))
            throw new DatabaseException("Empty string passed to EventDAO.createRecursive.");
        if(UserDAO.read(username) == null)
            throw new DatabaseException("Username passed to EventDAO.createRecursive not found " +
                                        "in database.");
        if(PersonDAO.read(person_id) == null)
            throw new DatabaseException("Person ID passed to EventDAO.createRecursive not found " +
                                        "in database.");
        if(birth_year > Calendar.getInstance().get(Calendar.YEAR))
            throw new DatabaseException("Invalid birth year passed to EventDAO.createRecursive.");

        final int CURRENT_YEAR = Calendar.getInstance().get(Calendar.YEAR);
        int num_events = 0;

        try {
            int age;
            Person person;
            String father_id;
            String mother_id;

            // get age
            age = CURRENT_YEAR - birth_year;

            // set person events
            num_events += createBirth(username, person_id, birth_year);
            num_events += createBaptism(username, person_id, age);
            num_events += createDeath(username, person_id, age);

            // get person & parents
            person = PersonDAO.read(person_id);
            father_id = person.getFatherID();
            mother_id = person.getMotherID();

            // has parents?
            if (father_id != null && mother_id != null) {
                int year_of_marriage;
                int father_birth_year;
                int mother_birth_year;

                // calc years
                year_of_marriage = birth_year - (int)(random() * 15) + (int)(random() * 3);
                father_birth_year = year_of_marriage - (int)(random() * 20) - 20;
                mother_birth_year = year_of_marriage - (int)(random() * 20) - 18;

                // set marriage event
                num_events += createMarriage(username, father_id, mother_id, year_of_marriage);

                // recurse on parents
                num_events += createRecursive(username, father_id, father_birth_year);
                num_events += createRecursive(username, mother_id, mother_birth_year);
            }
        } catch(DatabaseException e) {
            throw new DatabaseException("EventDAO.createRecursive failed. " + e.getMessage());
        }

        return num_events;
    }

    /**
     * CREATE BIRTH:
     * Adds a birth event to the given user.
     *
     * @param username, a unique non-empty string connected to a user <i>already</i> in the database
     * @param person_id, the identifier associated with a person already in the database
     * @param birth_year, an integer less than the current year
     * @return the number of events added
     * @throws DatabaseException on bad input or exception encountered
     */
    private static int createBirth(String username, String person_id, int birth_year)
            throws DatabaseException {
        final int EVENT_ADDED = 1;

        try {
            Event birth = new Event(username, person_id, getLocation(), "birth", birth_year);
            create(birth);
            return EVENT_ADDED;

        } catch(Exception e) {
            throw new DatabaseException("EventDAO.createBirth failed. " + e.getMessage());
        }
    }

    /**
     * CREATE BAPTISM:
     * Decides whether or not to add a baptism event to the given person.
     *
     * @param username, a unique non-empty string connected to a user <i>already</i> in the database
     * @param person_id, the identifier associated with a person already in the database
     * @param age, an integer greater than zero
     * @return the number of events added
     * @throws DatabaseException on bad input or exception encountered
     */
    private static int createBaptism(String username, String person_id, int age)
            throws DatabaseException {
        final int EVENT_ADDED = 1;
        final int EVENT_NOT_ADDED = 0;
        final double BAPTISMAL_RATE = 0.33;

        // was the person baptized?
        if(random() < BAPTISMAL_RATE) {
            final int CURRENT_YEAR = Calendar.getInstance().get(Calendar.YEAR);

            // age at baptism?
            int age_at_baptism = (int)(random()*random()*age);
            int year_of_baptism = CURRENT_YEAR - age + age_at_baptism;

            // create event
            try {
                Event baptism = new Event(username, person_id, getLocation(), "baptism",
                                          year_of_baptism);
                create(baptism);

            } catch(Exception e) {
                throw new DatabaseException("EventDAO.createBaptism failed. " + e.getMessage());
            }

            return EVENT_ADDED;

        } else {
            return EVENT_NOT_ADDED;
        }
    }

    /**
     * CREATE MARRIAGE:
     * Marries two people.
     *
     * @param username, a unique non-empty string connected to a user <i>already</i> in the database
     * @param person_id1, the identifier associated with a person already in the database
     * @param person_id2, the identifier associated with a person already in the database
     * @param year_of_marriage, an integer less than CURRENT_YEAR
     * @return the number of events added
     * @throws DatabaseException on bad input or exception encountered
     */
    private static int createMarriage(String username, String person_id1, String person_id2,
                                      int year_of_marriage) throws DatabaseException {
        final int EVENT_ADDED = 2;

        try {
            Event marriage1, marriage2;

            // create events
            marriage1 = new Event(username, person_id1, getLocation(), "marriage",
                    year_of_marriage);
            marriage2 = new Event(username, person_id2, getLocation(), "marriage",
                    year_of_marriage);
            create(marriage1);
            create(marriage2);

            // link spouses
            System.out.println("Linking couple [" + PersonDAO.read(person_id1).getPersonID() + "] and [" + PersonDAO.read(person_id2).getPersonID() + "] together");
            System.out.println("Person one's spouse ID: " + PersonDAO.read(person_id1).getSpouseID());
            PersonDAO.updateSpouseID(person_id1, person_id2);
            System.out.println("Person one's spouse ID: " + PersonDAO.read(person_id1).getSpouseID());
            System.out.println();
            PersonDAO.read(person_id2).setSpouseID(person_id1);

            return EVENT_ADDED;

        } catch(Exception e) {
            throw new DatabaseException("EventDAO.createMarriage failed. " + e.getMessage());
        }
    }

    /**
     * CREATE DEATH:
     * Checks the age of the person associated to the given ID and decides whether or not to add a
     * death event.
     *
     * @param username, a unique non-empty string connected to a user <i>already</i> in the database
     * @param person_id, the identifier associated with a person already in the database
     * @param age, an integer greater than zero
     * @return the number of events added
     * @throws DatabaseException on bad input or exception encountered
     */
    private static int createDeath(String username, String person_id, int age)
            throws DatabaseException {
        final int EVENT_ADDED = 1;
        final int EVENT_NOT_ADDED = 0;
        final double OLD_AGE_MORTALITY_RATE = 0.5;
        final int OLD_AGE = 75;
        final int MAX_AGE = 95;

        // person dead?
        if(age > MAX_AGE || age > OLD_AGE && random() > OLD_AGE_MORTALITY_RATE) {
            final int CURRENT_YEAR = Calendar.getInstance().get(Calendar.YEAR);

            // age at death?
            int age_at_death = (int)(random()*(MAX_AGE-OLD_AGE)+OLD_AGE);
            int year_of_death = CURRENT_YEAR - age + age_at_death;
            if(year_of_death > CURRENT_YEAR) year_of_death = CURRENT_YEAR;

            // create event
            try {
                Event death = new Event(username, person_id, getLocation(), "death", year_of_death);
                create(death);

            } catch(Exception e) {
                throw new DatabaseException("EventDAO.createDeath failed. " + e.getMessage());
            }

            return EVENT_ADDED;
        } else {
            return EVENT_NOT_ADDED;
        }
    }

    /**
     * READ (INTERNAL):
     * Corresponds with the SELECT statement in SQL. Searches for an event in database with the
     * given ID.
     *
     * @param event_id, non-empty string
     * @return an event object if found, null otherwise
     */
    public static Event read(String event_id) throws DatabaseException {
        // check input
        if(event_id == null)
            throw new DatabaseException("Method EventDAO.read passed null pointer");
        if(event_id.equals(""))
            throw new DatabaseException("Method EventDAO.read passed empty string.");

        // initialize event (will remain null if never found)
        Event event = null;

        try {
            // declarations
            String sql;
            PreparedStatement statement;
            ResultSet result_set;

            // prepare statement
            sql = "SELECT * FROM " + TABLE_NAME + " WHERE (event_id = ?);";
            statement = Database.getInstance().getConnection().prepareStatement(sql);

            // insert event_id
            statement.setString(1, event_id);

            // execute sql
            result_set = statement.executeQuery();

            // event found?
            if(result_set.next()) {
                // construct event
                event = new Event(result_set.getString("event_id"),
                                  result_set.getString("descendant"),
                                  result_set.getString("person_id"),
                                  result_set.getDouble("latitude"),
                                  result_set.getDouble("longitude"),
                                  result_set.getString("country"),
                                  result_set.getString("city"),
                                  result_set.getString("event_type"),
                                  result_set.getInt("year"));

                // others found?
                if(result_set.next()) throw new DatabaseException("Method EventDAO.read() " +
                        "returned multiple events (ID: " +
                        event_id + ").");
            }

        } catch(SQLException e) {
            throw new DatabaseException("Unable to read on event (ID: " + event_id + "): " +
                    e.getMessage());
        }

        return event;
    }

    /**
     * READ FAMILY EVENTS:
     * Corresponds with the SELECT statement in SQL. Searches EventTable for all events whose
     * descendant value is equal to the given username.
     *
     * @param username, non-empty string
     * @throws DatabaseException
     * @return ArrayList of person objects if found, null otherwise
     * @see Event
     */
    public static ArrayList<Event> readFamilyEvents(String username) throws DatabaseException {
        // check input
        if(username == null)
            throw new DatabaseException("Method EventDAO.readFamilyEvents passed null pointer");
        if(username.equals(""))
            throw new DatabaseException("Method EventDAO.readFamilyEvents passed empty string.");

        try {
            ArrayList<Event> events = null;
            PreparedStatement statement;
            ResultSet result_set;

            // prepare statement
            String sql = "SELECT * FROM " + TABLE_NAME + " WHERE descendant = ?;";
            statement = Database.getInstance().getConnection().prepareStatement(sql);

            // insert person_id
            statement.setString(1, username);

            // execute sql
            result_set = statement.executeQuery();

            // people found?
            while(result_set.next()) {
                if(events == null) events = new ArrayList<>();
                Event temp;

                // construct person
                temp = new Event(result_set.getString("event_id"),
                                 result_set.getString("descendant"),
                                 result_set.getString("person_id"),
                                 result_set.getDouble("latitude"),
                                 result_set.getDouble("longitude"),
                                 result_set.getString("country"),
                                 result_set.getString("city"),
                                 result_set.getString("event_type"),
                                 result_set.getInt("year"));

                // add to array
                events.add(temp);
            }

            return events;

        } catch(SQLException e) {
            throw new DatabaseException("EventDAO.readFamilyEvents failed.", e);
        }
    }

    /**
     * READ EVENT:
     * Corresponds with the SELECT statement in SQL. Searches EventTable for the event with the
     * given ID within some user's tree.
     *
     * @param username, non-empty string
     * @param event_id, non-empty string
     * @throws DatabaseException
     * @return Event object if found, null otherwise
     * @see Event
     */
    public static Event readEvent(String username, String event_id) throws DatabaseException {
        // check input
        if(username == null || event_id == null)
            throw new DatabaseException("Null pointer passed to EventDAO.readEvent.");
        if(username.equals("") || event_id.equals(""))
            throw new DatabaseException("Empty string passed to EventDAO.readEvent.");

        try {
            Event event = null;
            String sql;
            PreparedStatement statement;
            ResultSet result_set;

            // prepare statement
            sql = "SELECT * FROM " + TABLE_NAME + " WHERE descendant = ? AND event_id = ?;";
            statement = Database.getInstance().getConnection().prepareStatement(sql);

            // fill statement
            statement.setString(1, username);
            statement.setString(2, event_id);

            // execute sql
            result_set = statement.executeQuery();

            // event found?
            if(result_set.next()) {
                // construct event
                event = new Event(result_set.getString("event_id"),
                                  result_set.getString("descendant"),
                                  result_set.getString("person_id"),
                                  result_set.getDouble("latitude"),
                                  result_set.getDouble("longitude"),
                                  result_set.getString("country"),
                                  result_set.getString("city"),
                                  result_set.getString("event_type"),
                                  result_set.getInt("year"));

                // others found?
                if(result_set.next())
                    throw new DatabaseException("EventDAO.readEvent returned multiple events.");
            }

            return event;

        } catch(SQLException e) {
            throw new DatabaseException("EventDAO.readEvent failed.", e);
        }
    }

    /**
     * DELETE:
     * Corresponds with the DELETE statement in SQL. Searches EventTable for the given event ID.
	 * If found, the event is deleted; else, no action is taken.
	 * 
     * @param event_id, a non-empty string denoting an event
     * @throws DatabaseException
     */
    public static void delete(String event_id) throws DatabaseException {
        // check input
        if(event_id == null)
            throw new DatabaseException("Method EventDAO.delete passed null pointer.");
        if(event_id.equals(""))
            throw new DatabaseException("Method EventDAO.delete passed empty string.");

        // delete event
        try {
            // declarations
            String sql;
            PreparedStatement statement;

            // prepare statement
            sql = "DELETE FROM " + TABLE_NAME + " WHERE event_id = ?;";
            statement = Database.getInstance().getConnection().prepareStatement(sql);

            // fill statement
            statement.setString(1, event_id);

            // execute statement
            statement.executeUpdate();

        } catch(SQLException e) {
            throw new DatabaseException("Method EventDAO.delete failed.", e);
        }
    }

    /**
     * DELETE ALL:
     * Corresponds with the DELETE statement in SQL. Deletes all data from EventTable.
     *
     * @throws DatabaseException detailing reason for failure
     */
    public static void deleteAll() throws DatabaseException {
        try {
            // create statement
            Statement statement = Database.getInstance().getConnection().createStatement();

            // execute statement
            statement.executeUpdate("DELETE FROM " + TABLE_NAME + ";");

        }
        catch(SQLException e) {
            throw new DatabaseException("Unable to delete all data from EventTable in database.");
        }
    }

    /**
     * DELETE (CONDITIONAL):
     * Corresponds with the DELETE statement in SQL. Deletes all data from EventTable satisfying
     * some given condition.
     *
     * @param column, a non-empty string
     * @param value, a non-empty string
     * @throws DatabaseException in case of bad input or exception encountered
     */
    public static void deleteConditional(String column, String value)
            throws DatabaseException {
        // check input
        if(column == null || value == null)
            throw new DatabaseException("Null pointer passed to EventDAO.deleteConditional.");
        if(column.equals("") || value.equals(""))
            throw new DatabaseException("Empty string passed to EventDAO.deleteConditional.");

        try {
            String sql;
            PreparedStatement statement;

            // prepare statement
            sql = "DELETE FROM " + TABLE_NAME + " WHERE " + column + " = ?;";
            statement = Database.getInstance().getConnection().prepareStatement(sql);

            // fill statement
            statement.setString(1, value);

            // execute statement
            statement.executeUpdate();

        } catch(SQLException e) {
            throw new DatabaseException("EventDAO.deleteConditional failed." + e.getMessage());
        }
    }

// Class Members
    /**
     * TABLE NAME:
     * The name of the table associated with this DAO.
     */
    private static final String TABLE_NAME = "EventTable";
}
