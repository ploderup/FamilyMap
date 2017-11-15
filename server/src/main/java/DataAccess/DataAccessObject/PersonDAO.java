package DataAccess.DataAccessObject;
import 	java.sql.PreparedStatement;
import 	java.sql.ResultSet;
import 	java.sql.SQLException;
import 	java.sql.Statement;
import  java.util.ArrayList;
import 	DataAccess.Database;
import  DataAccess.DatabaseException;
import  Model.Person;

import static Server.JSONDecoder.*;

public class PersonDAO {
// Class Methods
    /**
     * CREATE:
     * Corresponds with the INSERT statement in SQL. Checks first to confirm that the person given
	 * is unique and that the descendant (user) of that person exists already in the database. Then,
     * the method checks to confirm that all non-null IDs (father, etcetera) exist in the database.
	 * Finally, the person is added to the database.
	 *
	 * @param person, a Person object
     * @throws DatabaseException
     */
    public static void create(Person person) throws DatabaseException {
        // check input
		if(person == null)
            throw new DatabaseException("Method PersonDAO.create passed null pointer");
        if(!person.membersValid())
            throw new DatabaseException("Method PersonDAO.create passed invalid person object.");
        if(read(person.getPersonID()) != null)
            throw new DatabaseException("Method PersonDAO.create passed person whose ID already " +
                                        "exists in database.");
        if(UserDAO.read(person.getDescendant()) == null)
            throw new DatabaseException("Method PersonDAO.create passed person belonging to a " +
                                        "user not currently in the database.");

        // insert into db
        try {
            // declarations
            String sql;
            PreparedStatement statement;

            // prepare statement
            sql = "INSERT INTO " + TABLE_NAME + " VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
            statement = Database.getInstance().getConnection().prepareStatement(sql);

            // fill statement
            statement.setString(1, person.getPersonID());
            statement.setString(2, person.getDescendant());
            statement.setString(3, person.getFirstName());
            statement.setString(4, person.getLastName());
            statement.setString(5, person.getGender());
            statement.setString(6, person.getFatherID());
            statement.setString(7, person.getMotherID());
            statement.setString(8, person.getSpouseID());

            // execute statement
            statement.executeUpdate();

        } catch(SQLException e) {
            throw new DatabaseException("Method PersonDAO.create failed due to SQLException", e);
        }
	}

    /**
     * CREATE (RECURSIVE):
     * Takes an initialized person object not yet stored in the database, stores it in the database,
     * creates parents for it, and recurses on its parents.
     *
     * @param username, a unique non-empty string connected to a user <i>already</i> in the database
     * @param person, an initialized person object not yet in the database
     * @param num_generations, the number of generations to generate
     * @return the number of people added to the database
     * @throws DatabaseException for bad input or exception encountered
     */
    public static int createRecursive(String username, Person person, int num_generations) throws
            DatabaseException {
        // check input
        if(username == null || person == null)
            throw new DatabaseException("Null pointer passed to PersonDAO.createRecursive.");
        if(username.equals(""))
            throw new DatabaseException("Empty string passed to PersonDAO.createRecursive.");
        if(UserDAO.read(username) == null)
            throw new DatabaseException("Username passed to PersonDAO.createRecursive not found " +
                                        "in database.");
        if(num_generations < 0)
            throw new DatabaseException("Negative number of generations passed to " +
                    "PersonDAO.createRecursive.");

        int num_people = 0;

        try {
            // add generations?
            if(num_generations > 0) {
                final String MALE = "m";
                Person father;
                String father_name;
                String father_last_name;
                final String FEMALE = "f";
                Person mother;
                String mother_name;
                String mother_maiden_name;

                // get parents' names
                father_name = getMaleName();
                father_last_name = person.getLastName();
                mother_name = getFemaleName();
                father_last_name = person.getLastName();
                mother_maiden_name = getLastName();

                // create father object
                father = new Person(username, father_name, father_last_name, MALE);
                person.setFatherID(father.getPersonID());

                // create mother object
                mother = new Person(username, mother_name, mother_maiden_name, FEMALE);
                person.setMotherID(mother.getPersonID());

                // recurse on parents
                num_people += createRecursive(username, father, num_generations-1);
                num_people += createRecursive(username, mother, num_generations-1);
            }

            // add person to db
            create(person);
            num_people++;

        } catch(Exception e) {
            throw new DatabaseException("PersonDAO.createRecursive failed. " + e.getMessage());
        }

        return num_people;
    }

    /**
     * READ (INTERNAL):
     * Corresponds with the SELECT statement in SQL. Searches the database for a single person with
     * the given person ID.
     *
     * @param person_id, non-empty string
     * @throws DatabaseException
     * @return Person object if found, null otherwise
     * @see Person
     */
    public static Person read(String person_id) throws DatabaseException {
        // check input
        if(person_id == null)
            throw new DatabaseException("Method PersonDAO.read passed null pointer.");
        if(person_id.equals(""))
            throw new DatabaseException("Method PersonDAO.read passed empty string.");

        try {
            Person person = null;
            String sql;
            PreparedStatement statement;
            ResultSet result_set;

            // prepare statement
            sql = "SELECT * FROM " + TABLE_NAME + " WHERE person_id = ?;";
            statement = Database.getInstance().getConnection().prepareStatement(sql);

            // fill statement
            statement.setString(1, person_id);

            // execute sql
            result_set = statement.executeQuery();

            // person found?
            if(result_set.next()) {
                // construct person
                person = new Person(result_set.getString("person_id"),
                        result_set.getString("descendant"),
                        result_set.getString("first_name"),
                        result_set.getString("last_name"),
                        result_set.getString("gender"),
                        result_set.getString("mother_id"),
                        result_set.getString("father_id"),
                        result_set.getString("spouse_id"));

                // others found?
                if(result_set.next())
                    throw new DatabaseException("PersonDAO.readPerson returned multiple people.");
            }

            return person;

        } catch(SQLException e) {
            throw new DatabaseException("PersonDAO.readPerson failed.", e);
        }
    }

    /**
     * READ FAMILY:
     * Corresponds with the SELECT statement in SQL. Searches PersonTable for all people whose
     * username value is equal to the given username.
     *
     * @param username, non-empty string
     * @throws DatabaseException
     * @return ArrayList of person objects if found, null otherwise
     * @see Person
     */
    public static ArrayList<Person> readFamily(String username) throws DatabaseException {
        // check input
        if(username == null)
            throw new DatabaseException("Method PersonDAO.read passed null pointer");
        if(username.equals(""))
            throw new DatabaseException("Method PersonDAO.read passed empty string.");

        try {
            ArrayList<Person> family = null;
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
                if(family == null) family = new ArrayList<>();
                Person temp;

                // construct person
                temp = new Person(result_set.getString("person_id"),
                                  result_set.getString("descendant"),
                                  result_set.getString("first_name"),
                                  result_set.getString("last_name"),
                                  result_set.getString("gender"),
                                  result_set.getString("father_id"),
                                  result_set.getString("mother_id"),
                                  result_set.getString("spouse_id"));

                // add to array
                family.add(temp);
            }

            return family;

        } catch(SQLException e) {
            throw new DatabaseException("PersonDAO.readFamily failed.", e);
        }
    }

    /**
     * READ PERSON:
     * Corresponds with the SELECT statement in SQL. Searches PersonTable for the person with the
     * given ID within some user's tree.
     *
     * @param username, non-empty string
     * @param person_id, non-empty string
     * @throws DatabaseException
     * @return Person object if found, null otherwise
     * @see Person
     */
    public static Person readPerson(String username, String person_id) throws DatabaseException {
        // check input
        if(username == null || person_id == null)
            throw new DatabaseException("Method PersonDAO.read passed null pointer.");
        if(username.equals("") || person_id.equals(""))
            throw new DatabaseException("Method PersonDAO.read passed empty string.");

        try {
            Person person = null;
            String sql;
            PreparedStatement statement;
            ResultSet result_set;

            // prepare statement
            sql = "SELECT * FROM " + TABLE_NAME + " WHERE descendant = ? AND person_id = ?;";
            statement = Database.getInstance().getConnection().prepareStatement(sql);

            // fill statement
            statement.setString(1, username);
            statement.setString(2, person_id);

            // execute sql
            result_set = statement.executeQuery();

            // person found?
            if(result_set.next()) {
                // construct person
                person = new Person(result_set.getString("person_id"),
                                    result_set.getString("descendant"),
                                    result_set.getString("first_name"),
                                    result_set.getString("last_name"),
                                    result_set.getString("gender"),
                                    result_set.getString("mother_id"),
                                    result_set.getString("father_id"),
                                    result_set.getString("spouse_id"));

                // others found?
                if(result_set.next())
                    throw new DatabaseException("PersonDAO.readPerson returned multiple people.");
            }

            return person;

        } catch(SQLException e) {
            throw new DatabaseException("PersonDAO.readPerson failed.", e);
        }
    }

    /**
     * UPDATE:
     * Corresponds with the UPDATE statement in SQL. Checks whether the person object provided has
     * a non-empty ID, and whether the descendant, father, mother and spouse associated with the
     * person exist in the PersonTable. If so, the person in the Database with the same ID as the
     * one given is updated. CODE COMMENTED AS NEVER USER FOR PROJECT.
     *
     * @param person, a person object
     * @throws DatabaseException
     * @deprecated
     */
    public static void update(Person person) throws DatabaseException {}

    /**
     * DELETE:
     * Corresponds with the DELETE statement in SQL. Searches PersonTable for the given person ID.
     * If found, the person is deleted; else, no action is taken.
     * 
     * @param person_id, a non-empty string denoting a person
     * @throws DatabaseException
     */
    public static void delete(String person_id) throws DatabaseException {
        // check input
        if(person_id == null)
            throw new DatabaseException("Method PersonDAO.delete passed null pointer.");
        if(person_id.equals(""))
            throw new DatabaseException("Method PersonDAO.delete passed empty string.");

        // delete person
        try {
            // declarations
            String sql;
            PreparedStatement statement;

            // prepare statement
            sql = "DELETE FROM " + TABLE_NAME + " WHERE person_id = ?;";
            statement = Database.getInstance().getConnection().prepareStatement(sql);

            // fill statement
            statement.setString(1, person_id);

            // execute statement
            statement.executeUpdate();

        } catch(SQLException e) {
            throw new DatabaseException("Method PersonDAO.delete failed.", e);
        }
    }

	/**
	 * DELETE ALL:
	 * Corresponds with the DELETE statement in SQL. Deletes all data from PersonTable.
	 *
	 * @throws Exception detailing reason for failure
	 */
	public static void deleteAll() throws Exception {
		try {
			// create statement
			Statement statement = Database.getInstance().getConnection().createStatement();

			// execute statement
			statement.executeUpdate("DELETE FROM " + TABLE_NAME + ";");

		}
		catch(SQLException e) {
			throw new Exception("Unable to delete all data from PersonTable in database.");
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
    private static final String TABLE_NAME = "PersonTable";
}

