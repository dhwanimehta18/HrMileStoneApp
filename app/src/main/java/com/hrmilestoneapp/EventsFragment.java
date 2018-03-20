package com.hrmilestoneapp;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


public class EventsFragment extends Fragment implements View.OnClickListener {

    View view;
    FloatingActionButton fab_event;
    int[] event_pic = {R.drawable.event_pic};
    int[] event_date = {R.drawable.event_date};
    String[] event_name = {"Product innovation hunt"};
    ListView lstEvents;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_events, container, false);

        lstEvents = view.findViewById(R.id.lstEvents);
        EventListAdapter eventListAdapter = new EventListAdapter(getActivity());
        eventListAdapter.eventPic(event_pic);
        eventListAdapter.eventDate(event_date);
        eventListAdapter.eventName(event_name);

        lstEvents.setAdapter(eventListAdapter);


        fab_event = view.findViewById(R.id.fab_event);
        fab_event.setOnClickListener(EventsFragment.this);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onClick(View view) {

    }
}
