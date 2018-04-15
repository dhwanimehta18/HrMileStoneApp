package com.hrmilestoneapp.utils;

import android.content.Context;
import android.content.SharedPreferences;



public class /**/PreferenceManager {

    public static PreferenceManager preferenceManager;
    private static SharedPreferences mSharedPreferences;
    private  static SharedPreferences.Editor mEditor;
    public static String SHAREDPREFERENCE = "MY_SHAREDPREFERENCE";
    public static String IS_USER_LOGGED_IN = "isUserLoggedIn";


    public static void removePref(Context context, String keyToRemove) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mEditor.remove(keyToRemove);
        mEditor.commit();

    }

    public static void setPref(Context context, String value, String key) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mEditor.putString(key, value);
        mEditor.commit();
    }

    public static String getprefUserKey(Context context) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE);
        String strUserkey = mSharedPreferences.getString("USER_KEY", "");
        return strUserkey;
    }
    public static String getprefUserPassword(Context context) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE);
        String strUserPass = mSharedPreferences.getString("USER_PASS", "");
        return strUserPass;
    }
    public static String getprefImagepath(Context context) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE);
        String strImpath = mSharedPreferences.getString("IMGPATH", "");
        return strImpath;
    }

    public static String getprefUserId(Context context) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE);
        String strUserId = mSharedPreferences.getString("USER_ID", "");
        return strUserId;
    }

    public static String getprefUserFirstName(Context context) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE);
        String strUserFirstName = mSharedPreferences.getString("USER_FIRSTNAME", "");
        return strUserFirstName;
    }
    public static String getprefUserLastName(Context context) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE);
        String strUserLastName = mSharedPreferences.getString("USER_LASTNAME", "");
        return strUserLastName;
    }
    public static String getprefUserEmail(Context context) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE);
        String strUserEmail = mSharedPreferences.getString("USER_EMAIL", "");
        return strUserEmail;
    }

    public static String getprefCheckEmail(Context context) {
        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE);
        String strUserEmail = mSharedPreferences.getString("USER_CHECK_EMAIL", "");
        return strUserEmail;
    }

    public static void saveUserLoggedIn(Context context, boolean isLoggedIn, String isUserLoggedIn) {

        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mEditor.putBoolean(isUserLoggedIn, isLoggedIn);
        mEditor.commit();
    }
    public static Boolean isUserLoggedIn(Context context) {

        mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE);
        Boolean bisUserLoggedIn = mSharedPreferences.getBoolean("isUserLoggedIn", false);
        return bisUserLoggedIn;
    }



}
