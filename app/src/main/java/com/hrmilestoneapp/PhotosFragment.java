package com.hrmilestoneapp;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hrmilestoneapp.utils.PreferenceManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class PhotosFragment extends Fragment implements View.OnClickListener {

    FloatingActionButton fab;
    String encoded;
    Photos photos;
    String date;
    String time;


    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    //ImageView imgtemp;

    ArrayList<Photos> photosArrayList;

    View view;
    String userFname;
    String userLname;
    Dialog dialog;
    Button btn_photo_take_picture, btn_photo_choose_from_library;

    GridView grdPhotos;
    DatabaseReference fref;
    String photoId;
    String userId;
    //int[] imgData = {R.drawable.temp, R.drawable.tempp, R.drawable.temppp, R.drawable.tempppp};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        userId = PreferenceManager.getprefUserKey(getActivity());
        userFname = PreferenceManager.getprefUserFirstName(getActivity());
        userLname = PreferenceManager.getprefUserLastName(getActivity());
        view = inflater.inflate(R.layout.fragment_photos, container, false);

        //imgtemp = view.findViewById(R.id.imgtemp);

        grdPhotos = view.findViewById(R.id.grdPhotos);
        /*grdPhotos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.photos_dialog);
                TextView tv_photo_upload_time = dialog.findViewById(R.id.tv_photo_upload_time);
                TextView tv_photos_upload_name = dialog.findViewById(R.id.tv_photos_upload_name);
                tv_photo_upload_time.setText(photos.getTime() + " " + photos.getDate());
                tv_photos_upload_name.setText(photos.getUserFName() + " " + photos.getUserLName());

                dialog.show();

                //Toast.makeText(getActivity(), "" + photos.getUserId(), Toast.LENGTH_LONG).show();
            }
        });*/

        FirebaseDatabase fdatabase = FirebaseDatabase.getInstance();
        fref = fdatabase.getReference("photos");
        photosArrayList = new ArrayList<Photos>();
        photoId = fref.push().getKey();

        fref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    photos = snapshot.getValue(Photos.class);
                    photosArrayList.add(photos);
                    setData(photosArrayList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(PhotosFragment.this);

        return view;
    }

    private void setData(ArrayList<Photos> photosArrayList) {
        PhotosAdapter photosAdapter = new PhotosAdapter(getActivity(), photosArrayList);
        //photosAdapter.notifyDataSetChanged();
        grdPhotos.setAdapter(photosAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                //photoDialog();
                selectImage();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                    Snackbar.make(view, "Permissions not granted", Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }


    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getActivity());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                } else if (items[item].equals("Remove Photo")) {
                    userChoosenTask = "Choose from Library";
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.hrlist_icon);
                    //imgtemp.setImageResource(R.drawable.event_pic);
                    Log.i("imagePath", "imagePath : " + bitmap.toString());
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] byteArray = bytes.toByteArray();
        //imgtemp.setImageBitmap(thumbnail);
        encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        //Firebase insert
        Photos photos = new Photos();
        date = DateTime.Date();
        time = DateTime.Time();
        photos.setUserId(userId);
        photos.setPhotoPath(encoded);
        photos.setDate(date);
        photos.setTime(time);
        photos.setUserFName(userFname);
        photos.setUserLName(userLname);
        fref.child(photoId).setValue(photos);
        Log.i("encoded", "encoded : " + encoded);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());

                //Bitmap bm = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                byte[] byteArray = bytes.toByteArray();
                //imgtemp.setImageBitmap(bm);
                encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //imgtemp.setImageBitmap(bm);
        Photos photos = new Photos();
        String date1 = DateTime.Date();
        String time1 = DateTime.Time();
        photos.setUserId(userId);
        photos.setPhotoPath(encoded);
        photos.setDate(date1);
        photos.setTime(time1);
        photos.setUserFName(userFname);
        photos.setUserLName(userLname);
        fref.child(photoId).setValue(photos);
        Log.i("imagePath", "imagePath : " + bm.toString());
    }
}
