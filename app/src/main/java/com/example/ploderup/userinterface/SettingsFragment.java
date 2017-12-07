package com.example.ploderup.userinterface;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Switch;

public class SettingsFragment extends Fragment {
// MEMBERS
    private final String TAG = "SettingsFragment";
    private Spinner mLifeStoryLineColorSpinner;
    private Switch mLifeStoryLineSwitch;
    private Spinner mFamilyTreeLineColorSpinner;
    private Switch mFamilyTreeLineSwitch;
    private Spinner mSpouseLineColorSpinner;
    private Switch mSpouseLineSwitch;


// METHODS
    @Override
    public void onCreate(Bundle saved_instance_state) {
        Log.d(TAG, "onCreate(...)");
        super.onCreate(saved_instance_state);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saved_instance_state) {
        Log.d(TAG, "onCreateView(...)");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        // Wire-up all widgets
        mLifeStoryLineColorSpinner = v.findViewById(R.id.life_story_line_color_spinner);
        mLifeStoryLineColorSpinner
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Here's the item selected...
                // TODO: switch on the item
                adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });
        mLifeStoryLineSwitch = v.findViewById(R.id.life_story_line_color_switch);
        mFamilyTreeLineColorSpinner = v.findViewById(R.id.family_tree_line_color_spinner);
        mFamilyTreeLineSwitch = v.findViewById(R.id.family_tree_line_color_switch);
        mSpouseLineColorSpinner = v.findViewById(R.id.spouse_line_color_spinner);
        mSpouseLineSwitch = v.findViewById(R.id.spouse_line_color_switch);
        // TODO: Finish with widgets
        // A link w/ good info on how to use spinners:
        // https://developer.android.com/guide/topics/ui/controls/spinner.html

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_fss, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.back_menu_item:
                // TODO: Go back
                break;

            default:
                Log.e(TAG, "Default (error) case reached at onOptionsItemSelected");
                Log.e(TAG, "Item ID was " + Integer.toHexString(item.getItemId()));
        }

        return false;
    }
}
