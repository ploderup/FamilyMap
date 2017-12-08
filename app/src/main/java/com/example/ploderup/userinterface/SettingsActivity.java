package com.example.ploderup.userinterface;

import android.support.v4.app.Fragment;

/**
 * @see SingleFragmentActivity
 */
public class SettingsActivity extends SingleFragmentActivity {
    @Override
    protected String setTitle() {
        return "Settings";
    }

    @Override
    protected Fragment createFragment() {
        return new SettingsFragment();
    }
}
