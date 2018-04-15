package com.hrmilestoneapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ModifyEvent extends AppCompatActivity implements View.OnClickListener {
    EditText et_event_modify_name, et_event_modify_agenda, et_event_modify_description, et_event_modify_date,
            et_event_modify_time, et_event_modify_venue;
    ImageView img_event_modify_banner;
    Button btn_modify_event, btn_delete_event;
    DatabaseReference fref;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    String encoded;

    private String USER_KEY;
    private String imgpath;
    final Calendar myCalendar = Calendar.getInstance();
    private String am_pm;
    private String eventId;
    private String e_agenda;
    private String e_date;
    private String e_desc;
    private String e_name;
    private String e_time;
    private String e_venue;
    private String e_banner;
    private String EVENT_ID;

    static final int TIME_DIALOG_ID = 1111;
    private int hour;
    private int minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_event);
        FirebaseDatabase fdatabase = FirebaseDatabase.getInstance();
        fref = fdatabase.getReference("event");
        eventId = fref.push().getKey();

        final Bundle bundle = getIntent().getExtras();
        EVENT_ID = bundle.getString("EVENT_ID");

        String user_id = bundle.getString("USER_ID");
        Log.i("user_id", "user_id : " + user_id);

        USER_KEY = PreferenceManager.getprefUserKey(this);
        Log.i("USER_KEY", "USER_KEY : " + USER_KEY);

        et_event_modify_agenda = findViewById(R.id.et_event_modify_agenda);
        et_event_modify_date = findViewById(R.id.et_event_modify_date);
        et_event_modify_description = findViewById(R.id.et_event_modify_description);
        et_event_modify_time = findViewById(R.id.et_event_modify_time);
        et_event_modify_venue = findViewById(R.id.et_event_modify_venue);
        img_event_modify_banner = findViewById(R.id.img_modify_event_banner);
        et_event_modify_name = findViewById(R.id.et_event_modify_name);
        btn_modify_event = findViewById(R.id.btn_modify_event);
        btn_delete_event = findViewById(R.id.btn_delete_event);

        btn_modify_event.setOnClickListener(ModifyEvent.this);
        et_event_modify_time.setOnClickListener(ModifyEvent.this);
        et_event_modify_date.setOnClickListener(ModifyEvent.this);
        img_event_modify_banner.setOnClickListener(ModifyEvent.this);
        btn_delete_event.setOnClickListener(ModifyEvent.this);

        if (!user_id.equals(USER_KEY)) {
            btn_modify_event.setVisibility(View.GONE);
            btn_delete_event.setVisibility(View.GONE);
            et_event_modify_name.setInputType(InputType.TYPE_NULL);
            et_event_modify_agenda.setInputType(InputType.TYPE_NULL);
            et_event_modify_date.setInputType(InputType.TYPE_NULL);
            et_event_modify_description.setInputType(InputType.TYPE_NULL);
            et_event_modify_time.setInputType(InputType.TYPE_NULL);
            et_event_modify_venue.setInputType(InputType.TYPE_NULL);
            img_event_modify_banner.setClickable(false);
        }

        fref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Events events = snapshot.getValue(Events.class);
                    String fEvent = events.getEvent_id();

                    if (bundle.getString("EVENT_ID").equals(fEvent)) {
                        e_agenda = events.getEvent_agenda();
                        e_date = events.getEvent_date();
                        e_desc = events.getEvent_description();
                        e_name = events.getEvent_name();
                        e_time = events.getEvent_time();
                        e_venue = events.getEvent_venue();
                        e_banner = events.getImg_banner();
                        imgpath = events.getImg_banner();

                        PreferenceManager.setPref(ModifyEvent.this, imgpath, "IMAGEPATH");
                        if (!(imgpath == null) && !(imgpath == "")) {
                            byte[] decoded = Base64.decode(imgpath, Base64.DEFAULT);
                            Bitmap decodedImage = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
                            img_event_modify_banner.setImageBitmap(decodedImage);
                            Log.e("decoded", "decoded :" + decodedImage);
                        }
                        et_event_modify_agenda.setText(e_agenda);
                        et_event_modify_date.setText(e_date);
                        et_event_modify_description.setText(e_desc);
                        et_event_modify_name.setText(e_name);
                        et_event_modify_time.setText(e_time);
                        et_event_modify_venue.setText(e_venue);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void updateTime(int hours, int mins) {
        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        et_event_modify_time.setText(aTime);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:

                // set time picker as current time
                return new TimePickerDialog(this, timePickerListener, hour, minute,
                        false);

        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour = hourOfDay;
            minute = minutes;

            updateTime(hour, minute);

        }

    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_modify_event:

                updateFirebaseEvent();

                Toast.makeText(this, "Event modified", Toast.LENGTH_LONG).show();
                break;
            case R.id.et_event_modify_date:
                DatePickerDialog dialog = new DatePickerDialog(ModifyEvent.this, android.R.style.Theme_Holo_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, month);
                        myCalendar.set(Calendar.DAY_OF_MONTH, day);
                        updateLabel();

                        //Todo your work here
                    }
                }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.getDatePicker().setMinDate(new Date().getTime());
                dialog.show();
                break;

            case R.id.et_event_modify_time:
                showDialog(TIME_DIALOG_ID);

                break;

            case R.id.img_modify_event_banner:
                selectImage();
                break;

            case R.id.btn_delete_event:
                dialogAlert();
                break;
        }
    }

    private void dialogAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(getString(R.string.event_delete_alert));
        alertDialogBuilder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                fref.child(EVENT_ID).removeValue();
                Toast.makeText(ModifyEvent.this, "Event deleted", Toast.LENGTH_LONG);
                startActivity(new Intent(ModifyEvent.this, MainActivity.class));
            }
        });
        alertDialogBuilder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void updateFirebaseEvent() {

        // et_event_modify_venue;
        //btn_modify_event

        fref.child(EVENT_ID).child("event_agenda").setValue(et_event_modify_agenda.getText().toString().trim());
        fref.child(EVENT_ID).child("event_date").setValue(et_event_modify_date.getText().toString().trim());
        fref.child(EVENT_ID).child("event_description").setValue(et_event_modify_description.getText().toString().trim());
        fref.child(EVENT_ID).child("event_name").setValue(et_event_modify_name.getText().toString().trim());
        fref.child(EVENT_ID).child("event_time").setValue(et_event_modify_time.getText().toString().trim());
        fref.child(EVENT_ID).child("event_venue").setValue(et_event_modify_venue.getText().toString().trim());
        fref.child(EVENT_ID).child("img_banner").setValue(encoded);

        Toast.makeText(ModifyEvent.this, "Event successfully modified", Toast.LENGTH_LONG);
        startActivity(new Intent(ModifyEvent.this, MainActivity.class));
        finish();
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        et_event_modify_date.setText(sdf.format(myCalendar.getTime()));
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ModifyEvent.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(ModifyEvent.this);

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
                    img_event_modify_banner.setImageResource(R.drawable.event_pic);
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
        img_event_modify_banner.setImageBitmap(thumbnail);
        encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        Log.i("encoded", "encoded : " + encoded);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());

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
    }
}
