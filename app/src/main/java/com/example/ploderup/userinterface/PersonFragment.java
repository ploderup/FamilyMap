package com.example.ploderup.userinterface;

import android.content.ReceiverCallNotAllowedException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import java.util.HashMap;
import java.util.List;

import Model.Event;
import Model.Person;

public class PersonFragment extends Fragment {
    // MEMBERS
    private final String TAG = "PersonFragment";

    private TextView mFirstNameTextView;
    private TextView mLastNameTextView;
    private TextView mGenderTextView;
    private RecyclerView mLifeEventsRecyclerView;
    private RecyclerView mFamilyMemberRecyclerView;

    private FamilyMap sFamilyMap = FamilyMap.getInstance();


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
    private class PersonHolder extends RecyclerView.ViewHolder {
        private String mRelationship;
        private Person mPerson;

        private ImageView mIcon;
        private TextView mTitle;
        private TextView mDetails;

        public PersonHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_person, parent, false));

            // Wire-up widgets
            mIcon = parent.findViewById(R.id.list_item_icon);
            mTitle = parent.findViewById(R.id.list_item_title);
            mDetails = parent.findViewById(R.id.list_item_details);
        }

        public void bind(String r, Person p) {
            mRelationship = r;
            mPerson = p;

            if (mPerson.getGender().equalsIgnoreCase("m")) mIcon.setImageDrawable(
                        ContextCompat.getDrawable(getActivity(), R.drawable.ic_male_gender));
            else mIcon.setImageDrawable(
                        ContextCompat.getDrawable(getActivity(), R.drawable.ic_female_gender));
            mTitle.setText(mPerson.getFullName());
            mDetails.setText(mRelationship);
        }
    }

    private class EventHolder extends RecyclerView.ViewHolder {
        private Event mEvent;
        private String mPersonFullName;

        private ImageView mIcon;
        private TextView mTitle;
        private TextView mDetails;

        public EventHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_person, parent, false));

            // Wire-up widgets
            mIcon = parent.findViewById(R.id.list_item_icon);
            mTitle = parent.findViewById(R.id.list_item_title);
            mDetails = parent.findViewById(R.id.list_item_details);
        }

        public void bind(Event e) {
            mEvent = e;
            mPersonFullName = sFamilyMap.findPersonByID(e.getPersonID()).getFullName();

            mIcon.setImageDrawable(
                    ContextCompat.getDrawable(getActivity(), R.drawable.ic_location));
            mTitle.setText(mEvent.getEventType().substring(0,1).toUpperCase() +
                    mEvent.getEventType().substring(1).toLowerCase() + ": " + mEvent.getCity() +
                    ", " + mEvent.getCountry() + " (" + mEvent.getYear() + ")");
            mDetails.setText(mPersonFullName);
        }
    }

    private class PersonListAdapter extends RecyclerView.Adapter<PersonHolder> {
        private HashMap<String, Person> mFamilyMembers;

        public PersonListAdapter(HashMap<String, Person> mFamilyMembers) {
            this.mFamilyMembers = mFamilyMembers;
        }

        @Override
        public PersonHolder onCreateViewHolder(ViewGroup parent, int view_type) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new PersonHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(PersonHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return mFamilyMembers.size();
        }
    }

    private class EventListAdapter extends RecyclerView.Adapter<EventHolder> {
        private ArrayList<Event> mPersonEvents;

        public EventListAdapter(ArrayList<Event> mPersonEvents) {
            this.mPersonEvents = mPersonEvents;
        }

        @Override
        public EventHolder onCreateViewHolder(ViewGroup parent, int view_type) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new EventHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(EventHolder holder, int position) {
            holder.bind(mPersonEvents.get(position));
        }

        @Override
        public int getItemCount() {
            return mPersonEvents.size();
        }
    }
}
