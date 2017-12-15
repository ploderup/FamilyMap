package com.example.ploderup.userinterface;

import android.content.Context;
import android.content.Intent;
import android.content.ReceiverCallNotAllowedException;
import android.graphics.Typeface;
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
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ploderup.model.FamilyMap;
import com.example.ploderup.userinterface.expandablelist.ExpandableListAdapter;
import com.example.ploderup.userinterface.expandablelist.ExpandableListDataPump;

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

    private String mCurrentPersonID;

    private ExpandableListAdapter mExpandableListAdapter;
    private ExpandableListView mExpandableListView;
    private List<String> mExpandableListTitle;
    private HashMap<String, List<Object>> mExpandableListDetail;

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
        mCurrentPersonID = bundle.getString("person_id");

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

        // Retrieve the expandable list view
        mExpandableListView = v.findViewById(R.id.expandable_list);
        mExpandableListDetail = ExpandableListDataPump.getData(mCurrentPersonID);
        mExpandableListTitle = new ArrayList<String> (mExpandableListDetail.keySet());
        mExpandableListAdapter = new ExpandableListAdapter(getActivity(), mExpandableListTitle,
                mExpandableListDetail, mCurrentPersonID);
        mExpandableListView.setAdapter(mExpandableListAdapter);
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i,
                                        int i1, long l) {
                Object item = mExpandableListAdapter.getChild(i, i1);

                // Is the 'list item' a Person object?
                if (item instanceof Person) {
                    // Cast the item to Person type
                    Person person = (Person) item;

                    // Start another PersonActivity
                    getActivity().startActivity(new Intent(getActivity(), PersonActivity.class)
                            .putExtra("person_id", person.getPersonID())
                            .putExtra("first_name", person.getFirstName())
                            .putExtra("last_name", person.getLastName())
                            .putExtra("gender", person.getGender()));

                // It's an Event object
                } else {
                    // Cast the item to an Event type
                    Event event = (Event) item;

                    // Start a new MapActivity
                    getActivity().startActivity(new Intent(getActivity(), MapActivity.class)
                            .putExtra("event_id", event.getEventID())
                            .putExtra("person_id", event.getPersonID()));
                }



                return false;
            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_mp, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;

            case R.id.top_menu_item:
                startActivity(new Intent(getActivity(), MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                break;

            default:
                Log.e(TAG, "Default (error) case reached at onOptionsItemSelected");
                Log.e(TAG, "Item ID was " + Integer.toHexString(item.getItemId()));
        }

        return super.onOptionsItemSelected(item);
    }

}
