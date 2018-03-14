package com.hrmilestoneapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.net.URI;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private URI mMediaUri;
    public static final int MEDIA_TYPE_IMAGE = 2;

    CircleImageView edit_profile_image;
    Dialog dialog;
    Button btn_remove_picture, btn_take_picture, btn_choose_from_library, btn_cancel_profile_pic;
    private int TAKE_PIC_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        edit_profile_image = findViewById(R.id.edit_profile_image);

        edit_profile_image.setOnClickListener(EditProfileActivity.this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_profile_image:
                customDialog();
                break;
            case R.id.btn_cancel_profile_pic:
                dialog.dismiss();
                break;
            case R.id.btn_take_picture:
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                if (mMediaUri == null) {
                    Toast.makeText(getApplicationContext(), "Sorry there was an error! Try again.", Toast.LENGTH_LONG).show();
                    //mSaveChangesBtn.setEnabled(false);
                } else {
                    takePicture.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                    startActivityForResult(takePicture, TAKE_PIC_REQUEST_CODE);

                    //mSaveChangesBtn.setEnabled(true);
                }
                break;
        }
    }

    private URI getOutputMediaFileUri(int mediaTypeImage) {

        if(isExternalStorageAvailable()){
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "UPLOADIMAGES");
        }

        return null;
    }

    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    private void customDialog() {

        dialog = new Dialog(EditProfileActivity.this);
        dialog.setContentView(R.layout.bottom_sheet);
        dialog.setTitle("Change Profile Photo");
        btn_remove_picture = dialog.findViewById(R.id.btn_remove_picture);
        btn_take_picture = dialog.findViewById(R.id.btn_take_picture);
        btn_choose_from_library = dialog.findViewById(R.id.btn_choose_from_library);
        btn_cancel_profile_pic = dialog.findViewById(R.id.btn_cancel_profile_pic);
        dialog.show();

        btn_cancel_profile_pic.setOnClickListener(EditProfileActivity.this);
        btn_take_picture.setOnClickListener(EditProfileActivity.this);

    }
}
