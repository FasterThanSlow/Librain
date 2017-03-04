package com.greenkey.librain;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.greenkey.librain.campaign.Level;

/**
 * Created by Alexander on 02.03.2017.
 */
public class LevelDatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "levels.db";

    public static class LevelEntry implements BaseColumns {
        public static final String TABLE_NAME = "level";

        public static final String LEVEL_ENABLED_COLUMN = "enabled";
        public static final String LEVEL_STARS_COUNT_COLUMN = "start_count";

        public static final String[] COLUMNS = new String[]{ _ID, LEVEL_ENABLED_COLUMN, LEVEL_STARS_COUNT_COLUMN};
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + LevelEntry.TABLE_NAME + " (" +
                    LevelEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    LevelEntry.LEVEL_ENABLED_COLUMN + " INTEGER, " +
                    LevelEntry.LEVEL_STARS_COUNT_COLUMN + " INTEGER " +
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + LevelEntry.TABLE_NAME;


    public LevelDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);

        //Открытый первый уровнь
        ContentValues firstLevelContentValues = new ContentValues();
        firstLevelContentValues.put(LevelDatabaseHelper.LevelEntry.LEVEL_STARS_COUNT_COLUMN, 0);
        firstLevelContentValues.put(LevelDatabaseHelper.LevelEntry.LEVEL_ENABLED_COLUMN, true);
        db.insert(LevelEntry.TABLE_NAME, null, firstLevelContentValues);

        ContentValues contentValues = new ContentValues();
        contentValues.put(LevelDatabaseHelper.LevelEntry.LEVEL_STARS_COUNT_COLUMN, 0);
        contentValues.put(LevelDatabaseHelper.LevelEntry.LEVEL_ENABLED_COLUMN, false);
        for (int i = 1; i < LevelDao.LEVELS_COUNT; i++) {
            db.insert(LevelEntry.TABLE_NAME, null, contentValues);
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}