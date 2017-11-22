package com.example.ploderup.Model;

import java.util.ArrayList;

import Model.Person;
import Model.User;

public class DataCache {
// MEMBERS
    /**
     * USER
     * The user currently logged in to the app. Null until a successful login has occurred.
     */
    private User mUser;
    public void setUser(User user) { mUser = user; }

    /**
     * ROOT PERSON
     * The person object corresponding to the user currently logged in. Null until a successful
     * login has occurred.
     */
    private Person mRootPerson;
    public void setRootPerson(Person root_person) { mRootPerson = root_person; }

    /**
     * FAMILY TREE
     * The family tree of the currently logged in user. Null until a successful login has occured.
     */
    private ArrayList<Person> mFamilyTree;
    public void setFamilyTree(ArrayList<Person> family_tree) { mFamilyTree = family_tree; }
}
