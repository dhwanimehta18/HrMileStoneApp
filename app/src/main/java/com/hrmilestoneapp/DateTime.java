package com.hrmilestoneapp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTime {

    public static String Date() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String currentDateTimeString = sdf.format(new Date());

        return currentDateTimeString;
    }


    public static String Time() {
        Calendar c = Calendar.getInstance();
        String time = new SimpleDateFormat("HH:mm").format(c.getTime());

        return time;

    }

    public  String dateStore() {
        Calendar c = Calendar.getInstance();

        int date = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        String Date = String.valueOf(date).concat(String.valueOf(month + 1)).concat(String.valueOf(year));

        return Date;
    }
    public static String dateTimeYear(){

        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
        String currentDateTimeString = sdf.format(new Date());
        String time= String.valueOf(System.currentTimeMillis());
        String finalString=currentDateTimeString.concat(time);

        return finalString;
    }


    public String timeStore() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);

        String str = String.valueOf(hour).concat(String.valueOf(minute).concat(String.valueOf(second)));

        return str;

    }



    public static Date dateTimeYearinDateFormat() {

        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy HH:mm:ss");
        String currentDateTimeString = sdf.format(new Date());
        Date dateObject = null;
        try {
            dateObject=sdf.parse(currentDateTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateObject;
    }



    public static String dateTimeYearformatter(){

        DateFormat formatter = new SimpleDateFormat("ddMMyyHHmmss");
        String dateObject = formatter.format(new Date(System.currentTimeMillis()));

        return dateObject;
    }




}
