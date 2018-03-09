package com.hrmilestoneapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Common {
    public static SharedPreferences mPrefs;
    public static SharedPreferences.Editor prefsEditor;
    public static boolean flagSubmitShipment = false;
    public static String strBusinessId = "1";
    public static boolean isDescChanged = false;
    public static ProgressDialog progressDialog = null;
    public static boolean isfromPush = false;
    public static String ID = null;
    public static String STATUS = null;
    public static String BidderList_offsetMain = "0";
    public static String BidderList_page = "0";


    public static int progressDialog_CustomerShipments_flag = 0;
    public static int progressDialog_CarrierShipments_flag = 0;
    public static int progressDialog_CarrierMyBids_flag = 0;
    public static int progressDialog_Notifications_flag = 0;


    public static void setStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#000000"));
        }
    }

    /*
    * A Common function to display toast.
    * */
    public static Void displayToast(Context context, String strToast) {
        Toast.makeText(context, strToast, Toast.LENGTH_SHORT).show();
        return null;
    }

    /*
    * A Common function to display Log.
    * */
    public static Void displayLog(String strTitle, String strText) {
        Log.d(strTitle, strText);
        return null;
    }

    /*
    * A Common function to check internet connection.
    * */
    public static boolean isOnline(Context c) {
        try {
            ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
            @SuppressLint("MissingPermission") NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

 /*
    * A Common function to hide keyboard
    * */

    public static void hideKeyboard(View view, Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /*
    * A common function to check length for input.
    * */
    public static boolean isValidLength(String fName) {
        if (fName.trim().length() > 0) {
            return true;
        }
        return false;
    }

    /*
   * A common function to validate Description for simple numbers >8  and @
   * */
    public static String isValidDecr(String strShipmentDesc) {

        int numberCount = 0;
        for (int i = 0; i < strShipmentDesc.length(); i++) {
            char c = strShipmentDesc.charAt(i);
            if (Character.isDigit(c)) {
                numberCount++;
            }
        }
        if (numberCount > 8) {
            isDescChanged = true;
        } else if (strShipmentDesc.contains("@")) {
            isDescChanged = true;
        }
        return strShipmentDesc;
    }


    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // validating of confirm password
    public static boolean isValidMatchPassword(String pass, String confirmPassword) {
        if (pass.equals(confirmPassword)) {
            return true;
        }
        return false;
    }

    /*
    * A Common function to hide soft-keyboard.
    * */
    public static void hideSoftKeyboard(Activity activity) {
        try {
            if (activity.getCurrentFocus().getWindowToken() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void setPref(Context context, String key, String value) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefsEditor = mPrefs.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public static String getPref(Context context, String key) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString(key, "");
    }

    /*[A-Z]{5}[0-9]{4}[A-Z]{1}
       * A common function to validate ZipCode.
       * */
    public static boolean isValidZipCode(String strZip) {
        String ZIP_PATTERN = "^[1-9][0-9]{5}$";
        Pattern pattern = Pattern.compile(ZIP_PATTERN);
        Matcher matcher = pattern.matcher(strZip);
        return matcher.matches();
    }

    /*
       * A common function to validate PAN number.
       * */
    public static boolean isValidPAN(String strPan) {
        String ZIP_PATTERN = "[A-Z]{5}[0-9]{4}[A-Z]{1}";
        Pattern pattern = Pattern.compile(ZIP_PATTERN);
        Matcher matcher = pattern.matcher(strPan);
        return matcher.matches();
    }

    /*public static boolean isValidIFSCNumber(String strIFSC) {
        String IFSC_PATTERN = "^[^\\s]{4}\\d{7}$";

        Pattern pattern = Pattern.compile(IFSC_PATTERN);
        Matcher matcher = pattern.matcher(strIFSC);
        return matcher.matches();
    }*/

    public static boolean isValidMobile(String strMobile) {
       /* boolean flagTemp = false;
        if (strMobile.toString().length() == 10) {
            flagTemp = true;
        } else {
            flagTemp = false;
        }
        return flagTemp;*/
        String MOBILE_PATTERN = "[789][0-9]{9}";
        Pattern pattern = Pattern.compile(MOBILE_PATTERN);
        Matcher matcher = pattern.matcher(strMobile);
        return matcher.matches();
    }

    public static String convertToBase64(String path) {
        String encodedImage4 = "";
        try {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Bitmap bitmap = decodeSampledBitmapFromResource(path, 200, 200);
//            bitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] byteArrayImage = baos.toByteArray();
            encodedImage4 = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodedImage4;
    }

    /*public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }*/

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(String res,
                                                         int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(String.valueOf(res), options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(String.valueOf(res), options);
    }

    public static boolean isValidVAT(String strVAT) {
        String VAT_PATTERN = "[V]{1}[0-9]{11}";
        Pattern pattern = Pattern.compile(VAT_PATTERN);
        Matcher matcher = pattern.matcher(strVAT);
        return matcher.matches();
    }

    public static boolean isValidCST(String strCST) {
        String CST_PATTERN = "[C]{1}[0-9]{11}";
        Pattern pattern = Pattern.compile(CST_PATTERN);
        Matcher matcher = pattern.matcher(strCST);
        return matcher.matches();
    }

    public static void loadProgressDialog(Context context, boolean boolCancelable) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(boolCancelable);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    public static void dismissProgressDialog(Context context) {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }
}