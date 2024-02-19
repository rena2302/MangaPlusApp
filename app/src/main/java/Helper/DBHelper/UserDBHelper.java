package Helper.DBHelper;

import android.annotation.SuppressLint;
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

}
