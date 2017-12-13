package com.example.ploderup.userinterface;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ploderup.model.FamilyMap;
import com.example.ploderup.model.Filter;
import com.example.ploderup.model.Settings;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import Model.Event;
import Model.Person;

public class MapFragment extends Fragment implements OnMapReadyCallback {
// MEMBERS
    private final String TAG = "MapFragment";
    private GoogleMap mGoogleMap;
    private LinearLayout mEventDisplay;
    private ImageView mEventIcon;
    private TextView mEventPerson;
    private TextView mEventDetails;
    private Person mCurrentPerson;

    private ArrayList<Marker> mEventMarkers;

    private Settings mSettings = Settings.getInstance();
    private Filter mFilter = Filter.getInstance();
    private FamilyMap mFamilyMap = FamilyMap.getInstance();

// METHODS
    /**
     * Required empty public constructor.
     */
    public MapFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView called");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        // Set on-click listener for event display box
        mEventDisplay = v.findViewById(R.id.event_display);
        mEventDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create PersonActivity to display information about person associated w/ event
                getActivity().startActivity(new Intent(getActivity(), PersonActivity.class));
            }
        });

        // Initialize event display box
        mEventPerson = v.findViewById(R.id.event_person);
        mEventDetails = v.findViewById(R.id.event_details);
        mEventIcon = v.findViewById(R.id.event_icon);
        mEventIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_location));

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // TODO: Update the map
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        // Check whether this fragment exists within MainActivity...
        if (getActivity() instanceof MainActivity) {
            inflater.inflate(R.menu.fragment_map, menu);

        // ...or MapActivity
        } else {
            inflater.inflate(R.menu.fragment_mp, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Check whether this fragment exists within MainActivity...
        if (getActivity() instanceof MainActivity) {
            switch(item.getItemId()) {
                case R.id.filter_menu_item:
                    // Start a FilterActivity
                    getActivity().startActivity(new Intent(getActivity(), FilterActivity.class));
                    break;

                case R.id.search_menu_item:
                    // Start a SearchActivity
                    getActivity().startActivity(new Intent(getActivity(), SearchActivity.class)
                            .putExtra("first_name", mCurrentPerson.getFirstName())
                            .putExtra("last_name", mCurrentPerson.getLastName())
                            .putExtra("gender", mCurrentPerson.getGender()));
                    break;

                case R.id.settings_menu_item:
                    // Start a SettingsActivity
                    Log.d(TAG, "Starting SettingsActivity");
                    getActivity().startActivity(new Intent(getActivity(), SettingsActivity.class));
                    break;

                default:
                    Log.e(TAG, "Default (error) case reached at onOptionsItemSelected");
                    Log.e(TAG, "Item ID was " + Integer.toHexString(item.getItemId()));
            }

        // ...or MapActivity
        } else {
            switch(item.getItemId()) {
                case R.id.back_menu_item:
                    // Return to the previous activity
                    break;

                case R.id.top_menu_item:
                    // Scroll to the top of the view
                    break;

                default:
                    Log.e(TAG, "Default (error) case reached at onOptionsItemSelected");
                    Log.e(TAG, "Item ID was " + Integer.toHexString(item.getItemId()));
            }
        }

        return false;
    }

    /**
     * Manipulates the map once available.
     */
    @Override
    public void onMapReady(GoogleMap mGoogleMap) {
        this.mGoogleMap = mGoogleMap;

        // Set-up the map
        mGoogleMap.setMapType(mSettings.getMapType());
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);

        // Wait for DataSyncTask to finish (TODO: Find a cleaner way to do this)
        while(!mFamilyMap.getDataSyncDone());

        // Do the markers need to be placed?
        if (mEventMarkers == null)
            placeEventMarkers(mFamilyMap.getAllEvents()); else updateEventMarkers();

        // Set a listener for clicks on the event markers
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Event event = (Event) marker.getTag();
                Person person = mFamilyMap.findPersonByID(event.getPersonID());

                // Set the gender icon
                if (person.getGender().equals("m")) {
                    mEventIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(),
                            R.drawable.ic_male_gender));
                } else {
                    mEventIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(),
                            R.drawable.ic_female_gender));
                }

                // Set the name and information of the person associated with the event
                mEventPerson.setText(person.getFirstName() + " " + person.getLastName());
                mEventDetails.setText(event.getEventType().substring(0, 1).toUpperCase() +
                        event.getEventType().substring(1) + ": " + event.getCity() + ", " +
                        event.getCountry() + " (" + event.getYear() + ")");

                // Update mCurrentPerson
                mCurrentPerson = person;

                return false;
            }
        });

        // TODO: Draw map lines

    }

    /**
     * Draws an event pin on the map for each event given.
     * @param events an array-list of events
     */
    private void placeEventMarkers(ArrayList<Event> events) {
        if(events == null) return;

        // Initialize the marker array-list
        mEventMarkers = new ArrayList<>();

        Log.d(TAG, "Drawing an event pin on the map");
        for(Event event : events) {
            // What color should the event pin be?
            float event_color;
            switch(event.getEventType().toLowerCase()) {
                case "baptism":     event_color = BitmapDescriptorFactory.HUE_BLUE; break;
                case "birth":       event_color = BitmapDescriptorFactory.HUE_RED; break;
                case "census":      event_color = BitmapDescriptorFactory.HUE_GREEN; break;
                case "christening": event_color = BitmapDescriptorFactory.HUE_ORANGE; break;
                case "marriage":    event_color = BitmapDescriptorFactory.HUE_CYAN; break;
                case "death":       event_color = BitmapDescriptorFactory.HUE_VIOLET; break;
                default:            event_color = BitmapDescriptorFactory.HUE_YELLOW; break;
            }

            // Where did the event take place?
            LatLng location = new LatLng(event.getLatitude(), event.getLongitude());

            // Drop a marker for the event
            Marker marker = mGoogleMap
                    .addMarker(new MarkerOptions()
                            .position(location)
                            .icon(BitmapDescriptorFactory.defaultMarker(event_color)));
            marker.setTag(event);

            // Add the marker to an array-list
            mEventMarkers.add(marker);
        }
    }

    /**
     * Sets marker visibility based upon whether it should be filtered or not.
     */
    private void updateEventMarkers() {
        Log.i(TAG, "Updating event markers");
        for (Marker marker : mEventMarkers) {
            // Should this marker be visible on the map (i.e., is it filtered?)
            marker.setVisible(!mFamilyMap.isFiltered((Event) marker.getTag()));
        }
    }
}
