package com.example.ploderup.communication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.ploderup.model.FamilyMap;
import com.example.ploderup.model.Settings;
import com.example.ploderup.model.UserInfo;
import com.example.ploderup.userinterface.MapFragment;
import com.example.ploderup.userinterface.R;

import java.util.ArrayList;

import Facade.Result.LoginResult;
import Facade.Result.PersonResult;
import Model.Person;

public class LoginTask extends AsyncTask<Void, Void, Boolean> {
// MEMBERS
    private final String TAG = "LoginTask";
    private ServerProxy mServerProxy = ServerProxy.getInstance();
    private UserInfo mUserInfo = UserInfo.getInstance();
    private AppCompatActivity mParentActivity;
    private Settings mSettings = Settings.getInstance();
    private FamilyMap mFamilyMap = FamilyMap.getInstance();


// METHODS
    public LoginTask(AppCompatActivity mParentActivity) { this.mParentActivity = mParentActivity; }

    /**
     * Attempts to log the user in to the FamilyMap server.
     * @param params void
     * @return true if successful, false otherwise
     */
    @Override
    protected Boolean doInBackground(Void... params) {
        // Retrieve user information from UserInfo singleton
        if (!mUserInfo.isServerInfoValid() || !mUserInfo.isLoginInfoValid()) {
            Log.e(TAG, "Invalid information stored at UserInfo singleton class");
            return false;
        }

        // Create URL from user-provided server host name and port number
        final String URL_PREFIX = mUserInfo.getURLPrefix();

        // Try to log user in through server proxy
        LoginResult login_result = mServerProxy.loginUser(URL_PREFIX, mUserInfo.getUsername(),
                mUserInfo.getPassword());

        // Was registration successful?
        if(login_result != null) {
            // Was registration error-free?
            if(login_result.getMessage() == null) {
                mServerProxy.setAuthToken(login_result.getToken());
                mServerProxy.setRootPersonID(login_result.getPersonID());

                // Retrieve person logged in from server
                PersonResult person_result = mServerProxy
                        .getPerson(URL_PREFIX, login_result.getPersonID());
                if (person_result == null) {
                    Log.e(TAG, "Null person result returned by mServerProxy.getPerson");
                    return false;
                }
                if (person_result.getFirstName() == null || person_result.getLastName() == null) {
                    Log.e(TAG, "Null first or last name returned by mServerProxy.getPerson");
                    return false;
                }

                // Update name in user info
                mUserInfo.setFirstName(person_result.getFirstName());
                mUserInfo.setLast_name(person_result.getLastName());

                return true;

            // Error message returned
            } else {
                Log.d(TAG, "An error message was returned");
                return false;
            }

        // mServerProxy.loginUser failed
        } else {
            return false;
        }
    }

    @Override
    public void onPostExecute(Boolean result) {
        Log.d(TAG, "onPostExecute(" + result + ");");
        mSettings.setLoggedIn(result);

        // Was the login successful?
        if (result) {
            Toast.makeText(mParentActivity,
                    mParentActivity.getString(R.string.login_successful_toast,
                            mUserInfo.getFullName()), Toast.LENGTH_SHORT).show();

            // Sync data with the server
            new DataSyncTask(mParentActivity).execute();

            // Switch to the MapFragment
            mParentActivity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new MapFragment())
                    .addToBackStack(null)
                    .commit();

        } else {
            mServerProxy.setAuthToken(null);
            mServerProxy.setRootPersonID(null);
            Toast.makeText(mParentActivity, R.string.login_failed_toast, Toast.LENGTH_SHORT).show();
        }
    }
}