package com.example.ploderup.UserInterface;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.ploderup.Communication.ServerProxy;

public class MainActivity extends AppCompatActivity {
// METHODS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        // Is the user already logged in?
        if(ServerProxy.getLoggedIn()) {
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
     * TAG:
     * The name of the activity, for logging.
     */
    private final String TAG = "MainActivity";
}
