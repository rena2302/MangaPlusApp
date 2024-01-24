package Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.mangaplusapp.DrawerFragment;

import java.util.regex.Pattern;

public class CreateDatabase extends SQLiteOpenHelper{
    SQLiteDatabase myDb= this.getWritableDatabase();

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
    public Boolean insertData (String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TB_USER_EMAIL, email);
        contentValues.put(TB_USER_PASSWORD, password);

        long result = db.insert(TB_USER, null, contentValues);

        db.close();
        return result != -1; // Kiểm tra nếu giá trị trả về khác -1

    }
    public Boolean CheckEmail (String email){
        Cursor cursor = myDb.rawQuery("Select * from " + TB_USER + " where " + TB_USER_EMAIL + " = ?", new String[]{email});
        if(cursor.getCount() > 0) {
            return true; // Email đã tồn tại, trả về true
        }
        else{
            return false;  // Email không tồn tại, trả về false
        }
    }
    public Boolean CheckPassword (String password){
        Cursor cursor = myDb.rawQuery("Select * from " + TB_USER + " where " + TB_USER_PASSWORD + " = ?", new String[]{password});
        if(cursor.getCount() > 0) {
            return true; // Password đã tồn tại, trả về true
        }
        else{
            return false;  // Password không tồn tại, trả về false
        }
    }
    public Boolean CheckEmailPassword (String email,String password){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TB_USER + " WHERE " + TB_USER_EMAIL + " = ? AND " + TB_USER_PASSWORD + " = ?", new String[]{email, password});
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isValid;
    }
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
    @SuppressLint("Range")
    public String getUserEmail() {
        String email = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + TB_USER_EMAIL + " FROM " + TB_USER, null);

        if (cursor.moveToFirst()) {
            email = cursor.getString(cursor.getColumnIndex(TB_USER_EMAIL));
        }
        cursor.close();
        db.close();
        return email;
    }
    @SuppressLint("Range")
    public String getUserName() {
        String name = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + TB_USER_NAME + " FROM " + TB_USER, null);

        if (cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndex(TB_USER_NAME));
        }
        cursor.close();
        db.close();
        return name;
    }
}
