package com.hrmilestoneapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SignUpScreen extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText user_fname, user_lname, user_email, user_password, txtEdit_cnf_pwd, user_contact;
    Button btn_signup;
    EditText user_birthdate;
    final Calendar myCalendar = Calendar.getInstance();
    RadioGroup rdgrp;
    RadioButton rdb;
    Spinner txtInput_cmpyName, txtInput_exp;

    FirebaseDatabase fdatabase = FirebaseDatabase.getInstance();
    DatabaseReference fref;

    AwesomeValidation awesomeValidation;

    View view;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);

        fref = fdatabase.getReference();
        userId = fref.push().getKey();

        user_fname = findViewById(R.id.et_firstname);
        user_lname = findViewById(R.id.et_lastname);
        user_email = findViewById(R.id.txtEdit_email);
        user_password = findViewById(R.id.txtEdit_pwd);
        txtEdit_cnf_pwd = findViewById(R.id.txtEdit_cnf_pwd);
        user_contact = findViewById(R.id.txtEdit_number);
        rdgrp = findViewById(R.id.rdgrp);
        txtInput_cmpyName = findViewById(R.id.txtInput_cmpyName);
        txtInput_exp = findViewById(R.id.txtInput_exp);

        btn_signup = findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(SignUpScreen.this);

        user_birthdate = findViewById(R.id.birthdate);
        user_birthdate.setOnClickListener(SignUpScreen.this);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        //awesomeValidation.addValidation(this, R.id.txtEdit_number, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.numbererror);

    }

    @Override
    public void onClick(View view) {
        int btnId = rdgrp.getCheckedRadioButtonId();
        rdb = (RadioButton) findViewById(btnId);
        Intent next = null;
        switch (view.getId()) {
            case R.id.btn_signup:

                if (!validateName()) {
                    return;
                } else if (!validateEmail()) {
                    return;
                } else if (!validatePassword()) {
                    return;
                } else if (!validateConfirmPassword()) {
                    return;
                } else if (!awesomeValidation.validate()) {
                    return;
                } else {

                    User user = new User();
                    user.setUser_fname(user_fname.getText().toString().trim());
                    user.setUser_lname(user_lname.getText().toString().trim());
                    user.setUser_email(user_email.getText().toString().trim());
                    user.setUser_password(user_password.getText().toString().trim());
                    user.setUser_contact(user_contact.getText().toString().trim());
                    user.setUser_gender(rdb.getText().toString().trim());
                    user.setUser_birthdate(user_birthdate.getText().toString().trim());

                    fref.child(userId).setValue(user);

                    next = new Intent(SignUpScreen.this, LoginScreen.class);
                    startActivity(next);
                }
                break;
            case R.id.birthdate:
                DatePickerDialog dialog = new DatePickerDialog(SignUpScreen.this, android.R.style.Theme_Holo_Dialog, new DatePickerDialog.OnDateSetListener() {
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

                dialog.show();
        }
    }


    private boolean validateConfirmPassword() {
        if (!user_password.getText().toString().trim().equals(txtEdit_cnf_pwd.getText().toString().trim())) {
            txtEdit_cnf_pwd.setError(getString(R.string.cnfpassworderror));
            return false;
        }
        return true;
    }

    private boolean validatePassword() {
        if (user_password.getText().toString().trim().length() < 6) {
            user_password.setError(getString(R.string.passworderror));
            return false;
        }
        return true;
    }

    private boolean validateEmail() {
        String email = user_email.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            user_email.setError(getString(R.string.emailerror));
            return false;
        }
        return true;
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        user_birthdate.setText(sdf.format(myCalendar.getTime()));
    }

    private boolean validateName() {
        if (user_fname.getText().toString().trim().length() < 2) {
            user_fname.setError(getString(R.string.nameerror));
            return false;
        }
        if (user_lname.getText().toString().trim().length() < 2) {
            user_lname.setError(getString(R.string.nameerror));
            return false;
        }
        return true;
    }

}
