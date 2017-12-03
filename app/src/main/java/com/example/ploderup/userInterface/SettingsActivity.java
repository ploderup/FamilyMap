package com.example.ploderup.userInterface;

import android.support.v4.app.Fragment;

public class SettingsActivity extends SingleFragmentActivity {
    /**
     * @see SingleFragmentActivity
     */
    @Override
    protected Fragment createFragment() {
        return new SettingsFragment();
    }
}
