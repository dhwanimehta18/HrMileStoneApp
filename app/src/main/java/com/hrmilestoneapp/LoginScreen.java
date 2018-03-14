package com.hrmilestoneapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hrmilestoneapp.network.ApiManager;
import com.hrmilestoneapp.network.RequestCode;
import com.hrmilestoneapp.network.RequestParam;
import com.hrmilestoneapp.utils.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import hp.harsh.library.interfaces.OkHttpInterface;
import hp.harsh.library.okhttp.OkHttpRequest;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener, OkHttpInterface {

    TextInputEditText et_email, et_password;
    Button btn_login, btn_reset_password, btn_signup;
    private ProgressBar progressBar;
    private static String TAG = SignUpScreen.class.getName();

    FirebaseDatabase fdatabase = FirebaseDatabase.getInstance();
    DatabaseReference fref;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        fref = fdatabase.getReference();
        userId = fref.push().getKey();

        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        btn_reset_password = findViewById(R.id.btn_reset_password);
        btn_signup = findViewById(R.id.btn_signup);
        progressBar = findViewById(R.id.progressBar);

        btn_signup.setOnClickListener(LoginScreen.this);
        btn_reset_password.setOnClickListener(LoginScreen.this);
        btn_login.setOnClickListener(LoginScreen.this);
    }

    @Override
    public void onClick(final View view) {

        Intent next = null;

        switch (view.getId()) {
            case R.id.btn_login:
                if (!validateEmail()) {
                    return;
                } else if (!validatePassword()) {
                    return;
                } else {
                    fref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                User user = snapshot.getValue(User.class);
                                String fEmail = user.getUser_email();
                                String fPassword = user.getUser_password();

                                if (fEmail.equals(et_email.getText().toString().trim()) && fPassword.equals(et_password.getText().toString().trim())) {
                                    String fname = user.getUser_fname();
                                    String lastname = user.getUser_lname();

                                    PreferenceManager.setPref(LoginScreen.this, fname, "USER_FIRSTNAME");
                                    PreferenceManager.setPref(LoginScreen.this, lastname, "USER_LASTNAME");
                                    PreferenceManager.setPref(LoginScreen.this, fEmail, "USER_EMAIL");

                                    Intent next = new Intent(LoginScreen.this, MainActivity.class);
                                    startActivity(next);
                                } else if (fEmail.equals(et_email.getText().toString().trim()) && !fPassword.equals(et_password.getText().toString().trim())) {
                                    Snackbar.make(view, "Password is incorrect", Snackbar.LENGTH_LONG).show();
                                } else if (!fEmail.equals(et_email.getText().toString().trim()) && fPassword.equals(et_password.getText().toString().trim())) {
                                    Snackbar.make(view, "Email is incorrect", Snackbar.LENGTH_LONG).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    //callAPIforLogin();
                    next = new Intent(LoginScreen.this, MainActivity.class);
                    startActivity(next);
                    break;
                }
            case R.id.btn_reset_password:
                break;
            case R.id.btn_signup:
                next = new Intent(LoginScreen.this, SignUpScreen.class);
                startActivity(next);
                break;
        }
    }

    private void callAPIforLogin() {

        new OkHttpRequest(LoginScreen.this,
                OkHttpRequest.Method.POST,
                ApiManager.USER_LOGIN,
                RequestParam.userLogin("" + et_email.getText().toString(), "" + et_password.getText().toString()),
                RequestParam.getNull(),
                RequestCode.CODE_USER_LOGIN,
                true, this);
    }

    private boolean validatePassword() {
        if (et_password.getText().toString().trim().length() < 6) {
            et_password.setError(getString(R.string.passworderror));
            return false;
        }
        return true;
    }

    private boolean validateEmail() {
        String email = et_email.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            et_email.setError(getString(R.string.emailerror));
            return false;
        }
        return true;
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onOkHttpStart(int requestId) {

    }

    @Override
    public void onOkHttpSuccess(int requestId, int statusCode, String response) {

        switch (requestId) {
            case RequestCode.CODE_USER_LOGIN:
                try {
                    JSONObject root = new JSONObject("" + response);

                    int responseCode = root.getInt("success");
                    final String responseMessage = root.getString("message");

                    if (responseCode == RequestCode.SUCCESS_CODE) {

                        if (root.has("user_id")) {

                            String mStrUserId = root.getString("user_id");
                            Log.e(TAG, "user_id==" + mStrUserId);
                            PreferenceManager.setPref(LoginScreen.this, mStrUserId, "USER_ID");

                        }

                        if (root.has("user_fname")) {
                            String mStrFirstName = root.getString("user_fname");
                            Log.e(TAG, "user_fname==" + mStrFirstName);
                            PreferenceManager.setPref(LoginScreen.this, mStrFirstName, "USER_FIRSTNAME");

                        }
                        if (root.has("user_lname")) {
                            String mStrLastName = root.getString("user_lname");
                            Log.e(TAG, "user_lname==" + mStrLastName);
                            PreferenceManager.setPref(LoginScreen.this, mStrLastName, "USER_LASTNAME");

                        }
                        if (root.has("user_email")) {

                            String mStrEmail = root.getString("user_email");
                            Log.e(TAG, "user_email==" + mStrEmail);
                            PreferenceManager.setPref(LoginScreen.this, mStrEmail, "USER_EMAIL");

                        }

                        this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginScreen.this, "" + responseMessage, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginScreen.this, MainActivity.class));
                                finish();
                            }
                        });

                        //mPreferenceManager.saveLoginData(id,first_name,last_name,email_id);

                        // keep this flag true if user login with social account

                        // Redirect to dashboard or HomeActivity

                    } else {
                        Toast.makeText(LoginScreen.this, "" + responseMessage, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onOkHttpFailure(int requestId, int statusCode, String response, Throwable error) {

    }

    @Override
    public void onOkHttpFinish(int requestId) {

    }
}