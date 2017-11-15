package DataAccess.DataAccessObject;
import 	java.sql.PreparedStatement;
import  java.sql.ResultSet;
import 	java.sql.SQLException;
import 	java.sql.Statement;
import 	DataAccess.Database;
import 	DataAccess.DatabaseException;
import	Model.AuthToken;

public class AuthTokenDAO {
// Class Methods
    /**
     * CREATE:
     * Corresponds with the INSERT statement in SQL. Checks to confirm that the token associated
     * with the authentication token object is unique and also as to whether the username associated
     * with the token exists in the database. Finally, the token is then added. Note that the
     * potentially not-null 'person_id' member of the AuthToken class is ignored here, as it is
     * never stored in the database and used only for returns to the LoginService.
	 *
	 * @param auth_token, an AuthToken object
     */
    public static void create(AuthToken auth_token) throws DatabaseException {
        // check input
        if(auth_token == null)
            throw new DatabaseException("Method AuthTokenDAO.create passed null pointer");
        if(!auth_token.membersValid())
            throw new DatabaseException("Method AuthTokenDAO.create passed invalid " +
                                        "authentication token object.");
        if(read(auth_token.getToken()) != null)
            throw new DatabaseException("Method AuthTokenDAO.create passed token which already " +
                                        "exists in database.");
        if(UserDAO.read(auth_token.getUsername()) == null)
            throw new DatabaseException("Method AuthTokenDAO.create passed authentication token " +
                                        "associated with a user not currently in the database.");

        // insert into db
        try {
            // declarations
            String sql;
            PreparedStatement statement;

            // prepare statement
            sql = "INSERT INTO " + TABLE_NAME + " VALUES (?, ?);";
            statement = Database.getInstance().getConnection().prepareStatement(sql);

            // fill statement
            statement.setString(1, auth_token.getToken());
            statement.setString(2, auth_token.getUsername());

            // execute statement
            statement.executeUpdate();

        } catch(SQLException e) {
            throw new DatabaseException("Method AuthTokenDAO.create failed.", e);
        }
	}

    /**
     * READ (INTERNAL):
     * Corresponds with the SELECT statement in SQL. Searches AuthTokenTable for the authorization
	 * token given; returns a boolean denoting its result.
	 * 
	 * @param token, non-empty string
     * @throws DatabaseException
	 * @return has_token, token object if found, null otherwise
     */
    public static AuthToken read(String token) throws DatabaseException {
        // check input
		if(token == null)
            throw new DatabaseException("Method AuthTokenDAO.read passed null pointer.");
        if(token.equals(""))
            throw new DatabaseException("Method AuthTokenDAO.read passed empty string.");

		// initialize auth_token (will remain null if never found)
		AuthToken auth_token = null;

		try {
			// declarations
			String sql;
			PreparedStatement statement;
			ResultSet result_set;

			// prepare statement
			sql = "SELECT * FROM " + TABLE_NAME + " WHERE token = ?;";
			statement = Database.getInstance().getConnection().prepareStatement(sql);

			// insert token
			statement.setString(1, token);

			// execute sql
			result_set = statement.executeQuery();

			// auth token found?
			if(result_set.next()) {
				// construct auth token
				auth_token = new AuthToken(result_set.getString("token"),
                                           result_set.getString("username"),
                                           null);

				// others found?
				if(result_set.next()) throw new DatabaseException("Method AuthTokenDAO.read() " +
						                                          "returned multiple tokens.");
			}

		} catch(SQLException e) {
			throw new DatabaseException("Unable to read on token (" + token + "): " +
                                        e.getMessage());
		}

		return auth_token;
	}

    /**
     * DELETE:
     * Corresponds with the DELETE statement in SQL. Searches AuthTokenTable for the given
	 * authorization key. If found, the authorization token is deleted; else, no action is taken.
	 * 
     * @param token, a non-empty string corresponding to an authorization key
     * @throws DatabaseException
     */
    public static void delete(String token) throws DatabaseException {
        // check input
        if(token == null)
            throw new DatabaseException("Method AuthTokenDAO.delete passed null pointer.");
        if(token.equals(""))
            throw new DatabaseException("Method AuthTokenDAO.delete passed empty string.");

        // delete token
        try {
            // declarations
            String sql;
            PreparedStatement statement;

            // prepare statement
            sql = "DELETE FROM " + TABLE_NAME + " WHERE token = ?;";
            statement = Database.getInstance().getConnection().prepareStatement(sql);

            // fill statement
            statement.setString(1, token);

            // execute statement
            statement.executeUpdate();

        } catch(SQLException e) {
            throw new DatabaseException("Method AuthTokenDAO.delete failed.", e);
        }
    }

	/**
	 * DELETE ALL:
	 * Corresponds with the DELETE statement in SQL. Deletes all data from AuthTokenTable.
	 *
	 * @throws DatabaseException detailing reason for failure
	 */
	public static void deleteAll() throws DatabaseException {
		try {
			// create statement
			Statement statement = Database.getInstance().getConnection().createStatement();

			// execute statement
			statement.executeUpdate("DELETE FROM " + TABLE_NAME);

		}
		catch(SQLException e) {
			throw new DatabaseException("Method AuthTokenDAO.deleteAll failed due to SQLException.",
                                        e);
		}
	}

// Class Members
    /**
     * TABLE NAME:
     * The name of the table associated with this DAO.
     */
    private static final String TABLE_NAME = "AuthTokenTable";
}
