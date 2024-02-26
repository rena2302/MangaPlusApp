package Helper.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import Database.CategoryDatabase;

public class CategoryHelper  extends CategoryDatabase {
    public CategoryHelper(Context context) {
        super(context);
    }
    public boolean insertData(String Name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TB_CATEGORY_NAME, Name);
        long result = db.insert(TB_CATEGORY, null, contentValues);
        db.close();
        return result != -1;
    }
}
