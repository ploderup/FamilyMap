package com.example.ploderup.UserInterface;

import android.support.v4.app.Fragment;

public class SearchActivity extends SingleFragmentActivity {
    /**
     * @see SingleFragmentActivity
     */
    @Override
    protected Fragment createFragment() {
        return new SearchFragment();
    }
}
