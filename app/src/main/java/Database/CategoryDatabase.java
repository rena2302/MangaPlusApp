package Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CategoryDatabase extends SQLiteOpenHelper {
    private final Context context;
    public static final String TB_CATEGORY = "CATEGORY";
    public static final String TB_CATEGORY_ID = "ID_CATEGORY";
    public static final String TB_CATEGORY_NAME = "NAME_CATEGORY";
    public static final String TB_CATEGORY_PICTURE = "PICTURE_CATEGORY"; // Sửa tên cột

    public CategoryDatabase(Context context) {
        super(context, "MangaPlus", null, 6);
        this.context = context;
    }

    public SQLiteDatabase open() {
        return this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tbCategory = "CREATE TABLE " + TB_CATEGORY + " ("
                + TB_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TB_CATEGORY_NAME + " TEXT, "
                + TB_CATEGORY_PICTURE + " TEXT)";
        db.execSQL(tbCategory);
        Log.d("CategoryDatabase", "Table CATEGORY created successfully.");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_CATEGORY);
        onCreate(db);
    }
}
