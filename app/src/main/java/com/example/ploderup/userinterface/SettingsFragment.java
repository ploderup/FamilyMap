package com.example.ploderup.userinterface;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.example.ploderup.communication.ServerProxy;
import com.example.ploderup.model.Settings;
import com.google.android.gms.maps.GoogleMap;

public class SettingsFragment extends Fragment {
// MEMBERS
    private final String TAG = "SettingsFragment";

    // Models
    private Settings mSettings = Settings.getInstance();

    // Widgets
    private Spinner mLifeStoryLineColorSpinner;
    private Switch mLifeStoryLineSwitch;
    private Spinner mFamilyTreeLineColorSpinner;
    private Switch mFamilyTreeLineSwitch;
    private Spinner mSpouseLineColorSpinner;
    private Switch mSpouseLineSwitch;
    private Spinner mMapTypeSpinner;
    private Button mResyncDataButton;
    private Button mLogoutButton;


// METHODS
    @Override
    public void onCreate(Bundle saved_instance_state) {
        super.onCreate(saved_instance_state);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saved_instance_state) {
        ArrayAdapter<CharSequence> adapter;

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        /** Wire Up All Widgets **/
        // Life-story line color spinner
        mLifeStoryLineColorSpinner = v.findViewById(R.id.life_story_line_color_spinner);
        adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.line_color_array, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLifeStoryLineColorSpinner.setAdapter(adapter);
        mLifeStoryLineColorSpinner.setSelection(
                getColorSpinnerIndex(mSettings.getLifeStoryLinesColor()));
        mLifeStoryLineColorSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSettings.setLifeStoryLinesColor(getHexFromIndex(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });

        // Life-story line switch
        mLifeStoryLineSwitch = v.findViewById(R.id.life_story_line_color_switch);
        mLifeStoryLineSwitch
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSettings.setLifeStoryLinesEnabled(isChecked);
            }
        });

        // Family tree line color spinner
        mFamilyTreeLineColorSpinner = v.findViewById(R.id.family_tree_line_color_spinner);
        adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.line_color_array, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFamilyTreeLineColorSpinner.setAdapter(adapter);
        mFamilyTreeLineColorSpinner.setSelection(
                getColorSpinnerIndex(mSettings.getFamilyTreeLinesColor()));
        mFamilyTreeLineColorSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSettings.setFamilyTreeLinesColor(getHexFromIndex(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });

        // Family tree line switch
        mFamilyTreeLineSwitch = v.findViewById(R.id.family_tree_line_color_switch);
        mFamilyTreeLineSwitch
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSettings.setFamilyTreeLinesEnabled(isChecked);
            }
        });

        // Spouse line color spinner
        mSpouseLineColorSpinner = v.findViewById(R.id.spouse_line_color_spinner);
        adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.line_color_array, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpouseLineColorSpinner.setAdapter(adapter);
        mSpouseLineColorSpinner.setSelection(getColorSpinnerIndex(mSettings.getSpouseLinesColor()));
        mSpouseLineColorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSettings.setSpouseLinesColor(getHexFromIndex(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });

        // Spouse line switch
        mSpouseLineSwitch = v.findViewById(R.id.spouse_line_color_switch);
        mSpouseLineSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSettings.setSpouseLinesEnabled(isChecked);
            }
        });

        // Map type spinner
        mMapTypeSpinner = v.findViewById(R.id.map_type_spinner);
        adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.map_type_array, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMapTypeSpinner.setAdapter(adapter);
        mMapTypeSpinner.setSelection(Settings.getInstance().getMapType()-1);
        mMapTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Update Settings to reflect new map type
                mSettings.setMapType(position+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Re-sync button
        mResyncDataButton = v.findViewById(R.id.resync_data_button);
        mResyncDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Re-sync data in cache with FamilyMap server
            }
        });

        // Logout button
        mLogoutButton = v.findViewById(R.id.logout_button);
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log the user out of FamilyMap
                ServerProxy.DataCache.setAuthToken(null);
                mSettings.setLoggedIn(false);
                getActivity().finish();
            }
        });

        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;

            default:
                Log.e(TAG, "Default (error) case reached at onOptionsItemSelected");
                Log.e(TAG, "Item ID was " + Integer.toHexString(item.getItemId()));
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * @param color_value, a hex value corresponding to a color
     * @return the index in the spinner corresponding to the color provided
     */
    private int getColorSpinnerIndex(int color_value) {
        switch(color_value) {
            case R.color.red:
                return 0;

            case R.color.green:
                return 1;

            case R.color.blue:
                return 2;

            // Invalid color provided
            default:
                return 0;
        }
    }

    /**
     * @param spinner_index a valid index in one of the color spinners
     * @return the hex value corresponding to the color value at the spinners given index
     */
    private int getHexFromIndex(int spinner_index) {
        switch(spinner_index) {
            case 0:
                return R.color.red;

            case 1:
                return R.color.green;

            case 2:
                return R.color.blue;

            default:
                return 0;
        }
    }
}
