package Server;

import com.google.gson.Gson;
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
        // create GSON object
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // convert result to JSON
        return gson.toJson(result);
    }
}
