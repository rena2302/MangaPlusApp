package com.example.mangaplusapp.Helper.DBHelper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import com.example.mangaplusapp.util.table.MangaTable;
import com.example.mangaplusapp.Database.MangaPlusDatabase;
import com.example.mangaplusapp.object.Mangas;

public class MangaDBHelper extends MangaPlusDatabase {
    public MangaDBHelper(Context context) {
        super(context);
    }

    public boolean insertData(String Name, String imgPath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MangaTable.TB_MANGA_NAME, Name);
        contentValues.put(MangaTable.TB_MANGA_PICTURE, String.valueOf(Uri.parse(imgPath))); // Lưu đường dẫn tệp hình ảnh
        long result = db.insert(MangaTable.TB_MANGA, null, contentValues);
        db.close();
        return result != -1;
    }

    public boolean insertData(String Name, Uri imgUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MangaTable.TB_MANGA_NAME, Name);
        contentValues.put(MangaTable.TB_MANGA_PICTURE, imgUri.toString());
        long result = db.insert(MangaTable.TB_MANGA, null, contentValues);
        db.close();
        return result != -1;
    }
    @SuppressLint("Range")
    public List<Mangas> getAllMangaItems() {
        List<Mangas> mangaItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + MangaTable.TB_MANGA, null);
        if (cursor.moveToFirst()) {
            do {
                String mangaName = cursor.getString(cursor.getColumnIndex(MangaTable.TB_MANGA_NAME));
                String imgPath = cursor.getString(cursor.getColumnIndex(MangaTable.TB_MANGA_PICTURE));
                mangaItems.add(new Mangas(mangaName, imgPath));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return mangaItems;
    }
    public boolean deleteAllMangaData() {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(MangaTable.TB_MANGA, null, null);
        db.close();
        return result != 0;
    }
}
