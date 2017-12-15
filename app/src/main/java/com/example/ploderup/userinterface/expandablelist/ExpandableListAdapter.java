package com.example.ploderup.userinterface.expandablelist;


import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ploderup.model.FamilyMap;
import com.example.ploderup.userinterface.PersonActivity;
import com.example.ploderup.userinterface.PersonFragment;
import com.example.ploderup.userinterface.R;

import java.util.HashMap;
import java.util.List;

import Model.Event;
import Model.Person;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private final String TAG = "ExpandableListAdapter";
    private FamilyMap sFamilyMap = FamilyMap.getInstance();
    private Context context;
    private List<String> mExpandableListTitles;
    private HashMap<String, List<Object>> mExpandableListDetails;
    private String mCurrentPersonID;

    public ExpandableListAdapter(Context context, List<String> mExpandableListTitles,
                                 HashMap<String, List<Object>> mExpandableListDetails,
                                 String mCurrentPersonID) {
        this.context = context;
        this.mExpandableListTitles = mExpandableListTitles;
        this.mExpandableListDetails = mExpandableListDetails;
        this.mCurrentPersonID = mCurrentPersonID;
    }

    @Override
    public Object getChild(int position, int expanded_position) {
        return this.mExpandableListDetails
                .get(this.mExpandableListTitles.get(position))
                .get(expanded_position);
    }

    @Override
    public long getChildId(int position, int expanded_position) {
        return expanded_position;
    }

    @Override
    public View getChildView(int position, final int expanded_position, boolean is_last_child,
                             View view, ViewGroup parent) {

        Object list_item = getChild(position, expanded_position);

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.expandable_list_item, null);
        }

        ImageView item_icon = view.findViewById(R.id.list_item_icon);
        TextView item_title = view.findViewById(R.id.list_item_title);
        TextView item_details = view.findViewById(R.id.list_item_details);

        // Is the 'list item' a Person object?
        if (list_item instanceof Person) {
            Person person = (Person) list_item;

            // Is the person male or female?
            if (person.getGender().equalsIgnoreCase("m"))
                item_icon.setImageDrawable(
                        ContextCompat.getDrawable(context, R.drawable.ic_male_gender));
            else
                item_icon.setImageDrawable(
                        ContextCompat.getDrawable(context, R.drawable.ic_female_gender));

            // Set the name and relationship field for the person
            item_title.setText(person.getFullName());
            item_details.setText(
                    sFamilyMap.calculateRelationship(person.getPersonID(), mCurrentPersonID));

        // It's an Event object
        } else {
            Event event = (Event) list_item;

            // Set the icon, information and name fields
            item_icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_location));
            item_title.setText(event.getEventType().substring(0,1).toUpperCase() +
                    event.getEventType().substring(1).toLowerCase() + ": " + event.getCity() +
                    ", " + event.getCountry() + " (" + event.getYear() + ")");
            item_details.setText(sFamilyMap.findPersonByID(event.getPersonID()).getFullName());
        }

        return view;
    }

    @Override
    public int getChildrenCount(int position) {
        return this.mExpandableListDetails
                .get(this.mExpandableListTitles.get(position))
                .size();
    }

    @Override
    public Object getGroup(int position) {
        return this.mExpandableListTitles.get(position);
    }

    @Override
    public int getGroupCount() {
        return this.mExpandableListTitles.size();
    }

    @Override
    public long getGroupId(int position) {
        return position;
    }

    @Override
    public View getGroupView(int position, boolean isExpanded,
                             View view, ViewGroup parent) {
        String list_title = (String) getGroup(position);
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.expandable_list_group, null);
        }
        TextView list_title_text_view = view
                .findViewById(R.id.expandable_list_title);
        list_title_text_view.setTypeface(null, Typeface.BOLD);
        list_title_text_view.setText(list_title);
        return view;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int position, int expanded_position) {
        return true;
    }
}