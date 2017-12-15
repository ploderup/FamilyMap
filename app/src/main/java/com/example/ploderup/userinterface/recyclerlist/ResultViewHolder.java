package com.example.ploderup.userinterface.recyclerlist;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ploderup.model.FamilyMap;
import com.example.ploderup.userinterface.MapActivity;
import com.example.ploderup.userinterface.PersonActivity;
import com.example.ploderup.userinterface.R;

import java.util.List;

import Model.Event;
import Model.Person;

public class ResultViewHolder extends RecyclerView.ViewHolder {
    private FamilyMap sFamilyMap = FamilyMap.getInstance();
    private final String TAG = "ResultViewHolder";

    private ImageView mIcon;
    private TextView mTitle;
    private TextView mDetails;
    private Context context;
    private List<Object> mSearchResults;

    public ResultViewHolder(View v, final List<Object> mSearchResults) {
        super(v);

        context = v.getContext();
        this.mSearchResults = mSearchResults;

        // Wire-up widgets
        mIcon = v.findViewById(R.id.list_item_icon);
        mTitle = v.findViewById(R.id.list_item_title);
        mDetails = v.findViewById(R.id.list_item_details);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the position of the clicked item, and the item itself
                int position = getAdapterPosition();
                Object item = mSearchResults.get(position);

                // What was the class of the item clicked?
                if (item instanceof Event) {
                    // Cast the item to an Event type
                    Event event = (Event) item;

                    // Start a new MapActivity
                    context.startActivity(new Intent(context, MapActivity.class)
                            .putExtra("event_id", event.getEventID())
                            .putExtra("person_id", event.getPersonID()));

                } else {
                    // Cast the item to Person type
                    Person person = (Person) item;

                    // Start another PersonActivity
                    context.startActivity(new Intent(context, PersonActivity.class)
                            .putExtra("person_id", person.getPersonID())
                            .putExtra("first_name", person.getFirstName())
                            .putExtra("last_name", person.getLastName())
                            .putExtra("gender", person.getGender()));
                }
            }
        });
    }


    /**
     * Binds an event object to a view.
     */
    public void bind(Event event) {
        // Set view values
        mIcon.setImageDrawable(
                ContextCompat.getDrawable(context, R.drawable.ic_location));
        mTitle.setText(event.getEventType().substring(0,1).toUpperCase() +
                event.getEventType().substring(1).toLowerCase() + ": " + event.getCity() +
                ", " + event.getCountry() + " (" + event.getYear() + ")");
        mDetails.setText(sFamilyMap.findPersonByID(event.getPersonID()).getFullName());
    }

    /**
     * Binds a person object to a view.
     */
    public void bind(Person person) {
        // Draw icons
        if (person.getGender().equals("m")) {
            mIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_male_gender));
        } else {
            mIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_female_gender));
        }

        // Set values in TextViews
        mTitle.setText(person.getFullName());
        mDetails.setText("");
    }
}