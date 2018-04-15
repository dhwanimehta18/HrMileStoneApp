package com.hrmilestoneapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class EventsFragment extends Fragment implements View.OnClickListener {

    View view;
    FloatingActionButton fab_event;
    int[] event_pic = {R.drawable.event_pic};
    int[] event_date = {R.drawable.event_date};
    String[] event_name = {"Product innovation hunt"};
    ListView lstEvents;
    String userChoosenTask;
    DatabaseReference fref;
    ArrayList<Events> eventsArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_events, container, false);

        lstEvents = view.findViewById(R.id.lstEvents);

        FirebaseDatabase fdatabase = FirebaseDatabase.getInstance();
        fref = fdatabase.getReference("event");
        eventsArrayList = new ArrayList<Events>();
        fref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Events events = snapshot.getValue(Events.class);
                    String eventsName = events.getEvent_name();
                    Log.e("****eventsName****", "eventsName" + eventsName);
                    eventsArrayList.add(events);
                    setData(eventsArrayList);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        fab_event = view.findViewById(R.id.fab_event);
        fab_event.setOnClickListener(EventsFragment.this);

        // Inflate the layout for this fragment
        return view;

    }

    private void setData(ArrayList<Events> eventsArrayList) {
        EventListAdapter eventListAdapter = new EventListAdapter(getActivity(), eventsArrayList);
        lstEvents.setAdapter(eventListAdapter);
        eventListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_event:
                createEvent();
                break;
        }
    }


    private void createEvent() {
        Intent next = new Intent(getActivity(), CreateEvent.class);
        startActivity(next);
    }

}

