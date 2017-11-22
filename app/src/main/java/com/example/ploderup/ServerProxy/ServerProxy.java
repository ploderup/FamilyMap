package com.example.ploderup.ServerProxy;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import Facade.Result.LoginResult;
import Facade.Result.RegisterResult;

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
     * @param URL_PREFIX a non-empty string specifying the hostname and port number of a valid
     *                   FamilyMap server
     * @param username a non-empty string
     * @param password a non-empty string
     * @return whether or not the login was successful
     */
    public static LoginResult loginUser(final String URL_PREFIX, String username, String password) {
        HttpURLConnection url_connection = new URL(URL_PREFIX);


        return null;
    }

    /**
     * REGISTER USER
     * Attempts to register the user at a given server's database, using given credentials.
     *
     * @param URL_PREFIX a non-empty string specifying the hostname and port number of a valid
     *                   FamilyMap server
     * @param username a non-empty string
     * @param password a non-empty string
     * @param first_name a non-empty string
     * @param last_name a non-empty string
     * @param email a valid email address
     * @param gender "m" or "f"
     * @return whether the registration was successful or not
     */
    public static RegisterResult registerUser(String URL_PREFIX, String username, String password,
                                    String first_name, String last_name, String email,
                                    String gender) {

        return null;
    }

    /**
     * OPEN CONNECTION
     * Opens a connection to a given URL.
     */
    private static HttpURLConnection openConnection(String URL_string, String request_method) {
        HttpURLConnection url_connection = null;

        try {
            // Create a new HTTP connection
            URL url = new URL(URL_string);
            url_connection = (HttpURLConnection) url.openConnection();

            // Setup the connection
            url_connection.setRequestMethod(request_method);
            if(request_method.equals("POST"))
                url_connection.setDoOutput(true);
            else
                url_connection.setDoOutput(false);



        } catch(MalformedURLException e) {
            Log.e(TAG, "MalformedURLException encountered. Check user input.");
        } catch(IOException e) {
            Log.e(TAG, "IOException encountered. Check user input.");
        }

        return url_connection;
    }
}
