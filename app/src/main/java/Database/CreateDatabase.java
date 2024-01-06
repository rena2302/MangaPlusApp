package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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

        db.close(); // Đóng kết nối sau khi thêm dữ liệu
        return result != -1; // Kiểm tra nếu giá trị trả về khác -1

    }

    public Boolean CheckEmail (String email){
        Cursor cursor = myDb.rawQuery("Select * from " + TB_USER + " where " + TB_USER_EMAIL + " = ?", new String[]{email});
        if(cursor.getCount() > 0){
            return true; /// that bai
        }
        else{
            return false; // thanh cong
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

}
