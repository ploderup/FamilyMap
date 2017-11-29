package DataAccess;
import  java.io.File;
import  java.sql.*;
import  java.util.ArrayList;
import  Model.*;
import  DataAccess.DataAccessObject.*;

/**
 * DATABASE:
 * This Database class acts as an interface between Server services (e.g., ClearService,
 * FillService) and the Data Access Objects, or DAOs. Excluding private methods, all methods assume
 * an open connection before being called (services should handle this).
 */
public class Database {
// CONSTRUCTORS
    /**
     * PRIVATE CONSTRUCTOR:
     * Constructs a new Database object.
     */
    private Database() {
        try {
            final String driver = "org.sqlite.JDBC";
            Class.forName(driver);
        }
        catch(ClassNotFoundException e) {
            System.out.println("Unable to load JDBC driver.");
        }
    }

// Class Methods
    /**
     * REGISTER USER:
     * Creates a new user account, and generates 4 generations of ancestor data for the new user.
     *
     * @param username, a username not yet registered with the database
     * @param password, a valid password
     * @param email, a valid email address
     * @param first_name, a non-empty string
     * @param last_name, a non-empty string
     * @param gender, a string: "m" for male or "f" for female
     * @throws DatabaseException in case of bad input or when thrown a DBException by a DAO
     */
    public void registerUser(String username, String password, String email, String first_name,
                             String last_name, String gender) throws DatabaseException {
        // check username (other input with be checked after construction)
        if(username == null)
            throw new DatabaseException("Null pointer passed to Database.registerUser as " +
                                        "username.");
        if(username.equals(""))
            throw new DatabaseException("Empty string passed to Database.registerUser as " +
                                        "username.");

        try {
            // declarations
            final int NUMBER_OF_GENERATIONS = 4;
            User user;

            // unique username?
            if(UserDAO.read(username) != null)
                throw new DatabaseException("Username passed to Database.registerUser is not " +
                        "unique.");

            // create user
            user = new User(username, password, email, first_name, last_name, gender);
            if(!user.membersValid())
                throw new DatabaseException("Invalid input passed to Database.registerUser.");
            UserDAO.create(user);

            // fill family tree
            fillUserTree(username, NUMBER_OF_GENERATIONS);
        
        } catch(DatabaseException e) {
            throw new DatabaseException("Database.registerUser failed. " + e.getMessage());
        }
    }


    /**
     * LOGIN USER:
     * Checks whether the given username exists in the database, and whether or not the password
     * associated with it is the same as the one provided. If so, the an authentication token object
     * containing the user's username, person ID and an authentication token string is constructed
     * and then added to the database.
     *
     * @param username, a string
     * @param password, a string
     * @return an authentication token containing a token, username AND person ID
     * @throws DatabaseException when given bad input or when thrown a DBException by a DAO
     */
    public AuthToken loginUser(String username, String password) throws DatabaseException {
        // check input
        if(username == null || password == null)
            throw new DatabaseException("Null pointer passed to Database.loginUser method.");
        if(username.equals("") || password.equals(""))
            throw new DatabaseException("Empty string passed to Database.loginUser method.");

        // declarations
        AuthToken auth_token;

        try {
            // declarations
            User user;
            String person_id;
            
            // find user in db
            user = UserDAO.read(username);
            if(user == null)
                throw new DatabaseException("Username passed to Database.loginUser does not " +
                        "exist in database.");
            
            // verify password
            if(!user.getPassword().equals(password))
                throw new DatabaseException("Password passed to Database.loginUser does not match " +
                                            "password associated with username provided.");

            // get person id
            person_id = user.getPersonID();
            if(person_id == null)
                throw new DatabaseException("Person ID associated with given username is a null " +
                                            "pointer.");
            if(person_id.equals(""))
                throw new DatabaseException("Person ID associated with given username is an empty " +
                                            "string.");

            // construct auth token
            auth_token = new AuthToken(username, person_id);
            auth_token.setPersonID(person_id);

            // add token to database
            AuthTokenDAO.create(auth_token);

        } catch(DatabaseException e) {
            throw new DatabaseException("Method Database.loginUser failed. " + e.getMessage());
        }

        return auth_token;
    }

    /**
     * CLEAR:
     * Deletes ALL data from the database, including user accounts, auth tokens, and generated
     * person and event data. Note, this method does not remove the actual tables from the database,
     * just the information contained therein.
     *
     * @throws DatabaseException detailing reason for failure
     */
    public void clear() throws DatabaseException {
        try {
			// delete all data
            AuthTokenDAO.deleteAll();
            EventDAO.deleteAll();
            PersonDAO.deleteAll();
            UserDAO.deleteAll();
        
		} catch(Exception e) {
            throw new DatabaseException("Database.clearDatabase() failed. " + e.getMessage());
        }
    }

    /**
     * DELETE USER'S TREE:
     * Removes all data from the database associated with a given username, aside from that user's
     * information in the user and authentication token tables.
     *
     * @param username, a non-empty string
     * @throws DatabaseException if given bad input or thrown another DatabaseException
     */
    private void deleteUserTree(String username) throws DatabaseException {
        if(username == null)
            throw new DatabaseException("Null pointer passed to Database.deleteUserTree.");
        if(username.equals(""))
            throw new DatabaseException("Empty string passed to Database.deleteUserTree.");
        if(UserDAO.read(username) == null)
            throw new DatabaseException("Username passed to Database.deleteUserTree not in " +
                    "database.");

        try {
            // delete events & people
            EventDAO.deleteConditional("descendant", username);
            PersonDAO.deleteConditional("descendant", username);

        } catch(DatabaseException e) {
            throw new DatabaseException("Database.deleteUserTree failed. " + e.getMessage());
        }
    }

    /**
     * FILL USER'S TREE:
     * Populates the server's database with generated data for the specified user name. The required
     * "username" parameter must be a user already registered with the server. If there is any data
     * in the database already associated with the given user name, it is deleted. The optional
     * “generations” parameter lets the caller specify the number of generations of ancestors to be
     * generated, and must be a non-negative integer (the default is 4, which results in 31 new
     * persons each with associated events).
     *
     * @param username, a valid username in the database
     * @param num_generations, the number of generations to generate
     * @return an array containing the number of people and events added, respectively
     * @throws DatabaseException for bad input or encountered exception
     */
    public int[] fillUserTree(String username, int num_generations) throws DatabaseException {
        // check input
        if(username == null)
            throw new DatabaseException("Null pointer passed to Database.fillUserTree.");
        if(username.equals(""))
            throw new DatabaseException("Empty string passed to Database.fillUserTree.");
        if(UserDAO.read(username) == null)
            throw new DatabaseException("Username passed to Database.fillUserTree not registered " +
                                        "in database.");
        if(num_generations < 0)
            throw new DatabaseException("Negative number passed to Database.fillUserTree.");

        try {
            final int USER_BIRTH_YEAR = 1994;
            User user;
            Person person;

            int num_people;
            int num_events;

            // remove old tree
            deleteUserTree(username);

            // create user's person
            user = UserDAO.read(username);
            person = new Person(user.getPersonID(), username, user.getFirstName(),
                    user.getLastName(), user.getGender(), null, null, null);

            // generate new tree
            num_people = PersonDAO.createRecursive(username, person, num_generations);

            // add events to tree
            num_events = EventDAO.createRecursive(username, user.getPersonID(), USER_BIRTH_YEAR);

            return new int[] { num_people, num_events };

        } catch(DatabaseException e) {
            throw new DatabaseException("Database.fillUserTree failed. " + e.getMessage());
        }
    }

    /**
     * LOAD:
     * Clears ALL data from the database, and then loads the posted user, person, and event data
     * into the database.
     *
     * @throws DatabaseException    detailing reason for failure
     */
    public void load(ArrayList<Event> events, ArrayList<Person> people,
                     ArrayList<User> users) throws DatabaseException {
        // check input
        if(events == null || people == null || users == null)
            throw new DatabaseException("Null pointer passed to Database.loadDatabase.");

        // load into db
        try {
            // clear db
            clear();

            // load users
            for(User user : users) UserDAO.create(user);

            // load people
            for(Person person : people) PersonDAO.create(person);

            // load events
            for(Event event : events) EventDAO.create(event);
        
		} catch(Exception e) {
            throw new DatabaseException("Method loadDatabase() failed: " + e.getMessage());
        }
    }

    /**
     * VERIFY AUTHENTICATION TOKEN:
     * Check whether an authentication token is valid (i.e., stored in the database) and retrieves
     * the username associated with it.
     *
     * @param  token, an authentication token string
     * @return  the username associated with the token if valid, null otherwise
     * @throws  DatabaseException in the case of bad input or encountered DBException
     */
    public String verifyAuthToken(String token) throws DatabaseException {
        // check input
        if(token == null)
            throw new DatabaseException("Null pointer passed to Database.verifyAuthToken.");
        if(token.equals(""))
            throw new DatabaseException("Empty string passed to Database.verifyAuthToken.");

        try {
            AuthToken auth_token;

            // get username
            auth_token = AuthTokenDAO.read(token);
            if(auth_token != null)
                return auth_token.getUsername();
            else
                return null;

        } catch(DatabaseException e) {
            throw new DatabaseException("Database.verifyAuthToken failed. " + e.getMessage());
        }
    }

    /**
     * GET PERSON:
     * Searches a user's tree for a certain person.
     *
     * @param  username, non-empty string specifying user in database
     * @param  person_id, an identifier associated with a person in the user's tree
     * @return a Person object if found; else, null
     * @throws  DatabaseException detailing reason for failure
     */
    public Person getPerson(String username, String person_id) throws DatabaseException {
        // check input
        if(username == null || person_id == null)
            throw new DatabaseException("Null pointer passed to Database.getPerson.");
        if(username.equals("") || person_id.equals(""))
            throw new DatabaseException("Empty string passed to Database.getPerson.");
        if(PersonDAO.read(person_id) == null)
            throw new DatabaseException("Person ID passed to Database.getPerson does not exist " +
                    "in database.");
        if(!PersonDAO.read(person_id).getDescendant().equals(username))
            throw new DatabaseException("Person ID passed to Database.getPerson is not in the " +
                    "given user's tree.");

        try {
            // get person
            return PersonDAO.readPerson(username, person_id);

        } catch(DatabaseException e) {
            throw new DatabaseException("Database.getPerson failed. " + e.getMessage());
        }
    }

    /**
     * GET FAMILY:
     * Retrieves all family members of a given user.
     *
     * @param username, non-empty string specifying user in database
     * @return array of all family members of the user specified; null if no family members
     * @throws  DatabaseException detailing reason for failure
     */
    public ArrayList<Person> getFamily(String username) throws DatabaseException {
        // check input
        if(username == null)
            throw new DatabaseException("Null pointer passed to Database.getFamily.");
        if(username.equals(""))
            throw new DatabaseException("Empty string passed to Database.getFamily.");

        try {
            // get family
            ArrayList<Person> family = PersonDAO.readFamily(username);
            if(family == null)
                throw new DatabaseException("There are no people in the database associated with " +
                        "this username.");
            return family;

        } catch(DatabaseException e) {
            throw new DatabaseException("Database.getFamily failed. " + e.getMessage());
        }
    }

    /**
     * GET EVENT:
     * Returns the single Event object with the specified ID.
     *
     * @param username, a non-empty string
     * @param event_id, a non-empty string
     * @throws  DatabaseException detailing reason for failure
     */
    public Event getEvent(String username, String event_id) throws DatabaseException {
        // check input
        if(username == null || event_id == null)
            throw new DatabaseException("Null pointer passed to Database.getEvent.");
        if(username.equals("") || event_id.equals(""))
            throw new DatabaseException("Empty string passed to Database.getEvent.");
        if(EventDAO.read(event_id) == null)
            throw new DatabaseException("Event ID passed to Database.getEvent does not exist " +
                    "in database.");
        if(!EventDAO.read(event_id).getDescendant().equals(username))
            throw new DatabaseException("Event ID passed to Database.getEvent is not in the " +
                    "given user's tree.");

        try {
            return EventDAO.readEvent(username, event_id);

        } catch(DatabaseException e) {
            throw new DatabaseException("Database.getEvent failed. " + e.getMessage());
        }
    }

    /**
     * GET FAMILY'S EVENTS:
     * Returns ALL events for ALL family members of a given user.
     *
     * @param username, a non-empty string
     * @throws  DatabaseException detailing reason for failure
     */
    public ArrayList<Event> getFamilyEvents(String username) throws DatabaseException {
        // check input
        if(username == null)
            throw new DatabaseException("Null pointer passed to Database.getFamilyEvents.");
        if(username.equals(""))
            throw new DatabaseException("Empty string passed to Database.getFamilyEvents.");

        try {
            // get events
            ArrayList<Event> events = EventDAO.readFamilyEvents(username);
            if(events == null)
                throw new DatabaseException("There are no events in the database associated " +
                        "with this username.");
            return events;

        } catch(DatabaseException e) {
            throw new DatabaseException("Database.getFamilyEvents failed. " + e.getMessage());
        }
    }

    /**
     * OPEN CONNECTION:
     * Opens a connection to the database and starts a transaction (i.e., disables auto-commit). If
     * the connection is already open, as should only be the case in testing, the method immediately
     * returns.
     *
     * @throws  DatabaseException detailing reason for failure
     */
    public void openConnection() throws DatabaseException {
        // connection open?
        if(connection != null) return;

        try {
            // compose path to DB
            final String CONNECTION_URL = "jdbc:sqlite:" + path_to_database;

            // open DB connection
            connection = DriverManager.getConnection(CONNECTION_URL);

            // start transaction
            connection.setAutoCommit(false);
        }
        catch(Exception e) {
            throw new DatabaseException("Unable to open connection to database. " + e.getMessage());
        }
    }

    /**
     * CLOSE CONNECTION:
     * Checks for successful database operation and then commits all changes if successful; else,
     * rolls back changes since last commit.
     */
    public void closeConnection(boolean commit) {
        try {
            // check flag
            if(commit) {
                connection.commit();
            } else {
                connection.rollback();
            }

        } catch (SQLException e) {
            // do nothing

        } finally {
            try {
                // close connection
                if (connection != null) connection.close();
            } catch(SQLException e) {
                // do nothing
            } finally {
                connection = null;
            }
        }
    }

    /**
     * CREATE TABLES:
     * Drops all tables (if they exist) and then (re)creates them.
     *
     * @throws  DatabaseException detailing reason for failure
     */
    private void createTables() throws DatabaseException {
        // sql statements
        final String CREATE_AUTHTOKEN_TABLE_STMT =
            "CREATE TABLE IF NOT EXISTS AuthTokenTable (" +
                "token      VARCHAR(255)    NOT NULL PRIMARY KEY, " +
                "username   VARCHAR(36)     NOT NULL REFERENCES UserTable(username)" +
            ");";
        final String CREATE_EVENT_TABLE_STMT =
            "CREATE TABLE IF NOT EXISTS EventTable (" +
                "event_id    VARCHAR(36)    NOT NULL PRIMARY KEY, " +
                "descendant  VARCHAR(36)    NOT NULL REFERENCES UserTable(username), " +
                "person_id   VARCHAR(36)    NOT NULL REFERENCES PersonTable(person_id), " +
                "latitude    INTEGER, " +
                "longitude   INTEGER, " +
                "country     VARCHAR(50), " +
                "city        VARCHAR(50), " +
                "event_type  VARCHAR(16)    NOT NULL, " +
                "year        INTEGER" +
            ");";
        final String CREATE_PERSON_TABLE_STMT =
            "CREATE TABLE IF NOT EXISTS PersonTable (" +
                "person_id  VARCHAR(36)     NOT NULL PRIMARY KEY, " +
                "descendant VARCHAR(36)     REFERENCES UserTable(username), " +
                "first_name VARCHAR(35)     NOT NULL, " +
                "last_name  VARCHAR(35)     NOT NULL, " +
                "gender     CHARACTER(1)    NOT NULL, " +
                "father_id  VARCHAR(36)     REFERENCES PersonTable(person_id), " +
                "mother_id  VARCHAR(36)     REFERENCES PersonTable(person_id), " +
                "spouse_id  VARCHAR(36)     REFERENCES PersonTable(person_id)" +
            ");";
        final String CREATE_USER_TABLE_STMT =
            "CREATE TABLE IF NOT EXISTS UserTable (" +
                "username     VARCHAR(16)   NOT NULL PRIMARY KEY, " +
                "password     VARCHAR(16)   NOT NULL, " +
                "email        VARCHAR(255)  NOT NULL, " +
                "first_name   VARCHAR(35)   NOT NULL, " +
                "last_name    VARCHAR(35)   NOT NULL, " +
                "gender       CHARACTER(1)  NOT NULL, " +
                "person_id    VARCHAR(36)   NOT NULL REFERENCES PersonTable(person_id)" +
            ");";

        try {
            // open connection
            openConnection();

            // retrieve statement
            Statement statement = connection.createStatement();

            // create tables
            statement.executeUpdate(CREATE_AUTHTOKEN_TABLE_STMT);
            statement.executeUpdate(CREATE_EVENT_TABLE_STMT);
            statement.executeUpdate(CREATE_PERSON_TABLE_STMT);
            statement.executeUpdate(CREATE_USER_TABLE_STMT);

            // close connection
            closeConnection(true);
        }
        catch(Exception e) {
            if (connection != null) closeConnection(false);
            throw new DatabaseException("createTables() failed. " + e.getMessage());
        }
    }


// MEMBERS
    /**
     * INSTANCE:
     * A singleton Database object. Forgive the messy connection handling in the getter. This class'
     * structure needs some editing.
     */
    private static Database instance;
    public static Database getInstance() throws DatabaseException {
        if(instance == null) {
            try {
                instance = new Database();
                instance.createTables();

            } catch(Exception e) {
                throw new DatabaseException("Database.getInstance failed. " + e.getMessage());
            }
        }

        return instance;
    }

    /**
     * DATABASE NAME:
     * A non-empty string used to reference a valid database file.
     */
    private String path_to_database = "server/src/main/res/db/fms.sqlite";
    public String getDatabaseLocation() { return path_to_database; }
    public void setDatabaseLocation(String dbn) { path_to_database = dbn; }

    /**
     * CONNECTION:
     * A connection to be opened by a service before accessing the database. After performing some
     * operation(s), the service ought to then decide whether to commit or rollback changes made on
     * that connection. Note, that the connection is never held open between two services but is
     * both opened and closed on any given service method call.
     */
    private Connection connection;
    public Connection getConnection() { return connection; }
}
