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

import Facade.Result.PersonResult;
import Facade.Result.RegisterResult;
import Model.Person;

public class RegisterTask extends AsyncTask<Void, Void, Boolean> {
// MEMBERS
    private final String TAG = "RegisterTask";
    private AppCompatActivity mParentActivity;
    private ServerProxy mServerProxy = ServerProxy.getInstance();
    private UserInfo mUserInfo = UserInfo.getInstance();
    private Settings mSettings = Settings.getInstance();
    private FamilyMap mFamilyMap = FamilyMap.getInstance();


// METHODS
    public RegisterTask(AppCompatActivity mParentActivity) {
        this.mParentActivity = mParentActivity;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        // Retrieve user information from UserInfo singleton
        if (!mUserInfo.isServerInfoValid() || !mUserInfo.isRegisterInfoValid()) {
            Log.e(TAG, "Invalid information stored at UserInfo singleton class");
            return false;
        }

        // Create URL from user-provided server host name and port number
        final String URL_PREFIX = mUserInfo.getURLPrefix();

        // Try to register user through server proxy
        RegisterResult register_result = mServerProxy.registerUser(URL_PREFIX, mUserInfo.getUsername(),
                mUserInfo.getPassword(), mUserInfo.getFirstName(), mUserInfo.getLast_name(),
                mUserInfo.getEmail(), mUserInfo.getGender());

        // Was registration successful?
        if(register_result != null) {
            // Was registration error-free?
            if(register_result.getMessage() == null) {
                // Update the data cache with the result
                mServerProxy.setAuthToken(register_result.getToken());
                mServerProxy.setRootPersonID(register_result.getPersonID());

                return true;

            // Error message returned
            } else {
                return false;
            }

        // mServerProxy.registerUser failed
        } else {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        Log.d(TAG, "onPostExecute(" + result + ")");
        mSettings.setLoggedIn(result);

        // Was the registration successful?
        if (result) {
            Toast.makeText(mParentActivity,
                    mParentActivity.getString(R.string.register_successful_toast,
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
            Toast.makeText(mParentActivity, R.string.register_failed_toast, Toast.LENGTH_SHORT).show();
        }
    }
}
