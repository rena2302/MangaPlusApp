package com.example.mangaplusapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.mangaplusapp.util.CategoryTable;
import com.example.mangaplusapp.util.MangaTable;
import com.example.mangaplusapp.util.UserTable;

public class MangaPlusDatabase extends SQLiteOpenHelper {
    private final Context context;
    private static final String DATABASE_NAME = "mangaplus.db";
    private static final int DATABASE_VERSION = 1;

    public MangaPlusDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MANGA_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS " + MangaTable.TB_MANGA + " (" +
                MangaTable.TB_MANGA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MangaTable.TB_MANGA_NAME + " TEXT," +
                MangaTable.TB_MANGA_PICTURE + " TEXT)";
        String CREATE_CATEGORY_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS " + CategoryTable.TB_CATEGORY + " (" +
                CategoryTable.TB_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CategoryTable.TB_CATEGORY_NAME + " TEXT," +
                CategoryTable.TB_CATEGORY_PICTURE + " TEXT)";
        String CREATE_USER_TABLE_QUERY = " CREATE TABLE " + UserTable.TB_USER + " ( " + UserTable.TB_USER_ID_USER + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + UserTable.TB_USER_NAME + " TEXT, " + UserTable.TB_USER_EMAIL + " TEXT, " + UserTable.TB_USER_PASSWORD + " TEXT, "
                + UserTable.TB_USER_ADDRESS + " TEXT, " + UserTable.TB_USER_GENDER + " TEXT, " + UserTable.TB_USER_PICTURE + " TEXT, " + UserTable.TB_USER_LEVEL + " INTEGER ) ";

        String CREATE_ADMIN_USER_TABLE_QUERY = " CREATE TABLE " + UserTable.TB_ADMIN + " ( " + UserTable.TB_ADMIN_ID_ADMIN + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + UserTable.TB_ADMIN_NAME + " TEXT, " + UserTable.TB_ADMIN_EMAIL + " TEXT, " + UserTable.TB_ADMIN_PASSWORD + " TEXT, "
                + UserTable.TB_ADMIN_ADDRESS + " TEXT, " + UserTable.TB_ADMIN_ROLE + " INTEGER ) ";

        db.execSQL(CREATE_MANGA_TABLE_QUERY);
        db.execSQL(CREATE_CATEGORY_TABLE_QUERY);
        db.execSQL(CREATE_USER_TABLE_QUERY);
        db.execSQL(CREATE_ADMIN_USER_TABLE_QUERY);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa các bảng cũ nếu chúng tồn tại
        db.execSQL("DROP TABLE IF EXISTS " + MangaTable.TB_MANGA);
        db.execSQL("DROP TABLE IF EXISTS " + CategoryTable.TB_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + UserTable.TB_USER);
        db.execSQL("DROP TABLE IF EXISTS " + UserTable.TB_ADMIN);

        // Tạo lại các bảng
        onCreate(db);
    }
    public SQLiteDatabase open() {
        return this.getWritableDatabase();
    }

    public Context getContext() {
        return context;
    }
}
