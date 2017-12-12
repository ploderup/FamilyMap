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
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.ploderup.model.Filter;

public class FilterFragment extends Fragment {
// MEMBERS
    private final String TAG = "FilterFragment";

    private Filter mFilter = Filter.getInstance();

    private Switch mFilterBaptismEventsSwitch;
    private Switch mFilterBirthEventsSwitch;
    private Switch mFilterCensusEventsSwitch;
    private Switch mFilterChristeningEventsSwitch;
    private Switch mFilterDeathEventsSwitch;
    private Switch mFilterMarriageEventsSwitch;
    private Switch mFilterFathersSideSwitch;
    private Switch mFilterMothersSideSwitch;
    private Switch mFilterMaleEventsSwitch;
    private Switch mFilterFemaleEventsSwitch;

// METHODS
    @Override
    public void onCreate(Bundle saved_instance_state) {
        super.onCreate(saved_instance_state);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saved_instance_state) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_filter, container, false);

        // Wire-up all switches
        mFilterBaptismEventsSwitch = v.findViewById(R.id.filter_baptism_events_switch);
        mFilterBaptismEventsSwitch.setChecked(mFilter.getFilterBaptismEvents());
        mFilterBaptismEventsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mFilter.setFilterBaptismEvents(b);
            }
        });

        mFilterBirthEventsSwitch = v.findViewById(R.id.filter_birth_events_switch);
        mFilterBirthEventsSwitch.setChecked(mFilter.getFilterBirthEvents());
        mFilterBirthEventsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mFilter.setFilterBirthEvents(b);
            }
        });

        mFilterCensusEventsSwitch = v.findViewById(R.id.filter_census_events_switch);
        mFilterCensusEventsSwitch.setChecked(mFilter.getFilterCensusEvents());
        mFilterCensusEventsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mFilter.setFilterCensusEvents(b);
            }
        });

        mFilterChristeningEventsSwitch = v.findViewById(R.id.filter_christening_events_switch);
        mFilterChristeningEventsSwitch.setChecked(mFilter.getFilterChristeningEvents());
        mFilterChristeningEventsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mFilter.setFilterChristeningEvents(b);
            }
        });

        mFilterDeathEventsSwitch = v.findViewById(R.id.filter_death_events_switch);
        mFilterDeathEventsSwitch.setChecked(mFilter.getFilterDeathEvents());
        mFilterDeathEventsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mFilter.setFilterDeathEvents(b);
            }
        });

        mFilterMarriageEventsSwitch = v.findViewById(R.id.filter_marriage_events_switch);
        mFilterMarriageEventsSwitch.setChecked(mFilter.getFilterMarriageEvents());
        mFilterMarriageEventsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mFilter.setFilterMarriageEvents(b);
            }
        });

        mFilterFathersSideSwitch = v.findViewById(R.id.filter_fathers_side_switch);
        mFilterFathersSideSwitch.setChecked(mFilter.getFilterFathersSide());
        mFilterFathersSideSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mFilter.setFilterFathersSide(b);
            }
        });

        mFilterMothersSideSwitch = v.findViewById(R.id.filter_mothers_side_switch);
        mFilterMothersSideSwitch.setChecked(mFilter.getFilterMothersSide());
        mFilterMothersSideSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mFilter.setFilterMothersSide(b);
            }
        });

        mFilterMaleEventsSwitch = v.findViewById(R.id.filter_male_events_switch);
        mFilterMaleEventsSwitch.setChecked(mFilter.getFilterMaleEvents());
        mFilterMaleEventsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mFilter.setFilterMaleEvents(b);
            }
        });

        mFilterFemaleEventsSwitch = v.findViewById(R.id.filter_female_events_switch);
        mFilterFemaleEventsSwitch.setChecked(mFilter.getFilterFemaleEvents());
        mFilterFemaleEventsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mFilter.setFilterFemaleEvents(b);
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
}
