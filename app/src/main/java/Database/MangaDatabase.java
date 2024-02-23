package Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MangaDatabase extends SQLiteOpenHelper {
    private final Context context;
    public static final String TB_MANGA = "MANGA";
    public static final String TB_MANGA_ID = "ID_MANGA";
    public static final String TB_MANGA_NAME = "NAME_MANGA";
    public static final String TB_MANGA_PICTURE = "PICTURE_MANGA";

    public MangaDatabase(Context context) {
        super(context, "MangaPlus", null, 6);
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

}
