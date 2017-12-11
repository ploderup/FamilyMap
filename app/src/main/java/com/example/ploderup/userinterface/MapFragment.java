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

import com.example.ploderup.model.Settings;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment implements OnMapReadyCallback {
// MEMBERS
    private final String TAG = "MapFragment";
    private GoogleMap mGoogleMap;
    private LinearLayout mEventDetails;
    private ImageView mEventIcon;
    private Settings mSettings = Settings.getInstance();

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
        mEventDetails = v.findViewById(R.id.event_display);
        mEventDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create PersonActivity to display information about person associated w/ event
                getActivity().startActivity(new Intent(getActivity(), PersonActivity.class));
            }
        });

        // Initialize event display box
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

        // TODO: Lay down event pins

        // TODO: Draw map lines

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mGoogleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
