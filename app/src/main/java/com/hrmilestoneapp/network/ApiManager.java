package com.hrmilestoneapp.network;

/**
 * Created by arti on 2/9/2016.
 */
public class ApiManager {

    // Base url
    //public static String BASE_URL = "http://192.168.1.112:8181/ws/jsonControllar.php?";
    public static String BASE_URL = "https://antiphonal-dollar.000webhostapp.com/jsonControllar.php?";

    public static String IMAGE_BASE_URL = "";

    // Custom api url
    public static String USER_LOGIN = BASE_URL + "ws_signin";
    public static String USER_SIGNUP = BASE_URL + "ws_signup";
    public static String USER_CHANGE_PASSWORD = BASE_URL + "ws_changepassword";
    public static String USER_FORGOT_PASSWORD = BASE_URL + "ws_forgotpwd";
    public static String USER_UPDATE = BASE_URL + "ws_updateuser";
    public static String DISPLAY_CATEGORY = BASE_URL + "view_category";

}