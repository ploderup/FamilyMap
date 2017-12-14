package com.example.ploderup.model;

import android.util.Log;

import static android.graphics.Color.*;
import static com.google.android.gms.maps.GoogleMap.*;

/**
 * A storage class of values relating to settings for the FamilyMap app. See the default constructor
 * for default setting values.
 */
public class Settings {
// SINGLETON STUFF
    private static Settings mSettings;
    private Settings() {
        resetAllSettings();
        mLoggedIn = false;
    }
    public static Settings getInstance() {
        if(mSettings == null) mSettings = new Settings();
        return mSettings;
    }


// MEMBERS
    private final String TAG = "Settings";

    /**
     * Whether the life-story lines are enabled or not.
     */
    private boolean mLifeStoryLinesEnabled;
    public boolean getLifeStoryLinesEnabled() {
        return mLifeStoryLinesEnabled;
    }
    public void setLifeStoryLinesEnabled(boolean mLifeStoryLinesEnabled) {
        this.mLifeStoryLinesEnabled = mLifeStoryLinesEnabled;
        Log.i(TAG, "mLifeStoryLinesEnabled set to " + mLifeStoryLinesEnabled);
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
        Log.i(TAG, "mLifeStoryLinesColor set to " + mLifeStoryLinesColor);
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
        Log.i(TAG, "mFamilyTreeLinesEnabled set to " + mFamilyTreeLinesEnabled);
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
        Log.i(TAG, "mFamilyTreeLinesColor set to " + mFamilyTreeLinesColor);
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
        Log.i(TAG, "mSpouseLinesEnabled set to " + mSpouseLinesEnabled);
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
        Log.i(TAG, "mSpouseLinesColor set to " + mSpouseLinesColor);
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
        Log.i(TAG, "mMapType set to " + mMapType);
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
        Log.i(TAG, "mLoggedIn set to " + mLoggedIn);
    }

// METHODS
    /**
     * Resets all class members (except mLoggedIn) to their default values.
     */
    public void resetAllSettings() {
        mLifeStoryLinesEnabled = true;
        mLifeStoryLinesColor = GREEN;
        mFamilyTreeLinesEnabled = false;
        mFamilyTreeLinesColor = BLUE;
        mSpouseLinesEnabled = false;
        mSpouseLinesColor = RED;
        mMapType = MAP_TYPE_NORMAL;
    }
}
