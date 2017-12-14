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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ploderup.model.FamilyMap;
import com.example.ploderup.model.Filter;
import com.example.ploderup.model.Settings;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Iterator;

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
    private ArrayList<Polyline> mRelationshipLines;

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
                // Has a person been selected?
                if (mCurrentPerson != null)
                    // Create PersonActivity to display information about person associated w/ event
                    getActivity().startActivity(new Intent(getActivity(), PersonActivity.class)
                            .putExtra("first_name", mCurrentPerson.getFirstName())
                            .putExtra("last_name", mCurrentPerson.getLastName())
                            .putExtra("gender", mCurrentPerson.getGender()));
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
                    getActivity().startActivity(new Intent(getActivity(), SearchActivity.class));
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
                case android.R.id.home:
                    getActivity().finish();
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

                // Initialize the array-list of lines, if necessary
                if (mRelationshipLines == null) mRelationshipLines = new ArrayList<>();

                // Erase all relationship lines currently on the map
                for (Iterator<Polyline> iterator = mRelationshipLines.iterator();
                     iterator.hasNext();) {
                    iterator.next().remove();
                    iterator.remove();
                }

                // Update mCurrentPerson
                mCurrentPerson = person;

                // Set the gender icon
                if (person.getGender().equals("m")) {
                    mEventIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(),
                            R.drawable.ic_male_gender));
                } else {
                    mEventIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(),
                            R.drawable.ic_female_gender));
                }

                // Set the name and information of the person associated with the event
                mEventPerson.setText(mCurrentPerson.getFullName());
                mEventDetails.setText(event.getEventType().substring(0, 1).toUpperCase() +
                        event.getEventType().substring(1) + ": " + event.getCity() + ", " +
                        event.getCountry() + " (" + event.getYear() + ")");


                // Draw relationship lines
                drawRelationshipLines(marker);

                return false;
            }
        });
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
            Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                    .position(location)
                    .icon(BitmapDescriptorFactory.defaultMarker(event_color)));
            marker.setTag(event);

            // Add the marker to an array-list
            mEventMarkers.add(marker);
        }
    }

    /**
     * Sets marker visibility based upon filters set by the user.
     */
    private void updateEventMarkers() {
        Log.i(TAG, "Updating event markers");
        for (Marker marker : mEventMarkers) {
            // Should this marker be visible on the map (i.e., is it filtered?)
            marker.setVisible(!mFamilyMap.isFiltered((Event) marker.getTag()));
        }
    }

    /**
     * Based on the marker just selected, draws lines enabled by settings on the map.
     */
    private void drawRelationshipLines(Marker marker) {
        // Are spouse lines enabled?
        if (mSettings.getSpouseLinesEnabled()) drawSpouseLine(marker.getPosition());

        // Are family tree lines enabled?
        if (mSettings.getFamilyTreeLinesEnabled()) drawFamilyTreeLines();

        // Are life-story lines enabled?
        if (mSettings.getLifeStoryLinesEnabled()) drawLifeStoryLines();
    }

    /**
     * Draws a line between the event just selected, and the earliest event associated with the
     * spouse of the person connected to the selected event.
     */
    private void drawSpouseLine(LatLng location_a) {
        Log.i(TAG, "Drawing a spouse line for " + mCurrentPerson.getFullName());

        // Is the current person married?
        if (mCurrentPerson.getSpouseID() != null) {
            // Retrieve the person's spouse's earliest event
            Event spouses_event = mFamilyMap.getPersonsEarliestEvent(mCurrentPerson.getSpouseID());
            if (spouses_event == null) return;

            Log.d(TAG, "...whose spouse is " + mFamilyMap.findPersonByID(mCurrentPerson.getSpouseID()).getFullName());
            Log.d(TAG, "...whose earliest event is [" + spouses_event.getEventType() + "], in " + spouses_event.getCity() + ", " + spouses_event.getCountry() + " (" + spouses_event.getYear() + ")");

            // Get the location of the event just retrieved
            LatLng location_b = new LatLng(spouses_event.getLatitude(),
                    spouses_event.getLongitude());

            // Draw a line from the marker to the person's spouse's earliest event
            Polyline line = mGoogleMap.addPolyline(new PolylineOptions()
                    .add(location_a, location_b)
                    .color(mSettings.getSpouseLinesColor()));

            // Add line to array-list of all lines
            mRelationshipLines.add(line);
        } else {
            Log.d(TAG, "mCurrentPerson.getSpouseID = " + mCurrentPerson.getSpouseID());
            Log.d(TAG, "mCurrentPerson.getFatherID = " + mCurrentPerson.getFatherID());
            Log.d(TAG, "mCurrentPerson.getMotherID = " + mCurrentPerson.getMotherID());
        }
    }

    /**
     * Draws a line between the event just selected,
     */
    private void drawFamilyTreeLines() {

    }

    /**
     * Draws a line between the event just selected,
     */
    private void drawLifeStoryLines() {

    }
}
