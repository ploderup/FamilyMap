package com.example.ploderup.UserInterface;

import android.support.v4.app.Fragment;

public class PersonActivity extends SingleFragmentActivity {
    /**
     * @see SingleFragmentActivity
     */
    @Override
    protected Fragment createFragment() {
        return new PersonFragment();
    }
}
