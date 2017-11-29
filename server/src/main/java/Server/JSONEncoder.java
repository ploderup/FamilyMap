package Server;

import com.google.gson.GsonBuilder;

public class JSONEncoder {
// METHODS
    /**
     * ENCODE RESPONSE:
     * Encodes the given result into JSON format.
     *
     * @param result, a ServiceResult object
     * @see Facade.Result.ClearResult;
     * @see Facade.Result.EventResult;
     * @see Facade.Result.FillResult;
     * @see Facade.Result.LoadResult;
     * @see Facade.Result.LoginResult;
     * @see Facade.Result.PersonResult;
     * @see Facade.Result.RegisterResult;
     */
    public static String encodeObject(Object result) {
        // convert result to JSON
        return new GsonBuilder()
                .setPrettyPrinting()
                .create()
                .toJson(result);
    }
}
