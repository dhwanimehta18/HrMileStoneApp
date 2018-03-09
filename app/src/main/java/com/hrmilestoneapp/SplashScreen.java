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
    String user_id;
    private static int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user_id = PreferenceManager.getprefUserId(SplashScreen.this);
        Log.i("user_id", user_id);


        setContentView(R.layout.activity_splash_screen);
        imgSplash = (ImageView) findViewById(R.id.imgSplash);
        imgSplash.setImageResource(R.drawable.logo);



        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {

                if (!user_id.equals("") && !user_id.isEmpty()) {
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

