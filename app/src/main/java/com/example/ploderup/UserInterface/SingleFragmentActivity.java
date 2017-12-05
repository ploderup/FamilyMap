package com.example.ploderup.UserInterface;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


/**
 * SINGLE FRAGMENT ACTIVITY
 * An abstract activity to be extended by any activity containing a single fragment (such as
 * SearchActivity, SettingsActivity, for example).
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {
// METHODS
    /**
     * CREATE FRAGMENT
     * To be overridden by the activity extending this class to allow for any fragment to be used by
     * the activity.
     *
     * @return a valid Fragment object
     */
    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}
