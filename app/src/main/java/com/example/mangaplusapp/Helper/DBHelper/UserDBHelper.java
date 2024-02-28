package com.example.mangaplusapp.Helper.DBHelper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Patterns;

import com.example.mangaplusapp.Database.MangaPlusDatabase;
import com.example.mangaplusapp.util.UserTable;

import org.mindrot.jbcrypt.BCrypt;

import java.util.regex.Pattern;

public class UserDBHelper  extends MangaPlusDatabase {
    public UserDBHelper(Context context) {
        super(context);
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
            cursor = db.rawQuery("SELECT 1 FROM " + UserTable.TB_USER + " WHERE " + UserTable.TB_USER_EMAIL + " = ?", new String[]{email});
            return cursor.getCount() > 0;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }
    public String hashPassword(String pass){
        return BCrypt.hashpw(pass,BCrypt.gensalt());
    }
    public boolean CheckHashPassword(String password,String hashPassword){
        return BCrypt.checkpw(password,hashPassword);
    }
    public boolean isUserLoggedIn() {
        SharedPreferences preferences = UserDBHelper.this.getContext().getSharedPreferences(UserTable.USER_SESSION_PREF, Context.MODE_PRIVATE);
        // Check if user email or any other session data exists
        return preferences.contains(UserTable.KEY_USER_EMAIL);
    }
    // Clear user session data
    public void clearUserSession() {
        SharedPreferences preferences = UserDBHelper.this.getContext().getSharedPreferences(UserTable.USER_SESSION_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(UserTable.KEY_USER_EMAIL);
        // Remove other session data if needed
        editor.apply();
    }
    public Boolean insertData (String email, String password,String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String hash = hashPassword(password);
        contentValues.put(UserTable.TB_USER_EMAIL, email);
        contentValues.put(UserTable.TB_USER_PASSWORD, hash);
        contentValues.put(UserTable.TB_USER_NAME, name);
        long result = db.insert(UserTable.TB_USER, null, contentValues);
        db.close();
        return result != -1; // Kiểm tra nếu giá trị trả về khác -1

    }
    //=====================================USER MANAGER===========================================//
    public boolean resetPassword(String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserTable.TB_USER_PASSWORD, newPassword);
        // use function update to reset new password
        int affectedRows = db.update(UserTable.TB_USER, contentValues,null,null);
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
            cursorHash = db.rawQuery(" SELECT "+ UserTable.TB_USER_PASSWORD + " FROM " + UserTable.TB_USER + " WHERE " + UserTable.TB_USER_EMAIL + " = ?",new String[]{userEmail});
            if(cursorHash!=null&&cursorHash.moveToFirst()){
                hash = cursorHash.getString(cursorHash.getColumnIndex(UserTable.TB_USER_PASSWORD));
                boolean checkHash = CheckHashPassword(password,hash);
                if (!checkHash) return -1;
            }
            else{
                Log.d("loginUser", "Email or password were error ");
                return -1;
            }

            cursor = db.rawQuery("SELECT " + UserTable.TB_USER_ID_USER + " FROM " + UserTable.TB_USER +
                            " WHERE " + UserTable.TB_USER_EMAIL + " = ? AND " + UserTable.TB_USER_PASSWORD + " = ?",
                    new String[]{userEmail, hash});
            // check exists data
            if (cursor!=null&&cursor.moveToFirst()) {
                userId = cursor.getInt(cursor.getColumnIndex(UserTable.TB_USER_ID_USER));
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
    @SuppressLint({"Range", "Recycle"})
    public int loginUser(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        int userId = -1; // value basic if login failed

        Cursor cursor = null;
        try{
            cursor = db.rawQuery("SELECT " + UserTable.TB_USER_ID_USER + " FROM " + UserTable.TB_USER +
                    " WHERE " + UserTable.TB_USER_EMAIL + " = ?" , new String[]{userEmail});
            // check exists data
            if (cursor!=null&&cursor.moveToFirst()) {
                userId = cursor.getInt(cursor.getColumnIndex(UserTable.TB_USER_ID_USER));
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
    @SuppressLint({"Range","Recycle"})
    public Boolean CheckPassword(String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT " + UserTable.TB_USER_PASSWORD + " FROM " + UserTable.TB_USER + " WHERE " + UserTable.TB_USER_PASSWORD + " = ?", new String[]{password});
            if (cursor.moveToFirst()) {
                String hashedPasswordInDB = cursor.getString(cursor.getColumnIndex(UserTable.TB_USER_PASSWORD));
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
            cursor = db.rawQuery(" SELECT "+ UserTable.TB_USER_PASSWORD + " FROM " + UserTable.TB_USER + " WHERE " + UserTable.TB_USER_EMAIL + " = ?",new String[]{email});
            if(cursor!=null&&cursor.moveToFirst()){
                String storedHash = cursor.getString(cursor.getColumnIndex(UserTable.TB_USER_PASSWORD));
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
    @SuppressLint("Range")
    public String getUserEmail(int userId) {
        String email = "";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + UserTable.TB_USER_EMAIL + " FROM " + UserTable.TB_USER +
                " WHERE " + UserTable.TB_USER_ID_USER + " = ?", new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            email = cursor.getString(cursor.getColumnIndex(UserTable.TB_USER_EMAIL));
        }
        cursor.close();
        db.close();
        return email;
    }
    @SuppressLint("Range")
    public String getUserPassword(int userId) {
        String password = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + UserTable.TB_USER_PASSWORD + " FROM " + UserTable.TB_USER + " Where " + UserTable.TB_USER_ID_USER + " = ?",new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            password = cursor.getString(cursor.getColumnIndex(UserTable.TB_USER_PASSWORD));
        }
        cursor.close();
        db.close();
        return password;
    }
    @SuppressLint("Range")
    public String getUserName(int userId) {
        String name = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + UserTable.TB_USER_NAME + " FROM " + UserTable.TB_USER + " Where " + UserTable.TB_USER_ID_USER + " = ?",new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndex(UserTable.TB_USER_NAME));
        }
        cursor.close();
        db.close();
        return name;
    }
    public void UpdatePassword(int userId, String newPass){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String hash = hashPassword(newPass);
        values.put(UserTable.TB_USER_PASSWORD, hash);
        db.update(UserTable.TB_USER, values, UserTable.TB_USER_ID_USER + " = ?", new String[]{String.valueOf(userId)});
        db.close();
    }
    public boolean clearALlUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(UserTable.TB_USER, null, null);
        db.close();
        return result != 0;
    }
}
