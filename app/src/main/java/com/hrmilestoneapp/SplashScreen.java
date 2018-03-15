package com.hrmilestoneapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.hrmilestoneapp.utils.PreferenceManager;

public class SplashScreen extends AppCompatActivity {

    ImageView imgSplash;
    String user_email;
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user_email = PreferenceManager.getprefUserEmail(SplashScreen.this);
        Log.i("user_id", user_email);


        setContentView(R.layout.activity_splash_screen);
        imgSplash = (ImageView) findViewById(R.id.imgSplash);
        imgSplash.setImageResource(R.drawable.logo);


        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {

                if (!user_email.equals("") && !user_email.isEmpty()) {
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent intent = new Intent(SplashScreen.this, LoginScreen.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, SPLASH_TIME_OUT);
    }


}

