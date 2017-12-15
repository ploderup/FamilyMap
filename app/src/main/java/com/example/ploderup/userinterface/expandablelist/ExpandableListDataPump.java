package com.example.ploderup.userinterface.expandablelist;

import com.example.ploderup.model.FamilyMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Model.Event;
import Model.Person;

public class ExpandableListDataPump {
    public static HashMap<String, List<Object>> getData(String person_id) {
        HashMap<String, List<Object>> expandableListDetail = new HashMap<>();

        // Add all of the family members of the person connected to the given ID, to the list
        List<Object> family_members = new ArrayList<>();
        for(Person p : FamilyMap.getInstance().getPersonsFamily(person_id)) family_members.add(p);

        // Add all of the life events of the person connected to the given ID, to the list
        List<Object> life_events = new ArrayList<>();
        for(Event e : FamilyMap.getInstance().getPersonsEvents(person_id)) life_events.add(e);

        expandableListDetail.put("Family members", family_members);
        expandableListDetail.put("Life events", life_events);

        return expandableListDetail;
    }
}
