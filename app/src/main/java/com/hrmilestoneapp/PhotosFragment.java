package com.hrmilestoneapp;


import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class PhotosFragment extends Fragment implements View.OnClickListener {

    FloatingActionButton fab;
    Dialog dialog;
    Button btn_photo_take_picture, btn_photo_choose_from_library;

    public PhotosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photos, container, false);

        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(PhotosFragment.this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                photoDialog();
                break;
        }
    }

    private void photoDialog() {
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.photos_dialog);
        btn_photo_choose_from_library = dialog.findViewById(R.id.btn_photos_choose_from_library);
        btn_photo_take_picture = dialog.findViewById(R.id.btn_photos_take_picture);

        dialog.show();
    }
}
