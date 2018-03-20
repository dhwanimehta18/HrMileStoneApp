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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;

    FirebaseDatabase fdatabase = FirebaseDatabase.getInstance();
    DatabaseReference fref;
    String userId;

    String fname, lname, email, contact, company, experience, birthdate, gender;

    TextView user_profile_fname, user_profile_lname, user_profile_email, tv_user_profile_contact, tv_user_profile_company;
    TextView tv_user_profile_exp, tv_user_gender, tv_user_birthdate;
    EditText et_user_profile_exp, et_user_gender, et_user_birthdate;
    EditText user_profile_f_name, user_profile_l_name, profile_email, et_user_profile_contact, et_user_profile_company;
    Button btn_profile_save;

    CircleImageView edit_profile_image;
    Dialog dialog;
    Button btn_edit_profile_picture, btn_remove_picture, btn_take_picture, btn_choose_from_library, btn_cancel_profile_pic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        fref = fdatabase.getReference("user");
        userId = fref.push().getKey();

        Log.i("usreId", "userId : " + userId);


        et_user_profile_exp = findViewById(R.id.et_user_profile_exp);
        et_user_gender = findViewById(R.id.et_user_gender);
        et_user_birthdate = findViewById(R.id.et_user_birthdate);
        user_profile_f_name = findViewById(R.id.user_profile_f_name);
        user_profile_l_name = findViewById(R.id.user_profile_l_name);
        profile_email = findViewById(R.id.profile_email);
        et_user_profile_contact = findViewById(R.id.et_user_profile_contact);
        et_user_profile_company = findViewById(R.id.et_user_profile_company);

        btn_profile_save = findViewById(R.id.btn_profile_save);

        user_profile_fname = findViewById(R.id.user_profile_fname);
        user_profile_lname = findViewById(R.id.user_profile_lname);
        user_profile_email = findViewById(R.id.user_profile_email);
        tv_user_profile_contact = findViewById(R.id.tv_user_profile_contact);
        tv_user_profile_company = findViewById(R.id.tv_user_profile_company);
        tv_user_profile_exp = findViewById(R.id.tv_user_profile_exp);
        tv_user_gender = findViewById(R.id.tv_user_gender);
        tv_user_birthdate = findViewById(R.id.tv_user_birthdate);


        btn_edit_profile_picture = findViewById(R.id.btn_edit_profile_picture);

        /*fref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    fname = user.getUser_fname();
                    Log.i("fname", "fname : " + fname);
                    lname = user.getUser_lname();
                    email = user.getUser_email();
                    contact = user.getUser_contact();
                    company = user.getUser_company();
                    experience = user.getUser_experience();
                    gender = user.getUser_gender();
                    birthdate = user.getUser_birthdate();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        et_user_birthdate.setText(birthdate);
        user_profile_f_name.setText(fname);
        user_profile_l_name.setText(lname);
        profile_email.setText(email);
        et_user_profile_contact.setText(contact);
        et_user_profile_company.setText(company);
        et_user_profile_exp.setText(experience);
        et_user_gender.setText(gender);*/

        edit_profile_image = findViewById(R.id.edit_profile_image);

        edit_profile_image.setOnClickListener(EditProfileActivity.this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_profile_image:
                selectImage();
                //customDialog();
                break;
            case R.id.btn_edit_profile_picture:
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
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Remove Photo", "Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(EditProfileActivity.this);

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
                    edit_profile_image.setImageResource(R.drawable.hrlist_icon);
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
        intent.setAction(Intent.ACTION_GET_CONTENT);//
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

        edit_profile_image.setImageBitmap(thumbnail);
        Log.i("imagePath", "imagePath : " + thumbnail.toString());
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        edit_profile_image.setImageBitmap(bm);
        Log.i("imagePath", "imagePath : " + bm.toString());
    }




    /*private void customDialog() {

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
        btn_choose_from_library.setOnClickListener(EditProfileActivity.this);
        btn_remove_picture.setOnClickListener(EditProfileActivity.this);

    }*/
}
