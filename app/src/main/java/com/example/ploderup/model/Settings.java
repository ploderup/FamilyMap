package com.example.ploderup.model;

import android.util.Log;

import com.example.ploderup.userinterface.R;

import static com.google.android.gms.maps.GoogleMap.*;

/**
 * A storage class of values relating to settings for the FamilyMap app. See the default constructor
 * for default setting values.
 */
public class Settings {
// SINGLETON STUFF
    private static Settings mSettings;
    private Settings() {
        // Constant color values
        final int RED   = R.color.red;
        final int GREEN = R.color.green;
        final int BLUE  = R.color.blue;

        // Initialize settings
        mLifeStoryLinesEnabled = true;
        mLifeStoryLinesColor = GREEN;
        mFamilyTreeLinesEnabled = false;
        mFamilyTreeLinesColor = BLUE;
        mSpouseLinesEnabled = false;
        mSpouseLinesColor = RED;
        mMapType = MAP_TYPE_NORMAL;
        mLoggedIn = false;
    }
    public static Settings getInstance() {
        if(mSettings == null) mSettings = new Settings();
        return mSettings;
    }


// MEMBERS
    /**
     * Whether the life-story lines are enabled or not.
     */
    private boolean mLifeStoryLinesEnabled;
    public boolean getLifeStoryLinesEnabled() {
        return mLifeStoryLinesEnabled;
    }
    public void setLifeStoryLinesEnabled(boolean mLifeStoryLinesEnabled) {
        this.mLifeStoryLinesEnabled = mLifeStoryLinesEnabled;
    }

    /**
     * The color of the life-story lines (24-bit hexadecimal).
     */
    private int mLifeStoryLinesColor;
    public int getLifeStoryLinesColor() {
        return mLifeStoryLinesColor;
    }
    public void setLifeStoryLinesColor(int mLifeStoryLinesColor) {
        this.mLifeStoryLinesColor = mLifeStoryLinesColor;
    }

    /**
     * Whether the family-tree lines are enabled or not.
     */
    private boolean mFamilyTreeLinesEnabled;
    public boolean getFamilyTreeLinesEnabled() {
        return mFamilyTreeLinesEnabled;
    }
    public void setFamilyTreeLinesEnabled(boolean mFamilyTreeLinesEnabled) {
        this.mFamilyTreeLinesEnabled = mFamilyTreeLinesEnabled;
    }

    /**
     * The color of the family-tree lines (24-bit hexadecimal).
     */
    private int mFamilyTreeLinesColor;
    public int getFamilyTreeLinesColor() {
        return mFamilyTreeLinesColor;
    }
    public void setFamilyTreeLinesColor(int mFamilyTreeLinesColor) {
        this.mFamilyTreeLinesColor = mFamilyTreeLinesColor;
    }

    /**
     * Whether the spouse lines are enabled or not.
     */
    private boolean mSpouseLinesEnabled;
    public boolean getSpouseLinesEnabled() {
        return mSpouseLinesEnabled;
    }
    public void setSpouseLinesEnabled(boolean mSpouseLinesEnabled) {
        this.mSpouseLinesEnabled = mSpouseLinesEnabled;
    }

    /**
     * The color of the spouse lines (24-bit hexadecimal).
     */
    private int mSpouseLinesColor;
    public int getSpouseLinesColor() {
        return mSpouseLinesColor;
    }
    public void setSpouseLinesColor(int mSpouseLinesColor) {
        this.mSpouseLinesColor = mSpouseLinesColor;
    }

    /**
     * The type of map to be displayed by MapFragment.
     */
    private int mMapType;
    public int getMapType() {
        return mMapType;
    }
    public void setMapType(int mMapType) {
        this.mMapType = mMapType;
    }

    /**
     * Whether a user is logged in to the server or not.
     */
    private boolean mLoggedIn;
    public boolean getLoggedIn() {
        return mLoggedIn;
    }
    public void setLoggedIn(boolean mLoggedIn) {
        this.mLoggedIn = mLoggedIn;
    }
}
