package com.example.rahaf.safeheart1;

public class Config {

    public static final String URL = "https://safeheart996.000webhostapp.com/safeheart/";
    public static final String LOGIN_URL=URL+"patient_login.php";//
    public static final String FAMILY_LIST = URL+"family_list.php";
    public static final String ADD_FAMILY = URL+"add_family.php";
    public static final String REMOVE_FAMILY =URL+"delete_family.php";
    public static final String UPDATE_READING =URL+"update_reading.php";

    public static final String GET_DOCTOR_PHONE =URL+"get_doctor_phone.php";

    public static final String GET_FAMILIES_PHONE =URL+"get_families_phones.php";


    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";


    //If server response is equal to this that means login is successful
    public static final String LOGIN_SUCCESS = "success";

    //Keys for Sharedpreferences
    //This would be the name of our shared preferences
    public static final String SHARED_PREF_NAME = "myloginapp";

    //This would be used to store the username of current logged in user
    public static final String USER_SHARED_PREF = "user";

    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";


}
