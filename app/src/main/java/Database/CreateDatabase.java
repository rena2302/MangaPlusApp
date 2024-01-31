package Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Patterns;

import org.mindrot.jbcrypt.BCrypt;

import java.util.regex.Pattern;

public class CreateDatabase extends SQLiteOpenHelper{
    SQLiteDatabase myDb= this.getWritableDatabase();
    private final Context context;
    private static final String USER_SESSION_PREF = "user_session";
    // This object is used to read stored values
    private static final String KEY_USER_EMAIL = "user_email"; // Adjust the key as needed
    private static final String KEY_USER_ID = "user_id"; // Adjust the key as needed

    // CREATE ACT
    public static String TB_USER = "USER";
    public static String TB_ADMIN = "ADMIN";

    // CREATE PRO
    // USER
    public static String TB_USER_ID_USER = "ID_USER";
    public static String TB_USER_NAME = "NAME";
    public static String TB_USER_EMAIL = "EMAIL";
    public static String TB_USER_PASSWORD = "PASSWORD";
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

    public CreateDatabase(Context context){
        super (context,"MangaPlus",null,1);
        this.context = context;
    }
    public boolean isUserLoggedIn() {
        SharedPreferences preferences = context.getSharedPreferences(USER_SESSION_PREF, Context.MODE_PRIVATE);
        // Check if user email or any other session data exists
        return preferences.contains(KEY_USER_EMAIL);
    }
    // Clear user session data
    public void clearUserSession() {
        SharedPreferences preferences = context.getSharedPreferences(USER_SESSION_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(KEY_USER_EMAIL);
        // Remove other session data if needed
        editor.apply();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
            String tbUser = " CREATE TABLE " + TB_USER + " ( " + TB_USER_ID_USER + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TB_USER_NAME + " TEXT, " + TB_USER_EMAIL + " TEXT, " + TB_USER_PASSWORD + " TEXT, "
                    + TB_USER_ADDRESS + " TEXT, " + TB_USER_GENDER + " TEXT, " + TB_USER_LEVEL + " INTEGER ) ";

            String tbAdmin = " CREATE TABLE " + TB_ADMIN + " ( " + TB_ADMIN_ID_ADMIN + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TB_ADMIN_NAME + " TEXT, " + TB_ADMIN_EMAIL + " TEXT, " + TB_ADMIN_PASSWORD + " TEXT, "
                    + TB_ADMIN_ADDRESS + " TEXT, " + TB_ADMIN_ROLE + " INTEGER ) ";

            db.execSQL(tbUser);
            db.execSQL(tbAdmin);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop Table if exists tbUser");
    }
    public SQLiteDatabase open() {
        return this.getWritableDatabase();

    }
    public String hashPassword(String pass){
        return BCrypt.hashpw(pass,BCrypt.gensalt());
    }
    public boolean CheckHashPassword(String password,String hashPassword){
        return BCrypt.checkpw(password,hashPassword);
    }
    public Boolean insertData (String email, String password,String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String hash = hashPassword(password);
        contentValues.put(TB_USER_EMAIL, email);
        contentValues.put(TB_USER_PASSWORD, hash);
        contentValues.put(TB_USER_NAME, name);
        long result = db.insert(TB_USER, null, contentValues);
        db.close();
        return result != -1; // Kiểm tra nếu giá trị trả về khác -1

    }
    //=====================================USER MANAGER===========================================//
    public boolean resetPassword(String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TB_USER_PASSWORD, newPassword);
        // use function update to reset new password
        int affectedRows = db.update(TB_USER, contentValues,null,null);
        // if haven't data updated, process wil return 0
        db.close();
        // check have data updated, did it check sure data had update
        return affectedRows > 0;
    }
    @SuppressLint({"Range", "Recycle"})
    public int loginUser(String userEmail, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        int userId = -1; // value basic if login failed
        // check login from user with user email and user password
        String hash;
        Cursor cursorHash,cursor = null;
        try{
            // get hasPassword from cursorHash
            cursorHash = db.rawQuery(" SELECT "+ TB_USER_PASSWORD + " FROM " + TB_USER + " WHERE " + TB_USER_EMAIL + " = ?",new String[]{userEmail});
            if(cursorHash!=null&&cursorHash.moveToFirst()){
                hash = cursorHash.getString(cursorHash.getColumnIndex(TB_USER_PASSWORD));
                boolean checkHash = CheckHashPassword(password,hash);
                if (!checkHash) return -1;
            }
            else{
                Log.d("loginUser", "Email or password were error ");
                return -1;
            }

            cursor = db.rawQuery("SELECT " + TB_USER_ID_USER + " FROM " + TB_USER +
                            " WHERE " + TB_USER_EMAIL + " = ? AND " + TB_USER_PASSWORD + " = ?",
                    new String[]{userEmail, hash});
            // check exists data
            if (cursor!=null&&cursor.moveToFirst()) {
                userId = cursor.getInt(cursor.getColumnIndex(TB_USER_ID_USER));
            }
            else{
                Log.d("loginUser", "Can not get id by Email :  " + userEmail);
                return -1;
            }
        }finally {
            if(cursor!=null){
                cursor.close();
            }
            db.close();
        }
        return userId;

    }
    @SuppressLint("Range")
    public String getUserEmail(int userId) {
        String email = "";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + TB_USER_EMAIL + " FROM " + TB_USER +
                " WHERE " + TB_USER_ID_USER + " = ?", new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            email = cursor.getString(cursor.getColumnIndex(TB_USER_EMAIL));
        }
        cursor.close();
        db.close();
        return email;
    }
    @SuppressLint("Range")
    public String getUserPassword(int userId) {
        String password = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + TB_USER_PASSWORD + " FROM " + TB_USER + " Where " + TB_USER_ID_USER + " = ?",new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            password = cursor.getString(cursor.getColumnIndex(TB_USER_PASSWORD));
        }
        cursor.close();
        db.close();
        return password;
    }
    @SuppressLint("Range")
    public String getUserName(int userId) {
        String name = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + TB_USER_NAME + " FROM " + TB_USER + " Where " + TB_USER_ID_USER + " = ?",new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndex(TB_USER_NAME));
        }
        cursor.close();
        db.close();
        return name;
    }
    //==========================================VALIDATION========================================//
    @SuppressLint("Range")
    public boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
    public boolean validPassword(String password){
        password = password.trim();
        return password.length() >= 8;
    }
    @SuppressLint("Recycle")
    public Boolean CheckEmailExists(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT 1 FROM " + TB_USER + " WHERE " + TB_USER_EMAIL + " = ?", new String[]{email});
            return cursor.getCount() > 0;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }
    @SuppressLint({"Range","Recycle"})
    public Boolean CheckPassword(String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT " + TB_USER_PASSWORD + " FROM " + TB_USER + " WHERE " + TB_USER_PASSWORD + " = ?", new String[]{password});
            if (cursor.moveToFirst()) {
                String hashedPasswordInDB = cursor.getString(cursor.getColumnIndex(TB_USER_PASSWORD));
                return CheckHashPassword(password, hashedPasswordInDB);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return false;
    }

    @SuppressLint("Range")
    public Boolean CheckEmailPassword(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try{
            // get hash
            cursor = db.rawQuery(" SELECT "+ TB_USER_PASSWORD + " FROM " + TB_USER + " WHERE " + TB_USER_EMAIL + " = ?",new String[]{email});
            if(cursor!=null&&cursor.moveToFirst()){
                String storedHash = cursor.getString(cursor.getColumnIndex(TB_USER_PASSWORD));
                boolean isValid = CheckHashPassword(password,storedHash);
                if (isValid) {
                    Log.d("CheckEmailPassword", "Login successful for email: " + email);
                } else {
                    Log.d("CheckEmailPassword", "Login failed for email: " + email);
                }
                return isValid;
            }
            else{
                Log.d("CheckEmailPassword", "No matching email in the database or cursor is null");
                return false;
            }
        }finally {
            if(cursor!=null){
                cursor.close();
            }
            db.close();
        }
    }

}
