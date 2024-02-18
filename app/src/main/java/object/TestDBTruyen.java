package object;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class TestDBTruyen extends SQLiteOpenHelper {
    private final Context context;
    public static final String TB_MANGA = "MANGA";
    public static final String TB_MANGA_ID = "ID_MANGA";
    public static final String TB_MANGA_NAME = "NAME_MANGA";
    public static final String TB_MANGA_PICTURE = "PICTURE_MANGA";

    public TestDBTruyen(Context context) {
        super(context, "MangaPlus", null, 4);
        this.context = context;
    }
    public SQLiteDatabase open() {
        return this.getWritableDatabase();
    }

    public void onCreate(SQLiteDatabase db) {
        Log.d("Database", "onCreate() called");
        String tbManga = "CREATE TABLE " + TB_MANGA + " ("
                + TB_MANGA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TB_MANGA_NAME + " TEXT, "
                + TB_MANGA_PICTURE + " TEXT)";
        db.execSQL(tbManga);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_MANGA);
        onCreate(db);
    }
    public boolean insertData(String Name, String imgPath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TB_MANGA_NAME, Name);
        contentValues.put(TB_MANGA_PICTURE, imgPath); // Lưu đường dẫn tệp hình ảnh

        long result = db.insert(TB_MANGA, null, contentValues);
        db.close();
        return result != -1;
    }

    public boolean insertData(String Name, Uri imgUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TB_MANGA_NAME, Name);
        contentValues.put(TB_MANGA_PICTURE, imgUri.toString());

        long result = db.insert(TB_MANGA, null, contentValues);
        db.close();
        return result != -1;
    }
    @SuppressLint("Range")
    public List<TruyenTranh> getAllMangaItems() {
        List<TruyenTranh> mangaItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TB_MANGA, null);
        if (cursor.moveToFirst()) {
            do {
                 String mangaName = cursor.getString(cursor.getColumnIndex(TB_MANGA_NAME));
                 String imgPath = cursor.getString(cursor.getColumnIndex(TB_MANGA_PICTURE));
                mangaItems.add(new TruyenTranh(mangaName, imgPath));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return mangaItems;
    }
    public boolean deleteAllMangaData() {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TB_MANGA, null, null);
        db.close();
        return result != 0;
    }
}
