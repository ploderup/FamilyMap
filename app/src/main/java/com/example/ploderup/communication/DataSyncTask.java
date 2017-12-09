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

import Facade.Result.LoginResult;
import Facade.Result.PersonResult;
import Model.Person;

public class DataSyncTask extends AsyncTask<Void, Void, Boolean> {
    // MEMBERS
    private final String TAG = "LoginTask";
    private AppCompatActivity mParentActivity;


    // METHODS
    public DataSyncTask(AppCompatActivity parent_activity) {
        mParentActivity = parent_activity;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        PersonResult person_result;

        // Might need to check some stuff here (auth token?) TODO
        if (ServerProxy.DataCache.getUsername() == null)

        // Retrieve all of the user's family members
        person_result = ServerProxy.getFamilyTree(URL_PREFIX);
        if(person_result == null) return false;
        family_tree = person_result.getData();
        if(family_tree == null) return false;

        // Update the data cache with the new tree
        ServerProxy.DataCache.setFamilyTree(family_tree);
        ServerProxy.DataCache.updateFullName();


    }

    @Override
    public void onPostExecute(Boolean result) {
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