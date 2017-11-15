package Server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import Facade.Request.*;
import Facade.Result.*;
import Model.Location;

import static java.lang.Math.random;

public class JSONDecoder {
// METHODS
    /**
     * DECODE REQUEST:
     * Decodes the given result from JSON format.
     *
     * @param json, a valid json string
     * @param request_type, some request object class
     * @see LoadRequest
     * @see LoginRequest
     * @see RegisterRequest
     */
    public static Object decodeRequest(String json, Class<?> request_type) {
        // create GSON object
        Gson gson = new Gson();

        // convert JSON to result
        return gson.fromJson(json, request_type);
    }

    /**
     * ENCODE RESPONSE:
     * Encodes the given result into JSON format.
     *
     * @param result, a ServiceResult object
     * @see ClearResult;
     * @see EventResult;
     * @see FillResult;
     * @see LoadResult;
     * @see LoginResult;
     * @see PersonResult;
     * @see RegisterResult;
     */
    public static String encodeResult(Object result) {
        // create GSON object
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // convert result to JSON
        return gson.toJson(result);
    }

    /**
     * GET LOCATION:
     * Retrieves a random location.
     *
     * @return a location object
     * @see Location
     */
    public static Location getLocation() throws Exception {
        // check for instantiation
        if(locations == null) instantiateLocations();

        // get male name
        if(locations.getNumLocations() > 0) {
            return locations.getRandomLocation();

        // no names
        } else {
            throw new Exception("JSONDecoder.getLocation failed. No locations in database.");
        }
    }

    /**
     * GET MALE NAME:
     * Retrieves a random male name.
     *
     * @return a non-empty string
     */
    public static String getMaleName() throws Exception {
        // check for instantiation
        if(male_names == null) instantiateMaleNames();

        // get male name
        if(male_names.getNumNames() > 0) {
            return male_names.getRandomName();

        // no names
        } else {
            throw new Exception("JSONDecoder.getMaleName failed. No male names in database.");
        }
    }

    /**
     * GET FEMALE NAME:
     * Retrieves a random female name.
     *
     * @return a non-empty string
     */
    public static String getFemaleName() throws Exception {
        // check for instantiation
        if(female_names == null) instantiateFemaleNames();

        // get female name
        if(female_names.getNumNames() > 0) {
            return female_names.getRandomName();

            // no names
        } else {
            throw new Exception("JSONDecoder.getFemaleName failed. No female names in database.");
        }
    }

    /**
     * GET LAST NAME:
     * Retrieves a random last name.
     *
     * @return a non-empty string
     */
    public static String getLastName() throws Exception {
        // check for instantiation
        if(last_names == null) instantiateLastNames();

        // get last name
        if(last_names.getNumNames() > 0) {
            return last_names.getRandomName();

        // no names
        } else {
            throw new Exception("JSONDecoder.getLastName failed. No last names in database.");
        }
    }

// MEMBERS
    /**
     * JSON LOCATION:
     * The location of the JSON files, relative to this package.
     */
    private static final String JSON_PATH = "server/src/main/res/json/";

    /**
     * LOCATIONS:
     * An array of not-null Location objects.
     */
    private static Locations locations;
    private static void instantiateLocations() throws FileNotFoundException {
        final String file_location = JSON_PATH + "locations.json";

        try {
            Gson gson = new Gson();
            locations = gson.fromJson(new FileReader(file_location), Locations.class);

        } catch(FileNotFoundException e) {
            throw new FileNotFoundException("Unable to locate JSON file at " + file_location + ".");
        }
    }

    /**
     * MALE NAMES:
     * An array of non-empty strings.
     */
    private static Names male_names;
    private static void instantiateMaleNames() throws FileNotFoundException {
        final String file_location = JSON_PATH + "mnames.json";

        try {
            Gson gson = new Gson();
            male_names = gson.fromJson(new FileReader(file_location), Names.class);

        } catch(FileNotFoundException e) {
            throw new FileNotFoundException("Unable to locate JSON file at " + file_location + ".");
        }
    }

    /**
     * FEMALE NAMES:
     * An array of non-empty strings.
     */
    private static Names female_names;
    private static void instantiateFemaleNames() throws FileNotFoundException {
        final String file_location = JSON_PATH + "fnames.json";

        try {
            Gson gson = new Gson();
            female_names = gson.fromJson(new FileReader(file_location), Names.class);

        } catch(FileNotFoundException e) {
            throw new FileNotFoundException("Unable to locate JSON file at " + file_location + ".");
        }
    }

    /**
     * LAST NAMES:
     * An array of non-empty strings.
     */
    private static Names last_names;
    private static void instantiateLastNames() throws FileNotFoundException {
        final String file_location = JSON_PATH + "lnames.json";

        try {
            Gson gson = new Gson();
            last_names = gson.fromJson(new FileReader(file_location), Names.class);

        } catch(FileNotFoundException e) {
            throw new FileNotFoundException("Unable to locate JSON file at " + file_location + ".");
        }
    }


// CLASSES
    /**
     * LOCATIONS:
     * A storage class used for importing of JSON data.
     */
    private static class Locations {
        private ArrayList<Location> locations;
        int getNumLocations() { return locations.size(); }
        Location getRandomLocation() {
            int index = (int)(random()*locations.size());
            return locations.get(index);
        }
    }

    /**
     * NAMES:
     * A storage class used for importing of JSON data.
     */
    private static class Names {
        private ArrayList<String> names;
        int getNumNames() { return names.size(); }
        String getRandomName() {
            int index = (int)(random()*names.size());
            return names.get(index);
        }
    }
}
