package com.example.ploderup.userinterface;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * @see SingleFragmentActivity
 */
public class FilterActivity extends SingleFragmentActivity {
    @Override
    protected String setTitle() {
        return "FamilyMap | Filters";
    }

    @Override
    protected Fragment createFragment() {
        return new FilterFragment();
    }
}
