package com.example.ploderup.communication;
import android.util.Log;

import com.example.ploderup.model.FamilyMap;
import com.example.ploderup.model.Filter;
import com.example.ploderup.model.Search;
import com.example.ploderup.model.Settings;
import com.example.ploderup.model.UserInfo;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import Facade.Request.LoginRequest;
import Facade.Request.RegisterRequest;
import Facade.Result.EventResult;
import Facade.Result.LoginResult;
import Facade.Result.PersonResult;
import Facade.Result.RegisterResult;
import Model.Person;

public class ServerProxy {
// SINGLETON STUFF
    private static ServerProxy mInstance = new ServerProxy();
    public static ServerProxy getInstance() { return mInstance; }
    private ServerProxy() {}


// MEMBERS
    private final String TAG = "ServerProxy";
    private final String HTTP_GET = "GET";
    private final String HTTP_POST = "POST";

    private FamilyMap mFamilyMap = FamilyMap.getInstance();
    private Filter mFilter = Filter.getInstance();
    private Search mSearch = Search.getInstance();
    private Settings mSettings = Settings.getInstance();
    private UserInfo mUserInfo = UserInfo.getInstance();

    private String mAuthToken;
    String getAuthToken() { return mAuthToken; }
    void setAuthToken(String mAuthToken) { this.mAuthToken = mAuthToken; }

    private String mRootPersonID;
    String getRootPersonID() { return mRootPersonID; }
    void setRootPersonID(String mRootPersonID) { this.mRootPersonID = mRootPersonID; }

// METHODS
    /**
     * LOGIN USER
     * Attempts to log the user in at a given server's database, using given credentials.
     *
     * @param url_prefix a non-empty string specifying the hostname and port number of a valid
     *                   FamilyMap server
     * @param username   a non-empty string
     * @param password   a non-empty string
     * @return whether or not the login was successful
     */
    public LoginResult loginUser(String url_prefix, String username, String password) {
        String request_body;

        // Create body for HTTP post request
        request_body = new GsonBuilder()
                .setPrettyPrinting()
                .create()
                .toJson(new LoginRequest(username, password));

        // Post to the server
        return (LoginResult) connectToServer(url_prefix, "/user/login", HTTP_POST, request_body,
                null, LoginResult.class);
    }

    /**
     * REGISTER USER
     * Attempts to register the user at a given server's database, using given credentials.
     *
     * @param url_prefix a non-empty string specifying the hostname and port number of a valid
     *                   FamilyMap server
     * @param username   a non-empty string
     * @param password   a non-empty string
     * @param first_name a non-empty string
     * @param last_name  a non-empty string
     * @param email      a valid email address
     * @param gender     "m" or "f"
     * @return whether the registration was successful or not
     */
    public RegisterResult registerUser(String url_prefix, String username, String password,
                                       String first_name, String last_name, String email,
                                       String gender) {
        Log.d(TAG, "registerUser(" + url_prefix + ", " + username + ", " + password + ", " +
                first_name + ", " + last_name + ", " + email + ", " + gender + ");");
        String request_body;

        // Create body for HTTP post request
        request_body = new GsonBuilder()
                .setPrettyPrinting()
                .create()
                .toJson(new RegisterRequest(username, password, email, first_name, last_name,
                        gender));
        Log.d(TAG, request_body);

        // Post to the server
        return (RegisterResult) connectToServer(url_prefix, "/user/register", HTTP_POST,
                request_body, null, RegisterResult.class);
    }

    /**
     * LOGOUT USER
     * [Explanation]
     */
    public void logoutUser() {
        this.mAuthToken = null;
        this.mRootPersonID = null;

        mFamilyMap.setRootPersonID(null);
        mFamilyMap.setAllEvents(null);
        mFamilyMap.setAllPeople(null);
        mFamilyMap.setDataSyncDone(true);

        mFilter.disableAllFilters();

        mSearch.resetSearchResults();

        mSettings.setLoggedIn(false);
        mSettings.resetAllSettings();

        mUserInfo.resetAllFields();
    }

    /**
     * GET FAMILY TREE
     * Attempts to retrieve a family tree from the FamilyMap database. Note, that this function
     * should only be called by either registerUser or loginUser after having set the username and
     * authentication values in the data cache.
     *
     * @param url_prefix a non-empty string specifying the hostname and port number of a valid
     *                   FamilyMap server
     * @return the result of the connection to the server
     */
    public PersonResult getFamilyTree(String url_prefix) {
        Log.d(TAG, "getFamilyTree(" + url_prefix + ");");

        // Get from the server
        return (PersonResult) connectToServer(url_prefix, "/person", HTTP_GET, null, mAuthToken,
                PersonResult.class);
    }

    /**
     * GET PERSON
     * Attempts to retrieve a single person from the FamilyMap database.
     *
     * @param url_prefix a non-empty string specifying the hostname and port number of a valid
     *                   FamilyMap server
     * @param person_id a non-empty string
     * @return the result of the connection to the server
     */
    public PersonResult getPerson(String url_prefix, String person_id) {
        Log.d(TAG, "getPerson(" + url_prefix + ");");

        // Get from the server
        return (PersonResult) connectToServer(url_prefix, ("/person/" + person_id), HTTP_GET, null,
                mAuthToken, PersonResult.class);
    }

    /**
     * GET FAMILY EVENTS
     * Attempt to retrieve all events associated with a given user from the FamilyMap database.
     *
     * @param url_prefix a non-empty string specifying the hostname and port number of a valid
     *                   FamilyMap server
     * @return the result of the connection to the server
     */
    public EventResult getFamilyEvents(String url_prefix) {
        Log.d(TAG, "getFamilyEvents(" + url_prefix + ");");

        return (EventResult) connectToServer(url_prefix, "/event", HTTP_GET, null, mAuthToken,
                EventResult.class);
    }

    /**
     * POST TO SERVER
     * Makes a connection to a FamilyMap Server and returns its result.
     *
     * @param url_prefix   the part of the URL containing the FamilyMap Server's host name and port
     *                     number (e.g. "192.168.0.12:8080")
     * @param api_url      the FamilyMap Server API part of the URL (e.g. "/user/login")
     * @param request_method the method of the request to be made (i.e., POST, or GET)
     * @param auth_token   a valid authentication token string (set to null if not required for post
     *                     operation)
     * @param request_body the request to be sent to the server (in JSON format); can be null
     * @param result_type the type of the result to be returned (e.g., PersonResult)
     * @return A [Service]Result object corresponding to the post performed to the server
     */
    private Object connectToServer(String url_prefix, String api_url, String request_method,
                                   String request_body, String auth_token, Class<?> result_type) {
        Log.d(TAG, "ServerProxy.connectToServer(" + url_prefix + ", " + api_url + ", " +
                request_method + ", " + request_body + ", " + auth_token + ", " + result_type +
                ");");
        Object connection_result = null;

        try {
            HttpURLConnection url_connection;

            // Create a new HTTP connection
            url_connection = (HttpURLConnection) new URL(url_prefix + api_url).openConnection();

            // Setup the connection
            url_connection.setRequestMethod(request_method);
            if (request_method.equals(HTTP_GET)) {
                url_connection.setDoInput(true);
                url_connection.setDoOutput(false);
            } else {
                url_connection.setDoInput(true);
                url_connection.setDoOutput(true);
            }

            // Is there an authentication token to add?
            if (auth_token != null) url_connection.addRequestProperty("Authorization", auth_token);

            // Is there any body data to add?
            if (request_body != null)
                stringToStream(request_body, url_connection.getOutputStream());

            // Make the connection
            url_connection.connect();

            // Was the connection successful?
            if (url_connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Convert the response from JSON into a result object
                InputStreamReader input_stream_reader =
                        new InputStreamReader(url_connection.getInputStream());
                connection_result = new GsonBuilder()
                        .setPrettyPrinting()
                        .create()
                        .fromJson(input_stream_reader, result_type);
                input_stream_reader.close();

            } else {
                Log.e(TAG, "Code other than HTTP_OK returned to connectToServer(): " +
                        url_connection.getResponseCode());
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException encountered at connectToServer()");
        } catch (IOException e) {
            Log.e(TAG, "IOException encountered at connectToServer()");
            Log.e(TAG, e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception encountered at connectToServer()");
            Log.e(TAG, e.getMessage());
        }

        return connection_result;
    }

    /**
     * STRING TO STREAM:
     * Writes a string into an output stream.
     *
     * @param str, a non-empty string
     * @param os,  an instantiated output stream object
     */
    private void stringToStream(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}