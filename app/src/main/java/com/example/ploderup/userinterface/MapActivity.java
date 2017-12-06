package com.example.ploderup.userinterface;

import android.support.v4.app.Fragment;

public class MapActivity extends SingleFragmentActivity {
    /**
     * @see SingleFragmentActivity
     */
    @Override
    protected Fragment createFragment() {
        return new MapFragment();
    }
}
