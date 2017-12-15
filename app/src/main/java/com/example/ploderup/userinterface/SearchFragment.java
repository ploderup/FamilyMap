package com.example.ploderup.userinterface;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ploderup.model.FamilyMap;
import com.example.ploderup.model.Search;
import com.example.ploderup.userinterface.recyclerlist.ResultAdapter;

import java.util.ArrayList;
import java.util.List;

import Model.Event;
import Model.Person;


public class SearchFragment extends Fragment {
    // MEMBERS
    private final String TAG = "SearchFragment";
    private SearchView mSearchBar;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mListAdapter;

    private Search sSearch = Search.getInstance();
    private List<Object> mSearchResults = new ArrayList<>();

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
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        mSearchBar = v.findViewById(R.id.search_bar);
        mSearchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchResults.clear();
                mSearchResults.addAll(sSearch.searchFamilyMap(query));
                mListAdapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Do nothing
                return false;
            }
        });

        mRecyclerView = v.findViewById(R.id.search_recycler_view);
        mListAdapter = new ResultAdapter(mSearchResults);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mListAdapter);

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
