package com.example.ploderup.model;

import android.util.Log;

import java.util.ArrayList;

import Model.Person;

public class DataCache {
// SINGLETON
    private static DataCache mInstance = new DataCache();
    public static DataCache getInstance() { return mInstance; }
    private DataCache() {}

// MEMBERS
    /**
     * URL PREFIX
     * The host name and port number of the FamilyMap server (e.g., 192.168.0.0:8080).
     */
    private String mURLPrefix;
    public String getURLPrefix() { return mURLPrefix; }
    public void setURLPrefix(String mURLPrefix) { this.mURLPrefix = mURLPrefix; }

    /**
     * USERNAME
     * The username of the user currently logged in to the app. Null when no user is logged in
     * to the server.
     */
    private String mUsername;
    public String getUsername() { return mUsername; }
    public void setUsername(String username) { mUsername = username; }

    /**
     * FULL NAME
     * The first and last name of the user currently logged in. Null when no user is logged in
     * to the server.
     */
    private String mFullName;
    public String getFullName() { return mFullName; }

    /**
     * UPDATE FULL NAME
     * Searches for the ID of the root person in the family tree, and sets full name data member
     * to the name of the person corresponding to that ID.
     */
    public void updateFullName() {
        if(mRootPersonID == null || mFamilyTree == null) return;

        for(Person person : mFamilyTree)
            if(person.getPersonID().equals(mRootPersonID)) {
                mFullName = person.getFirstName() + " " + person.getLastName();
            }
    }

    /**
     * ROOT PERSON ID
     * The ID of the person object corresponding to the user currently logged in. Null when no
     * user is logged in to the server.
     */
    private String mRootPersonID;
    public String getRootPersonID() { return mRootPersonID; }
    public void setRootPersonID(String root_person_id) { mRootPersonID = root_person_id; }

    /**
     * AUTHENTICATION TOKEN
     * The most recent authentication token returned by the server. Null when no user is logged
     * in to the server.
     */
    private String mAuthToken;
    public String getAuthToken() { return mAuthToken; }
    public void setAuthToken(String auth_token) { mAuthToken = auth_token; }

    /**
     * FAMILY TREE
     * The family tree of the currently logged in user. Null when no user is logged in to the
     * server.
     */
    private ArrayList<Person> mFamilyTree;
    public ArrayList<Person> getFamilyTree() { return mFamilyTree; }
    public void setFamilyTree(ArrayList<Person> family_tree) { mFamilyTree = family_tree; }

    /**
     * FAMILY EVENTS
     * All of the events associated with members of the current user's family tree. Null when no
     * user is logged in to the server.
     */
}