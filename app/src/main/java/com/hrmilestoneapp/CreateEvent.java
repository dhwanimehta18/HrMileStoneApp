package com.hrmilestoneapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hrmilestoneapp.utils.PreferenceManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateEvent extends AppCompatActivity implements View.OnClickListener {
    EditText et_eventName, et_event_agenda, et_event_description, et_event_date, et_event_time, et_event_venue;
    ImageView img_event_banner;
    Button btn_add_event;
    DatabaseReference fref;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    String encoded;
    final Calendar myCalendar = Calendar.getInstance();
    private String am_pm;
    private String eventId;

    String userId;

    Toolbar toolbar;
    Fragment fragment;

    static final int TIME_DIALOG_ID = 1111;
    private int hour;
    private int minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        userId = PreferenceManager.getprefUserKey(this);
        FirebaseDatabase fdatabase = FirebaseDatabase.getInstance();
        fref = fdatabase.getReference("event");
        eventId = fref.push().getKey();
        et_event_agenda = findViewById(R.id.et_event_agenda);
        et_event_date = findViewById(R.id.et_event_date);
        et_event_description = findViewById(R.id.et_event_description);
        et_event_time = findViewById(R.id.et_event_time);
        et_event_venue = findViewById(R.id.et_event_venue);
        et_eventName = findViewById(R.id.et_eventName);
        img_event_banner = findViewById(R.id.img_event_banner);
        btn_add_event = findViewById(R.id.btn_add_event);

        btn_add_event.setOnClickListener(CreateEvent.this);
        et_event_date.setOnClickListener(CreateEvent.this);
        et_event_time.setOnClickListener(CreateEvent.this);
        img_event_banner.setOnClickListener(CreateEvent.this);

        timeset();
    }

    private void timeset() {
        final Calendar c = Calendar.getInstance();
        // Current Hour
        hour = c.get(Calendar.HOUR_OF_DAY);
        // Current Minute
        minute = c.get(Calendar.MINUTE);

        // set current time into output textview
        updateTime(hour, minute);
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

        et_event_time.setText(aTime);
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
            case R.id.btn_add_event:

                if (et_eventName.getText().toString().trim().isEmpty()) {
                    et_eventName.setError("Enter Event Name");
                    return;
                } else if (et_event_agenda.getText().toString().trim().isEmpty()) {
                    et_event_agenda.setError("Enter Event Agenda");
                    return;
                } else if (et_event_venue.getText().toString().trim().isEmpty()) {
                    et_event_venue.setError("Enter Event Venue");
                    return;
                } else if (et_event_description.getText().toString().trim().isEmpty()) {
                    et_event_description.setError("Enter Event Description");
                    return;
                } else if (et_event_date.getText().toString().trim().isEmpty()) {
                    et_event_date.setError("Select Date of Event");
                    return;
                } else if (et_event_time.getText().toString().trim().isEmpty()) {
                    et_event_time.setError("Select Time of Event");
                } else {

                    Events events = new Events();
                    events.setUserId(userId);
                    events.setEvent_id(eventId);
                    events.setEvent_name(et_eventName.getText().toString());
                    events.setEvent_agenda(et_event_agenda.getText().toString().trim());
                    events.setEvent_date(et_event_date.getText().toString().trim());
                    events.setEvent_time(et_event_time.getText().toString().trim());
                    events.setEvent_description(et_event_description.getText().toString().trim());
                    events.setEvent_venue(et_event_venue.getText().toString().trim());
                    events.setImg_banner(encoded);
                    fref.child(eventId).setValue(events);

//                    toolbar.setTitle(R.string.title_profile);
                    // Intent i = new Intent(CreateEvent.this,EventsFragment.class);
                    //startActivity(i);
                    //  finish();

                   /* fragment = new EventsFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.frameLayout, fragment);
                    transaction.commit();*/
                    //loadFragment(fragment);
                    // finish();
                }
                break;
            case R.id.et_event_date:
                DatePickerDialog dialog = new DatePickerDialog(CreateEvent.this, android.R.style.Theme_Holo_Dialog, new DatePickerDialog.OnDateSetListener() {
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
                dialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
                dialog.show();
                break;

            case R.id.et_event_time:

                showDialog(TIME_DIALOG_ID);

                break;
            case R.id.img_event_banner:
                selectImage();
                break;
        }
    }

    /*private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }
*/
    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        et_event_date.setText(sdf.format(myCalendar.getTime()));
    }

    private void selectImage() {
        final CharSequence[] items = {getString(R.string.take_photo), getString(R.string.choose_from_library),
                getString(R.string.cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(CreateEvent.this);
        builder.setTitle(getString(R.string.add_photo));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(CreateEvent.this);

                if (items[item].equals(getString(R.string.take_photo))) {
                    if (result) {
                        userChoosenTask = getString(R.string.take_photo);
                        cameraIntent();
                    }

                } else if (items[item].equals(getString(R.string.choose_from_library))) {
                    userChoosenTask = getString(R.string.choose_from_library);
                    if (result)
                        galleryIntent();

                } else if (items[item].equals(getString(R.string.cancel))) {
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
        img_event_banner.setImageBitmap(thumbnail);
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
