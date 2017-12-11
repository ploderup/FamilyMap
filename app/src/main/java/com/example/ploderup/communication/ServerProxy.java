package com.example.ploderup.communication;
import android.util.Log;

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
import Facade.Result.LoginResult;
import Facade.Result.PersonResult;
import Facade.Result.RegisterResult;
import Model.Person;

public class ServerProxy {
    // MEMBERS
    private static final String TAG = "ServerProxy";
    private static final String HTTP_GET = "GET";
    private static final String HTTP_POST = "POST";

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
    public static LoginResult loginUser(String url_prefix, String username, String password) {
        Log.d(TAG, "ServerProxy(" + url_prefix + ", " + username + ", " + password + ");");
        String request_body;

        // Create body for HTTP post request
        request_body = new GsonBuilder()
                .setPrettyPrinting()
                .create()
                .toJson(new LoginRequest(username, password));
        Log.d(TAG, request_body);

        // Post to the server
        return (LoginResult) connectToServer(url_prefix, "/user/login", HTTP_POST, null,
                request_body, LoginResult.class);
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
    public static RegisterResult registerUser(String url_prefix, String username, String password,
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
        return (RegisterResult) connectToServer(url_prefix, "/user/register", HTTP_POST, null,
                request_body, RegisterResult.class);
    }

    /**
     * LOGOUT USER
     * [Explanation]
     */
//    public static void logoutUser(String url_prefix,)

    /**
     * GET FAMILY TREE
     * Attempts to retrieve a family tree from the FamilyMap database. Note, that this function
     * should only be called by either registerUser or loginUser after having set the username and
     * authentication values in the data cache.
     *
     * @param url_prefix a non-empty string specifying the hostname and port number of a valid
     *                   FamilyMap server
     * @return the result
     */
    public static PersonResult getFamilyTree(String url_prefix) {
        Log.d(TAG, "getFamilyTree(" + url_prefix + ");");

        // Get from the server
        return (PersonResult) connectToServer(url_prefix, "/person", HTTP_GET,
                DataCache.getAuthToken(), null, PersonResult.class);
    }

    /**
     * POST TO SERVER
     * Makes a connection to a FamilyMap Server and returns its result.
     *
     * @param url_prefix   the part of the URL containing the FamilyMap Server's host name and port
     *                     number (e.g. "192.168.0.12:8080")
     * @param api_url      the FamilyMap Server API part of the URL (e.g. "/user/login")
     * @param auth_token   a valid authentication token string (set to null if not required for post
     *                     operation)
     * @param request_body the request to be sent to the server (in JSON format); can be null
     * @return A [Service]Result object corresponding to the post performed to the server
     */
    private static Object connectToServer(String url_prefix, String api_url, String request_method,
                                          String auth_token, String request_body,
                                          Class<?> result_type) {
        Log.d(TAG, "ServerProxy.connectToServer(" + url_prefix + ", " + api_url + ", " + auth_token +
                ", " + request_body + ", " + result_type + ");");
        Object connection_result = null;

        try {
            HttpURLConnection url_connection;

            // Create a new HTTP connection
            Log.d(TAG, "About to create a new HTTP connection");
            url_connection = (HttpURLConnection) new URL(url_prefix + api_url).openConnection();

            // Setup the connection
            Log.d(TAG, "Setting up the connection");
            url_connection.setRequestMethod(request_method);
            if (request_method.equals(HTTP_GET)) {
                url_connection.setDoInput(true);
                url_connection.setDoOutput(false);
            } else {
                url_connection.setDoInput(true);
                url_connection.setDoOutput(true);
            }

            // Is there an authentication token to add?
            Log.d(TAG, "Checking for authentication token");
            if (auth_token != null) url_connection.addRequestProperty("Authorization", auth_token);

            // Is there any body data to add?
            Log.d(TAG, "Checking for body data to add");
            if (request_body != null)
                stringToStream(request_body, url_connection.getOutputStream());
            Log.d(TAG, "Done w/ body data");

            // Make the connection
            Log.d(TAG, "Opening the connection");
            url_connection.connect();
            Log.d(TAG, "The connection to the server is now open.");

            // Was the connection successful?
            Log.d(TAG, "What was the connection code?");
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
            Log.e(TAG, "MalformedURLException encountered. See connectToServer().");
        } catch (IOException e) {
            Log.e(TAG, "IOException encountered. See connectToServer().");
            Log.e(TAG, e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception encountered. See connectToServer().");
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
    private static void stringToStream(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

    public static class DataCache {
    // MEMBERS
        /**
         * URL PREFIX
         * The host name and port number of the FamilyMap server (e.g., 192.168.0.0:8080).
         */
        private static String mURLPrefix;

        public static String getURLPrefix() {
            return mURLPrefix;
        }

        public static void setURLPrefix(String url_prefix) {
            mURLPrefix = url_prefix;
        }

        /**
         * USERNAME
         * The username of the user currently logged in to the app. Null when no user is logged in
         * to the server.
         */
        private static String mUsername;

        public static String getUsername() {
            return mUsername;
        }

        public static void setUsername(String username) {
            mUsername = username;
        }

        /**
         * FULL NAME
         * The first and last name of the user currently logged in. Null when no user is logged in
         * to the server.
         */
        private static String mFullName;

        public static String getFullName() {
            return mFullName;
        }

        /**
         * UPDATE FULL NAME
         * Searches for the ID of the root person in the family tree, and sets full name data member
         * to the name of the person corresponding to that ID.
         */
        public static void updateFullName() {
            if (mRootPersonID == null || mFamilyTree == null) return;

            for (Person person : mFamilyTree)
                if (person.getPersonID().equals(mRootPersonID)) {
                    mFullName = person.getFirstName() + " " + person.getLastName();
                }
        }

        /**
         * ROOT PERSON ID
         * The ID of the person object corresponding to the user currently logged in. Null when no
         * user is logged in to the server.
         */
        private static String mRootPersonID;

        public static String getRootPersonID() {
            return mRootPersonID;
        }

        public static void setRootPersonID(String root_person_id) {
            mRootPersonID = root_person_id;
        }

        /**
         * AUTHENTICATION TOKEN
         * The most recent authentication token returned by the server. Null when no user is logged
         * in to the server.
         */
        private static String mAuthToken;

        public static String getAuthToken() {
            return mAuthToken;
        }

        public static void setAuthToken(String auth_token) {
            mAuthToken = auth_token;
        }

        /**
         * FAMILY TREE
         * The family tree of the currently logged in user. Null when no user is logged in to the
         * server.
         */
        private static ArrayList<Person> mFamilyTree;

        public static ArrayList<Person> getFamilyTree() {
            return mFamilyTree;
        }

        public static void setFamilyTree(ArrayList<Person> family_tree) {
            mFamilyTree = family_tree;
        }

        /**
         * FAMILY EVENTS
         * All of the events associated with members of the current user's family tree. Null when no
         * user is logged in to the server.
         */
    }
}