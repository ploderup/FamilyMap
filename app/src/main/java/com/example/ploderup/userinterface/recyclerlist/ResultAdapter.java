package com.example.ploderup.userinterface.recyclerlist;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ploderup.userinterface.R;

import java.util.List;

import Model.Event;
import Model.Person;

public class ResultAdapter extends RecyclerView.Adapter<ResultViewHolder> {
// MEMBERS
    private List<Object> mSearchResults;
    private final String TAG = "ResultAdapter";

// METHODS
    /**
     * Constructs a new adapter object.
     * @param mSearchResults
     */
    public ResultAdapter(List<Object> mSearchResults) {
        this.mSearchResults = mSearchResults;
    }

    @Override
    public ResultViewHolder onCreateViewHolder(ViewGroup parent, int view_type) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recycler_list_item, parent, false);

        return new ResultViewHolder(v, mSearchResults);
    }

    @Override
    public void onBindViewHolder(ResultViewHolder holder, int position) {
        Object o = mSearchResults.get(position);

        // Is the object an event, or a person?
        if (o instanceof Event) {
            holder.bind((Event) o);

        // It's a person object
        } else {
            holder.bind((Person) o);
        }
    }

    @Override
    public int getItemCount() {
        return mSearchResults.size();
    }
}
