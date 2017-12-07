package com.example.ploderup.userinterface;

import android.support.v4.app.Fragment;

/**
 * @see SingleFragmentActivity
 */
public class PersonActivity extends SingleFragmentActivity {
    @Override
    protected String setTitle() {
        return "FamilyMap | Person Details";
    }

    @Override
    protected Fragment createFragment() {
        return new PersonFragment();
    }
}
