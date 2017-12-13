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
import android.widget.TextView;

public class PersonFragment extends Fragment {
// MEMBERS
    private final String TAG = "PersonFragment";

    private TextView mFirstNameTextView;
    private TextView mLastNameTextView;
    private TextView mGenderTextView;


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

        // Retrieve data passed by Intent
        if (getArguments() != null) {
            Log.d(TAG, "Arguments passed by intent were not null");
            Bundle bundle = getArguments();
            mFirstNameTextView.setText(bundle.getString("first_name"));
            mLastNameTextView.setText(bundle.getString("last_name"));
            switch(bundle.getString("gender")) {
                case "m": mGenderTextView.setText(R.string.male_gender_detail); break;
                case "f": mGenderTextView.setText(R.string.female_gender_detail); break;
                default: Log.e(TAG, "Invalid gender passed by Intent");
            }
        }

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
