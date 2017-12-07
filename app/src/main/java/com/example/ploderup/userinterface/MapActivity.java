package com.example.ploderup.userinterface;

import android.support.v4.app.Fragment;

/**
 * @see SingleFragmentActivity
 */
public class MapActivity extends SingleFragmentActivity {
    @Override
    protected String setTitle() {
        return "FamilyMap";
    }

    @Override
    protected Fragment createFragment() {
        return new MapFragment();
    }
}
