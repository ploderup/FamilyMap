package com.example.ploderup.communication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.ploderup.model.FamilyMap;
import com.example.ploderup.model.UserInfo;
import com.example.ploderup.userinterface.R;
import com.example.ploderup.userinterface.SettingsActivity;

import Facade.Result.EventResult;
import Facade.Result.PersonResult;
import Model.Person;

public class DataSyncTask extends AsyncTask<Void, Void, Boolean> {
// MEMBERS
    private final String TAG = "DataSyncTask";
    private AppCompatActivity mParentActivity;
    private FamilyMap mFamilyMap = FamilyMap.getInstance();
    private ServerProxy mServerProxy = ServerProxy.getInstance();
    private UserInfo mUserInfo = UserInfo.getInstance();

// METHODS
    public DataSyncTask(AppCompatActivity mParentActivity) {
        this.mParentActivity = mParentActivity;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        // Set flag
        mFamilyMap.setDataSyncDone(false);

        // Was authentication token in the ServerProxy set?
        if (mServerProxy.getAuthToken() == null) {
            Log.e(TAG, "Authentication token stored at ServerProxy is null");
            return false;
        }

        // Retrieve user information from UserInfo singleton
        if (!mUserInfo.isServerInfoValid() || !mUserInfo.isLoginInfoValid()) {
            Log.e(TAG, "Invalid information stored at UserInfo singleton class");
            return false;
        }

        // Create URL from user-provided server host name and port number
        final String URL_PREFIX = mUserInfo.getURLPrefix();

        // Retrieve all of the user's family members and events
        PersonResult person_result = mServerProxy.getFamilyTree(URL_PREFIX);
        EventResult event_result = mServerProxy.getFamilyEvents(URL_PREFIX);

        // Check information returned from server
        if (person_result == null || event_result == null) {
            Log.e(TAG, "Null pointer returned by server proxy");
            return false;
        }
        if (person_result.getData() == null || event_result.getData() == null) {
            Log.e(TAG, "Null data returned by server proxy");
            return false;
        }

        // Update root person in FamilyMap


        // Update FamilyMap with data retrieved
        mFamilyMap.setAllPeople(person_result.getData());
        mFamilyMap.setAllEvents(event_result.getData());
        mFamilyMap.setRootPersonID(mServerProxy.getRootPersonID());
        mFamilyMap.setDataSyncDone(true);

        return true;
    }

    @Override
    public void onPostExecute(Boolean result) {
        // Was the request made from the SettingsActivity?
        if (mParentActivity instanceof SettingsActivity) {
            // Was the synchronization successful?
            if (result) {
                Toast.makeText(mParentActivity, R.string.sync_successful_toast, Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(mParentActivity, R.string.sync_failed_toast, Toast.LENGTH_SHORT)
                        .show();
            }

        // The request was made by Login/RegisterTask
        } else {
            if (result) {
                Log.d(TAG, "Sync with the server was successful");
            } else {
                Log.d(TAG, "Sync with the server failed");
            }
        }
    }
}