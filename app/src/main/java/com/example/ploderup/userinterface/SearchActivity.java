package com.example.ploderup.userinterface;

import android.support.v4.app.Fragment;

/**
 * @see SingleFragmentActivity
 */
public class SearchActivity extends SingleFragmentActivity {
    @Override
    protected String setTitle() {
        return "Search";
    }

    @Override
    protected Fragment createFragment() {
        return new SearchFragment();
    }
}
