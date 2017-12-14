package com.example.ploderup.userinterface;

import android.content.ReceiverCallNotAllowedException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ploderup.model.FamilyMap;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import Model.Event;
import Model.Person;

public class PersonFragment extends Fragment {
    // MEMBERS
    private final String TAG = "PersonFragment";

    private FamilyMap sFamilyMap = FamilyMap.getInstance();

    private TextView mFirstNameTextView;
    private TextView mLastNameTextView;
    private TextView mGenderTextView;
    private RecyclerView mLifeEventsRecyclerView;
    private RecyclerView mFamilyMemberRecyclerView;


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
        View v = inflater.inflate(R.layout.fragment_person, container, false);

        // Wire-up all widgets
        mFirstNameTextView = v.findViewById(R.id.person_fragment_fname_view);
        mLastNameTextView = v.findViewById(R.id.person_fragment_lname_view);
        mGenderTextView = v.findViewById(R.id.person_fragment_gender_view);

        // Retrieve data passed by Intent (this should never be null)
        Bundle bundle = getArguments();

        // Fill text views
        mFirstNameTextView.setText(bundle.getString("first_name"));
        mLastNameTextView.setText(bundle.getString("last_name"));
        switch (bundle.getString("gender")) {
            case "m":
                mGenderTextView.setText(R.string.male_gender_detail);
                break;
            case "f":
                mGenderTextView.setText(R.string.female_gender_detail);
                break;
            default:
                Log.e(TAG, "Invalid gender passed by Intent");
        }

        // Set-up recycler views
        mLifeEventsRecyclerView = v.findViewById(R.id.life_events_recycler_view);
        mLifeEventsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mLifeEventsRecyclerView
                .setAdapter(new EventListAdapter(
                        sFamilyMap.getPersonsEvents(bundle.getString("person_id"))));
        mFamilyMemberRecyclerView = v.findViewById(R.id.family_members_recycler_view);
        mFamilyMemberRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFamilyMemberRecyclerView
                .setAdapter(new PersonListAdapter(
                        sFamilyMap.getPersonsFamily(bundle.getString("person_id"))));

        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;

            default:
                Log.e(TAG, "Default (error) case reached at onOptionsItemSelected");
                Log.e(TAG, "Item ID was " + Integer.toHexString(item.getItemId()));
        }

        return super.onOptionsItemSelected(item);
    }

// INNER CLASSES
    private class Holder extends RecyclerView.ViewHolder {
        private ImageView mIcon;
        private TextView mTitle;
        private TextView mDetails;

        public Holder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_person, parent, false));
            mIcon = parent.findViewById(R.id.list_item_icon);
            mTitle = parent.findViewById(R.id.list_item_title);
            mDetails = parent.findViewById(R.id.list_item_details);
        }
    }

    private class PersonListAdapter extends RecyclerView.Adapter<Holder> {
        private ArrayList<Person> mFamilyMembers;

        public PersonListAdapter(ArrayList<Person> mFamilyMembers) {
            this.mFamilyMembers = mFamilyMembers;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int view_type) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new Holder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return mFamilyMembers.size();
        }
    }

    private class EventListAdapter extends RecyclerView.Adapter<Holder> {
        private ArrayList<Event> mPersonEvents;

        public EventListAdapter(ArrayList<Event> mPersonEvents) {
            this.mPersonEvents = mPersonEvents;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int view_type) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new Holder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return mPersonEvents.size();
        }
    }
}
