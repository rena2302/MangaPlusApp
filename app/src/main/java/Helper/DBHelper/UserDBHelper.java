package Helper.DBHelper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Patterns;

import java.util.regex.Pattern;

import Database.UserDatabase;

public class UserDBHelper  extends UserDatabase {
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
    public void UpdatePassword(int userId, String newPass){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String hash = hashPassword(newPass);
        values.put(TB_USER_PASSWORD, hash);
        db.update(TB_USER, values, TB_USER_ID_USER + " = ?", new String[]{String.valueOf(userId)});
        db.close();
    }
    public boolean clearALlUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TB_USER, null, null);
        db.close();
        return result != 0;
    }


}
