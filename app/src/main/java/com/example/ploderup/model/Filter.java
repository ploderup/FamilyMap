package com.example.ploderup.model;

import android.util.Log;

import Model.Event;

/**
 * A class of boolean flags that, when set, allow the user to filter certain results from the
 * search. Each flag is initialized to 'false', by default.
 */
public class Filter {
// SINGLETON STUFF
    private static Filter mFilter;
    private Filter() {
        // Disable all filters
        mFilterBaptismEvents = false;
        mFilterBirthEvents = false;
        mFilterCensusEvents = false;
        mFilterChristeningEvents = false;
        mFilterDeathEvents = false;
        mFilterMarriageEvents = false;
        mFilterFathersSide = false;
        mFilterMothersSide = false;
        mFilterMaleEvents = false;
        mFilterFemaleEvents = false;
    }
    public static Filter getInstance() {
        if(mFilter == null) mFilter = new Filter();
        return mFilter;
    }


// MEMBERS
    private final String TAG = "Filter";

    private boolean mFilterBaptismEvents;
    public boolean getFilterBaptismEvents() {
        return mFilterBaptismEvents;
    }
    public void setFilterBaptismEvents(boolean mFilterBaptismEvents) {
        this.mFilterBaptismEvents = mFilterBaptismEvents;
        Log.d(TAG, "mFilterBaptismEvents set to " + mFilterBaptismEvents);
    }

    private boolean mFilterBirthEvents;
    public boolean getFilterBirthEvents() {
        return mFilterBirthEvents;
    }
    public void setFilterBirthEvents(boolean mFilterBirthEvents) {
        this.mFilterBirthEvents = mFilterBirthEvents;
        Log.d(TAG, "mFilterBirthEvents set to " + mFilterBirthEvents);
    }

    private boolean mFilterCensusEvents;
    public boolean getFilterCensusEvents() {
        return mFilterCensusEvents;
    }
    public void setFilterCensusEvents(boolean mFilterCensusEvents) {
        this.mFilterCensusEvents = mFilterCensusEvents;
        Log.d(TAG, "mFilterCensusEvents set to " + mFilterCensusEvents);
    }

    private boolean mFilterChristeningEvents;
    public boolean getFilterChristeningEvents() {
        return mFilterChristeningEvents;
    }
    public void setFilterChristeningEvents(boolean mFilterChristeningEvents) {
        this.mFilterChristeningEvents = mFilterChristeningEvents;
        Log.d(TAG, "mFilterChristeningEvents set to " + mFilterChristeningEvents);
    }

    private boolean mFilterDeathEvents;
    public boolean getFilterDeathEvents() {
        return mFilterDeathEvents;
    }
    public void setFilterDeathEvents(boolean mFilterDeathEvents) {
        this.mFilterDeathEvents = mFilterDeathEvents;
        Log.d(TAG, "mFilterDeathEvents set to " + mFilterDeathEvents);
    }

    private boolean mFilterMarriageEvents;
    public boolean getFilterMarriageEvents() {
        return mFilterMarriageEvents;
    }
    public void setFilterMarriageEvents(boolean mFilterMarriageEvents) {
        this.mFilterMarriageEvents = mFilterMarriageEvents;
        Log.d(TAG, "mFilterMarriageEvents set to " + mFilterMarriageEvents);
    }

    private boolean mFilterFathersSide;
    public boolean getFilterFathersSide() {
        return mFilterFathersSide;
    }
    public void setFilterFathersSide(boolean mFilterFathersSide) {
        this.mFilterFathersSide = mFilterFathersSide;
        Log.d(TAG, "mFilterFathersSide set to " + mFilterFathersSide);
    }

    private boolean mFilterMothersSide;
    public boolean getFilterMothersSide() {
        return mFilterMothersSide;
    }
    public void setFilterMothersSide(boolean mFilterMothersSide) {
        this.mFilterMothersSide = mFilterMothersSide;
        Log.d(TAG, "mFilterMothersSide set to " + mFilterMothersSide);
    }

    private boolean mFilterMaleEvents;
    public boolean getFilterMaleEvents() {
        return mFilterMaleEvents;
    }
    public void setFilterMaleEvents(boolean mFilterMaleEvents) {
        this.mFilterMaleEvents = mFilterMaleEvents;
        Log.d(TAG, "mFilterMaleEvents set to " + mFilterMaleEvents);
    }

    private boolean mFilterFemaleEvents;
    public boolean getFilterFemaleEvents() {
        return mFilterFemaleEvents;
    }
    public void setFilterFemaleEvents(boolean mFilterFemaleEvents) {
        this.mFilterFemaleEvents = mFilterFemaleEvents;
        Log.d(TAG, "mFilterFemaleEvents set to " + mFilterFemaleEvents);
    }
}
