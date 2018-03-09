package com.hrmilestoneapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    CircleImageView profile_image;
    TextView tv_follower_number, tv_following_number, tv_follower_text, tv_following_text;
    Button btn_edit_profile;
    ListView lst_profile_timeline;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profile_image = view.findViewById(R.id.profile_image);

        tv_follower_number = view.findViewById(R.id.tv_follower_number);
        tv_following_number = view.findViewById(R.id.tv_following_number);
        tv_follower_text = view.findViewById(R.id.tv_follower_text);
        tv_following_text = view.findViewById(R.id.tv_following_text);

        btn_edit_profile = view.findViewById(R.id.btn_edit_profile);

        lst_profile_timeline = view.findViewById(R.id.lst_profile_timeline);

        btn_edit_profile.setOnClickListener(ProfileFragment.this);

        return view;
    }

    @Override
    public void onClick(View view) {
        Intent next = null;
        switch (view.getId()){
            case R.id.btn_edit_profile:
                next = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(next);
                break;
        }
    }
}
