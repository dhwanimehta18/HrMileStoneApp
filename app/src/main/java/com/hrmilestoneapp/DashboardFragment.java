package com.hrmilestoneapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hrmilestoneapp.utils.PreferenceManager;

import java.util.ArrayList;


public class DashboardFragment extends Fragment {
    Button fabDashboard;
    EditText inputDashboard;
    ListView listDashboard;
    private String userKey;
    DatabaseReference mDatabase;
    private String senderName;
    String date;
    String time;
    private ArrayList<DashboardModel> dashboardMessageArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_dashboard, container, false);
        final View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        inputDashboard = (EditText) rootView.findViewById(R.id.input_dashboard);
        fabDashboard = (Button) rootView.findViewById(R.id.fab_dashboard);
        listDashboard = (ListView) rootView.findViewById(R.id.list_of_messages_dashboard);

        userKey = PreferenceManager.getprefUserKey(getActivity());
        //userKey = PreferenceManager.getprefUserKey(getActivity());

        senderName = PreferenceManager.getprefUserFirstName(getActivity());
        Log.e("userKey ", "userKey " + userKey);

        Log.e("fname", "fname  : " + senderName);
        fabDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputDashboard.getText().toString().trim().isEmpty()) {
                    inputDashboard.setError("Please write something to Post");
                    return;
                } else {
                    mDatabase = FirebaseDatabase.getInstance().getReference("Dashboard");
                    String messageId = mDatabase.push().getKey();
                    String messageKey = PreferenceManager.getprefUserKey(getActivity());
                    date = DateTime.Date();
                    time = DateTime.Time();

                    DashboardModel dbm = new DashboardModel();
                    dbm.setSenderId(userKey);
                    dbm.setDashboardMessages(inputDashboard.getText().toString());
                    dbm.setSenderName(senderName);
                    dbm.setDate(date);
                    dbm.setTime(time);
                    mDatabase.child(messageId).setValue(dbm);
                    inputDashboard.getText().clear();
                }
            }
        });
        dashboardMessageArrayList = new ArrayList<DashboardModel>();
        DatabaseReference mDatabase1 = FirebaseDatabase.getInstance().getReference("Dashboard");
        mDatabase1.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    DashboardModel dbm = snapshot.getValue(DashboardModel.class);

                    String dMessage = dbm.getDashboardMessages();
                    String dname = dbm.getSenderName();
                    String did = dbm.getSenderId();
                    dashboardMessageArrayList.add(dbm);
                    setdata(dashboardMessageArrayList);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        return rootView;

    }

    private void setdata(ArrayList<DashboardModel> dashboardMessageArrayList) {
        DashboardAdapter mAdapter = new DashboardAdapter(getActivity(), dashboardMessageArrayList);
        listDashboard.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
