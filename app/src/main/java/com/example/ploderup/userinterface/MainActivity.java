package com.example.ploderup.userinterface;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.ploderup.model.Settings;

public class MainActivity extends AppCompatActivity {
// MEMBERS
    private Settings mSettings = Settings.getInstance();
    private final String TAG = "MainActivity";

// METHODS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        // Is the user already logged in?
        if(mSettings.getLoggedIn()) {
            // Open the map screen
            if (fragment == null) {
                fragment = new MapFragment();
                fm.beginTransaction()
                        .add(R.id.fragment_container, fragment)
                        .commit();
            }

        // The user is not logged in
        } else {
            // Open the login screen
            if (fragment == null) {
                fragment = new LoginFragment();
                fm.beginTransaction()
                        .add(R.id.fragment_container, fragment)
                        .commit();
            }
        }
    }

    /**
     * Alters the behavior of the native back button. By default, a back press when the MapFragment
     * is open will return the user to the LoginFragment. This override instead closes the app.
     */
    @Override
    public void onBackPressed() {
        Log.d(TAG, "Back button pressed in MainActivity");
        finish();
    }
}
