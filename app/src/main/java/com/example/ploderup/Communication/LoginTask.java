package com.example.ploderup.Communication;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.ploderup.Model.Settings;
import com.example.ploderup.Model.UserInfo;
import com.example.ploderup.UserInterface.MapFragment;
import com.example.ploderup.UserInterface.R;

import java.util.ArrayList;

import Facade.Result.LoginResult;
import Facade.Result.PersonResult;
import Model.Person;

public class LoginTask extends AsyncTask<Void, Void, Boolean> {
// MEMBERS
    private final String TAG = "LoginTask";
    private AppCompatActivity mParentActivity;


// METHODS
    public LoginTask(AppCompatActivity parent_activity) {
        mParentActivity = parent_activity;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        LoginResult login_result;

        // Retrieve user information from UserInfo singleton
        UserInfo user_info = UserInfo.getInstance();
        if (!user_info.isServerInfoValid() || !user_info.isLoginInfoValid()) {
            Log.e(TAG, "Invalid information stored at UserInfo singleton class");
            return false;
        }

        // Create URL from user-provided server host name and port number
        final String URL_PREFIX = "http://" + user_info.getServerHost() + ":" +
                user_info.getServerPort();

        // Try to log user in through server proxy
        login_result = ServerProxy.loginUser(URL_PREFIX, user_info.getUsername(),
                user_info.getPassword());

        // Was registration successful?
        Log.d(TAG, "Checking for returned result from SProxy");
        if(login_result != null) {
            // Was registration error-free?
            Log.d(TAG, "Checking whether result returned an error");
            if(login_result.getMessage() == null) {
                Log.d(TAG, "No error was returned");
                PersonResult person_result;
                ArrayList<Person> family_tree;

                // Update the data cache with the result
                ServerProxy.DataCache.setAuthToken(login_result.getToken());
                ServerProxy.DataCache.setUsername(login_result.getUsername());
                ServerProxy.DataCache.setRootPersonID(login_result.getPersonID());

                // Retrieve newly generated family tree
                Log.d(TAG, "Retrieving newly generated family tree");
                person_result = ServerProxy.getFamilyTree(URL_PREFIX);
                Log.d(TAG, "Checking if result was null");
                if(person_result == null) return false;
                family_tree = person_result.getData();
                Log.d(TAG, "Checking if tree is null");
                if(family_tree == null) return false;

                // Update the data cache with the new tree
                ServerProxy.DataCache.setFamilyTree(family_tree);
                ServerProxy.DataCache.updateFullName();

                return true;

            // Error message returned
            } else {
                Log.d(TAG, "An error message was returned");
                return false;
            }

            // ServerProxy.loginUser failed
        } else {
            Log.d(TAG, "The SProxy failed to register");
            return false;
        }
    }

    @Override
    public void onPostExecute(Boolean result) {
        Log.d(TAG, "onPostExecute(" + result + ")");

        // Was the login successful?
        if(result) {
            // Set logged-in flag
            Settings.getInstance().setLoggedIn(true);

            // Print toast to user
            Toast.makeText(mParentActivity, mParentActivity.getString(
                    R.string.login_successful_toast, ServerProxy.DataCache.getFullName()),
                    Toast.LENGTH_SHORT)
                    .show();

            // Switch to MapFragment
            mParentActivity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new MapFragment())
                    .addToBackStack(null)
                    .commit();

        } else {
            Settings.getInstance().setLoggedIn(false);

            // Print toast to user
            Toast.makeText(mParentActivity, R.string.login_failed_toast,
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }
}