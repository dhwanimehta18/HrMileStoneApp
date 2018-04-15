package com.hrmilestoneapp;

import android.os.Bundle;
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
import com.hrmilestoneapp.utils.PreferenceManager;

import java.util.ArrayList;


public class MessegeFragment extends Fragment {
    DatabaseReference fref;
    View v;
    private String userId;
    private ArrayList<User> userArrayList;
    ListView lst_hrlist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_messege, container, false);
        final String userEmaiId = PreferenceManager.getprefUserEmail(getActivity());

        FirebaseDatabase fdatabase = FirebaseDatabase.getInstance();
        lst_hrlist = v.findViewById(R.id.lst_hrlist);
        fref = fdatabase.getReference("user");
        // userId = fref.push().getKey();
        userArrayList = new ArrayList<User>();
        fref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    User user = snapshot.getValue(User.class);
                    String emailId = user.getUser_email();
                    String fname = user.getUser_fname();
                    String lname = user.getUser_lname();
                    Log.e("MessegeFragment", "" + fname);
                    Log.e("userEmaiId","userEmaiId"+userEmaiId);
                    if ( !userEmaiId.equals(emailId)){
                        Log.e("MessegeFragment", "" + emailId);
                        userArrayList.add(user);
                        setData(userArrayList);

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return v;

    }

    private void setData(ArrayList<User> userArrayList) {

        HRListAdapter hrListAdapter = new HRListAdapter(getActivity(), userArrayList);
        lst_hrlist.setAdapter(hrListAdapter);
        hrListAdapter.notifyDataSetChanged();
    }
}
