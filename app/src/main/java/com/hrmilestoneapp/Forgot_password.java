package com.hrmilestoneapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_password extends AppCompatActivity {
    Button submit;
    EditText email1;
    String email;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mAuth = FirebaseAuth.getInstance();
        email1 = (EditText) findViewById(R.id.textView_forgot_password);
        email = "jinay2721@gmail.com";
        submit = (Button) findViewById(R.id.button_forgot_password);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
//                 TODO Auto-generated method stub
                Log.e("email", "email" + email1.getText().toString().trim());

                String email = email1.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Forgot_password.this, "Enter your registered email id", Toast.LENGTH_SHORT).show();
                    return;
                }

                //progressBar.setVisibility(View.VISIBLE);
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Forgot_password.this, "we have sent you the instructions to reset your password", Toast.LENGTH_SHORT).show();
                            Intent next = new Intent(Forgot_password.this, LoginScreen.class);
                            startActivity(next);
                            finish();

                        } else {
                            Toast.makeText(Forgot_password.this, "Failed to send reset email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });




           /*

                try {
                    Log.e("onClick","onClick");
                    GMailSender sender = new GMailSender("jinay2721@gmail.com", "H1jinayS");
                    sender.sendMail("This is Subject",
                            "This is Body",
                            "jinay2721@gmail.com",
                            "dhwani2901@gmail.com");
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }*/

             /*   new Thread(new Runnable() {

                    public void run() {

                        try {

                            GMailSender sender = new GMailSender(

                                    "jinay2721@gmail.com",

                                    "H1jinayS");

                          //  sender.addAttachment(Environment.getExternalStorageDirectory().getPath()+"/image.jpg");

                            sender.sendMail("Test mail", "This mail has been sent from android app along with attachment",

                                    "jinay2721@gmail.com",

                                    "dhwani2901@gmail.com");

                        } catch (Exception e) {

                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();



                        }

                    }

                }).start();
*/
            }


        });
    }
   /* private class SendMail extends AsyncTask<String, Integer, Void> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Forgot_password.this, "Please wait", "Sending mail", true, false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }

        protected Void doInBackground(String... params) {
            Mail m = new Mail("dhwani2901@gmail.com", "Dhwani@29");

            String[] toArr = {"dhwani2901@gmail.com", "dhwani2901@gmail.com"};
            m.setTo(toArr);
            m.setFrom("dhwani2901@gmail.com");
            m.setSubject("This is an email sent using my Mail JavaMail wrapper from an Android device.");
            m.setBody("Email body.");

            try {
                if(m.send()) {
                    Toast.makeText(Forgot_password.this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(Forgot_password.this, "Email was not sent.", Toast.LENGTH_LONG).show();
                }
            } catch(Exception e) {
                Log.e("MailApp", "Could not send email", e);
            }
            return null;
        }
    }*/
}

