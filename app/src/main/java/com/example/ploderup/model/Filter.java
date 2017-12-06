package com.example.ploderup.model;

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
    private boolean mFilterBaptismEvents;
    public boolean getFilterBaptismEvents() {
        return mFilterBaptismEvents;
    }
    public void setFilterBaptismEvents(boolean mFilterBaptismEvents) {
        this.mFilterBaptismEvents = mFilterBaptismEvents;
    }

    private boolean mFilterBirthEvents;
    public boolean getFilterBirthEvents() {
        return mFilterBirthEvents;
    }
    public void setFilterBirthEvents(boolean mFilterBirthEvents) {
        this.mFilterBirthEvents = mFilterBirthEvents;
    }

    private boolean mFilterCensusEvents;
    public boolean getFilterCensusEvents() {
        return mFilterCensusEvents;
    }
    public void setFilterCensusEvents(boolean mFilterCensusEvents) {
        this.mFilterCensusEvents = mFilterCensusEvents;
    }

    private boolean mFilterChristeningEvents;
    public boolean getFilterChristeningEvents() {
        return mFilterChristeningEvents;
    }
    public void setFilterChristeningEvents(boolean mFilterChristeningEvents) {
        this.mFilterChristeningEvents = mFilterChristeningEvents;
    }

    private boolean mFilterDeathEvents;
    public boolean getFilterDeathEvents() {
        return mFilterDeathEvents;
    }
    public void setFilterDeathEvents(boolean mFilterDeathEvents) {
        this.mFilterDeathEvents = mFilterDeathEvents;
    }

    private boolean mFilterFathersSide;
    public boolean getFilterFathersSide() {
        return mFilterFathersSide;
    }
    public void setFilterFathersSide(boolean mFilterFathersSide) {
        this.mFilterFathersSide = mFilterFathersSide;
    }

    private boolean mFilterMothersSide;
    public boolean getFilterMothersSide() {
        return mFilterMothersSide;
    }
    public void setFilterMothersSide(boolean mFilterMothersSide) {
        this.mFilterMothersSide = mFilterMothersSide;
    }

    private boolean mFilterMaleEvents;
    public boolean getFilterMaleEvents() {
        return mFilterMaleEvents;
    }
    public void setFilterMaleEvents(boolean mFilterMaleEvents) {
        this.mFilterMaleEvents = mFilterMaleEvents;
    }

    private boolean mFilterFemaleEvents;
    public boolean getFilterFemaleEvents() {
        return mFilterFemaleEvents;
    }
    public void setFilterFemaleEvents(boolean mFilterFemaleEvents) {
        this.mFilterFemaleEvents = mFilterFemaleEvents;
    }
}
