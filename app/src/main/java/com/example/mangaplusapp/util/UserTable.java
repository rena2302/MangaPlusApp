package com.example.mangaplusapp.util;

import android.provider.BaseColumns;

public class UserTable implements BaseColumns {
    public static final String USER_SESSION_PREF = "user_session";
    // This object is used to read stored values
    public static final String KEY_USER_EMAIL = "user_email"; // Adjust the key as needed
    public static final String KEY_USER_ID = "user_id"; // Adjust the key as needed

    // CREATE ACT
    public static String TB_USER = "USER";
    public static String TB_ADMIN = "ADMIN";

    // CREATE PRO
    // USER
    public static String TB_USER_ID_USER = "ID_USER";
    public static String TB_USER_NAME = "NAME";
    public static String TB_USER_EMAIL = "EMAIL";
    public static String TB_USER_PASSWORD = "PASSWORD";
    public static String TB_USER_PICTURE = "PICTURE";
    public static String TB_USER_ADDRESS = "ADDRESS";
    public static String TB_USER_LEVEL = "LEVEL";
    public static String TB_USER_GENDER = "GENDER";

    // ADMIN
    public static String TB_ADMIN_ID_ADMIN = "ID_ADMIN";
    public static String TB_ADMIN_NAME = "NAME";
    public static String TB_ADMIN_EMAIL = "EMAIL";
    public static String TB_ADMIN_PASSWORD = "PASSWORD";
    public static String TB_ADMIN_ADDRESS = "ADDRESS";
    public static String TB_ADMIN_ROLE = "ROLE";

}
