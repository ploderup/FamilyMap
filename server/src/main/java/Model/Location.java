package Model;

/**
 * LOCATION:
 * Used to store locations imported from "locations.json" (located in resource folder).
 */
public class Location {
// CONSTRUCTORS
    /**
     * DEFAULT CONSTRUCTOR:
     * Constructs an Location object.
     */
    public Location() {}

    /**
     * Primary Constructor:
     * Constructs a Location object.
     */
    public Location(String co, String ci, double la, double lo) {
        country = co;
        city = ci;
        latitude = la;
        longitude = lo;
    }

// MEMBERS
    /**
     * Country:
     * A non-empty string.
     */
    private String country;
    public String getCountry() { return country; }

    /**
     * City:
     * A non-empty string.
     */
    private String city;
    public String getCity() { return city; }

    /**
     * Latitude:
     * An integer representing a valid latitude.
     */
    private double latitude;
    public double getLatitude() { return latitude; }

    /**
     * Longitude:
     * An integer representing a valid longitude.
     */
    private double longitude;
    public double getLongitude() { return longitude; }
}