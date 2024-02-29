package com.example.mangaplusapp.Helper.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.mangaplusapp.util.table.CategoryTable;
import com.example.mangaplusapp.Database.MangaPlusDatabase;

public class CategoryDBHelper extends MangaPlusDatabase {
    public CategoryDBHelper(Context context) {
        super(context);
    }
    public boolean insertData(String Name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CategoryTable.TB_CATEGORY_NAME, Name);
        long result = db.insert(CategoryTable.TB_CATEGORY, null, contentValues);
        db.close();
        return result != -1;
    }
}
