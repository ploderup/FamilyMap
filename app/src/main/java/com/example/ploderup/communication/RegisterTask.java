package com.example.ploderup.communication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.ploderup.model.Settings;
import com.example.ploderup.model.UserInfo;
import com.example.ploderup.userinterface.MapFragment;
import com.example.ploderup.userinterface.R;

import java.util.ArrayList;

import Facade.Result.PersonResult;
import Facade.Result.RegisterResult;
import Model.Person;

public class RegisterTask extends AsyncTask<Void, Void, Boolean> {
// MEMBERS
    private final String TAG = "RegisterTask";
    private AppCompatActivity mParentActivity;

// METHODS
    public RegisterTask(AppCompatActivity parent_activity) {
        mParentActivity = parent_activity;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        RegisterResult register_result;

        // Retrieve user information from UserInfo singleton
        UserInfo user_info = UserInfo.getInstance();
        if (!user_info.isServerInfoValid() || !user_info.isRegisterInfoValid()) {
            Log.e(TAG, "Invalid information stored at UserInfo singleton class");
            return false;
        }

        // Create URL from user-provided server host name and port number
        final String URL_PREFIX = "http://" + user_info.getServerHost() + ":" +
                user_info.getServerPort();

        // Try to register user through server proxy
        register_result = ServerProxy.registerUser(URL_PREFIX, user_info.getUsername(),
                user_info.getPassword(), user_info.getFirstName(), user_info.getLast_name(),
                user_info.getEmail(), user_info.getGender());

        // Was registration successful?
        Log.d(TAG, "Checking for returned result from SProxy");
        if(register_result != null) {
            // Was registration error-free?
            Log.d(TAG, "Checking whether result returned an error");
            if(register_result.getMessage() == null) {
                Log.d(TAG, "No error was returned");
                PersonResult person_result;
                ArrayList<Person> family_tree;

                // Update the data cache with the result
                ServerProxy.DataCache.setAuthToken(register_result.getToken());
                ServerProxy.DataCache.setUsername(register_result.getUsername());
                ServerProxy.DataCache.setRootPersonID(register_result.getPersonID());

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

            // ServerProxy.registerUser failed
        } else {
            Log.d(TAG, "The SProxy failed to register");
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        Log.d(TAG, "onPostExecute(" + result + ")");

        // Was the login successful?
        if(result) {
            // Set logged-in flag
            Settings.getInstance().setLoggedIn(true);

            // Print toast to user
            Toast.makeText(mParentActivity, mParentActivity.getString(
                    R.string.register_successful_toast, ServerProxy.DataCache.getFullName()),
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
            Toast.makeText(mParentActivity, R.string.register_failed_toast,
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
