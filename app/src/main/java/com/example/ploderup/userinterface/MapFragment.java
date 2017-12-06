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
import android.view.WindowManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment implements OnMapReadyCallback {
// MEMBERS
    private final String TAG = "MapFragment";
    private GoogleMap mMap;

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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        Log.d(TAG, "We made it to this point!");
        mapFragment.getMapAsync(this);

        return v;
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
                    break;

                case R.id.search_menu_item:
                    // Start a SearchActivity
                    break;

                case R.id.settings_menu_item:
                    // Start a SettingsActivity
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

        // TODO What do I return here?
        return false;
    }

    /**
     * Manipulates the map once available.
     */
    @Override
    public void onMapReady(GoogleMap google_map) {
        mMap = google_map;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
