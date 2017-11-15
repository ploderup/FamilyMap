package DataAccess;

public class DatabaseException extends Exception {
    public DatabaseException(String s) {
        super(s);
    }

    public DatabaseException(String s, Throwable t) {
        super(s, t);
    }
}
