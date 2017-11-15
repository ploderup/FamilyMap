package DataAccess.DataAccessObject;
import  java.sql.PreparedStatement;
import  java.sql.ResultSet;
import  java.sql.SQLException;
import  java.sql.Statement;
import  DataAccess.Database;
import  DataAccess.DatabaseException;
import	Model.User;

public class UserDAO {
// Class Methods
    /**
     * CREATE:
     * Corresponds with the INSERT statement in SQL. Checks to confirm that given user object's
     * username is unique and then adds user to database.
	 *
	 * @param user a User object
	 * @throws DatabaseException detailing reason for failure
     */
    public static void create(User user) throws DatabaseException {
        // check input
        if(user == null)
            throw new DatabaseException("Method UserDAO.create passed null pointer");
        if(!user.membersValid())
            throw new DatabaseException("Method UserDAO.create passed invalid user object");
        if(read(user.getUsername()) != null)
            throw new DatabaseException("Method UserDAO.create passed username which already " +
                                        "exists in database.");

        // insert into db
        try {
            // declarations
            String sql;
            PreparedStatement statement;

            // prepare statement
            sql = "INSERT INTO " + TABLE_NAME + " VALUES (?, ?, ?, ?, ?, ?, ?);";
            statement = Database.getInstance().getConnection().prepareStatement(sql);

            // fill statement
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getFirstName());
            statement.setString(5, user.getLastName());
            statement.setString(6, user.getGender());
            statement.setString(7, user.getPersonID());

            // execute statement
            statement.executeUpdate();

        } catch(SQLException e) {
            throw new DatabaseException("UserDAO.create failed. " + e.getMessage());
        }
	}

    /**
     * READ (INTERNAL):
     * Corresponds with the SELECT statement in SQL. Searches UserTable for the user corresponding
	 * to the given username.
	 *
	 * @param username, non-empty string
	 * @return User object if found, null otherwise
	 * @see User
     */
    public static User read(String username) throws DatabaseException {
        // check input
        if(username == null)
            throw new DatabaseException("Method UserDAO.read passed null pointer");
        if(username.equals(""))
            throw new DatabaseException("Method UserDAO.read passed empty string.");

        // initialize user (will remain null if never found)
        User user = null;

        try {
            // declarations
			String sql;
            PreparedStatement statement;
            ResultSet result_set;

            // prepare statement
            sql = "SELECT * FROM " + TABLE_NAME + " WHERE username = ?;";
            statement = Database.getInstance().getConnection().prepareStatement(sql);

            // insert username
            statement.setString(1, username);

            // execute sql
            result_set = statement.executeQuery();

            // user found?
            if(result_set.next()) {
                // construct user
                user = new User(result_set.getString("username"),
                                result_set.getString("password"),
                                result_set.getString("email"),
                                result_set.getString("first_name"),
                                result_set.getString("last_name"),
                                result_set.getString("gender"),
                                result_set.getString("person_id"));

                // others found?
                if(result_set.next()) throw new DatabaseException("Method UserDAO.read() " +
                                                                  "returned multiple users (UN: " +
                                                                  username + ").");
            }

        } catch(SQLException e) {
            throw new DatabaseException("Unable to read on user (UN: " + username + "): " +
                                        e.getMessage());
        }

        return user;
	}

    /**
     * UPDATE:
     * Corresponds with the UPDATE statement in SQL. Checks whether the username of the user object
     * provided exists. If it does, values in the UserTable and PersonTable are then updated.
     *
     * Note, this method currently does nothing as I don't think its functionality is needed for
     * use with this lab for CS240.
     *
     * @param user, a user object
     * @deprecated
     */
    public static void update(User user) {}

    /**
     * DELETE:
     * Corresponds with the DELETE statement in SQL. Searches UserTable for the given username. If
     * found, the user is deleted; else, no action is taken.
	 *
     * @param username, a non-empty string denoting a user
     * @throws DatabaseException
     */
    public static void delete(String username) throws DatabaseException{
        // check input
        if(username == null)
            throw new DatabaseException("Method AuthTokenDAO.delete passed null pointer.");
        if(username.equals(""))
            throw new DatabaseException("Method AuthTokenDAO.delete passed empty string.");

        // delete username
        try {
            // declarations
            String sql;
            PreparedStatement statement;

            // prepare statement
            sql = "DELETE FROM " + TABLE_NAME + " WHERE username = ?;";
            statement = Database.getInstance().getConnection().prepareStatement(sql);

            // fill statement
            statement.setString(1, username);

            // execute statement
            statement.executeUpdate();

        } catch(SQLException e) {
            throw new DatabaseException("Method AuthTokenDAO.delete failed.", e);
        }
    }

	/**
	 * DELETE ALL:
	 * Corresponds with the DELETE statement in SQL. Deletes all data from UserTable.
	 *
	 * @throws DatabaseException	detailing reason for failure
	 */
	public static void deleteAll() throws DatabaseException {
		try {
			// create statement
			Statement statement = Database.getInstance().getConnection().createStatement();

			// execute statement
			statement.executeUpdate("DELETE FROM " + TABLE_NAME);

		}
		catch(SQLException e) {
			throw new DatabaseException("Unable to delete all data from UserTable in database.");
		}
	}


// Class Members
    /**
     * TABLE NAME:
     * The name of the table associated with this DAO.
     */
    private static final String TABLE_NAME = "UserTable";
}
