package com.greenkey.librain.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.greenkey.librain.level.Level;
import com.greenkey.librain.level.LevelEnvironment;

import java.util.ArrayList;
import java.util.List;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE;

/**
 * Created by Alexander on 02.03.2017.
 */

public class LevelDao {

    private static LevelDao levelDao;
    public static LevelDao getInstance(Context context) {
        if (levelDao == null) {
            levelDao = new LevelDao(context);
        }

        return levelDao;
    }

    private final SQLiteDatabase database;

    private final List<Level> levels;

    private LevelDao(Context context) {
        this.database = new LevelDatabaseHelper(context).getWritableDatabase();

        this.levels = new ArrayList<>();

        this.levels.add(new Level(1, 1000, 3, 3, new LevelEnvironment(LevelEnvironment.LevelEnvironmentType.SPACE, new int[] {1, 3} )));
        this.levels.add(new Level(2, 1000, 3, 3, new LevelEnvironment(LevelEnvironment.LevelEnvironmentType.FRUIT, new int[] {1, 2} )));
        this.levels.add(new Level(3, 1000, 3, 3, new LevelEnvironment(LevelEnvironment.LevelEnvironmentType.FRUIT, new int[] {2, 2} )));
        this.levels.add(new Level(4, 1000, 3, 3, new LevelEnvironment(LevelEnvironment.LevelEnvironmentType.SPACE, new int[] {1, 3} )));
        this.levels.add(new Level(5, 1000, 3, 3, new LevelEnvironment(LevelEnvironment.LevelEnvironmentType.SPACE, new int[] {1, 3} )));
        this.levels.add(new Level(6, 1000, 3, 3, new LevelEnvironment(LevelEnvironment.LevelEnvironmentType.SPACE, new int[] {1, 3} )));
        this.levels.add(new Level(7, 1000, 3, 3, new LevelEnvironment(LevelEnvironment.LevelEnvironmentType.SPACE, new int[] {1, 3} )));
        this.levels.add(new Level(8, 1000, 3, 3, new LevelEnvironment(LevelEnvironment.LevelEnvironmentType.SPACE, new int[] {1, 3} )));
        this.levels.add(new Level(9, 1000, 3, 3, new LevelEnvironment(LevelEnvironment.LevelEnvironmentType.SPACE, new int[] {1, 3} )));
        this.levels.add(new Level(10, 1000, 3, 3, new LevelEnvironment(LevelEnvironment.LevelEnvironmentType.SPACE, new int[] {1, 3} )));
        this.levels.add(new Level(11, 1000, 3, 3, new LevelEnvironment(LevelEnvironment.LevelEnvironmentType.SPACE, new int[] {1, 3} )));
        this.levels.add(new Level(12, 1000, 3, 3, new LevelEnvironment(LevelEnvironment.LevelEnvironmentType.SPACE, new int[] {1, 3} )));
        this.levels.add(new Level(13, 1000, 3, 3, new LevelEnvironment(LevelEnvironment.LevelEnvironmentType.SPACE, new int[] {1, 3} )));
        this.levels.add(new Level(14, 1000, 3, 3, new LevelEnvironment(LevelEnvironment.LevelEnvironmentType.SPACE, new int[] {1, 3} )));
        this.levels.add(new Level(15, 1000, 3, 3, new LevelEnvironment(LevelEnvironment.LevelEnvironmentType.SPACE, new int[] {1, 3} )));
        this.levels.add(new Level(16, 1000, 3, 3, new LevelEnvironment(LevelEnvironment.LevelEnvironmentType.SPACE, new int[] {1, 3} )));
        this.levels.add(new Level(17, 1000, 3, 3, new LevelEnvironment(LevelEnvironment.LevelEnvironmentType.SPACE, new int[] {1, 3} )));
        this.levels.add(new Level(18, 1000, 3, 3, new LevelEnvironment(LevelEnvironment.LevelEnvironmentType.SPACE, new int[] {1, 3} )));
        this.levels.add(new Level(19, 1000, 3, 3, new LevelEnvironment(LevelEnvironment.LevelEnvironmentType.SPACE, new int[] {1, 3} )));
        this.levels.add(new Level(20, 1000, 3, 3, new LevelEnvironment(LevelEnvironment.LevelEnvironmentType.SPACE, new int[] {1, 3} )));

        this.levels.add(new Level(21, 1000, 3, 3, new LevelEnvironment(LevelEnvironment.LevelEnvironmentType.SPACE, new int[] {1, 3} )));
        this.levels.add(new Level(22, 1000, 3, 3, new LevelEnvironment(LevelEnvironment.LevelEnvironmentType.SPACE, new int[] {1, 3} )));
        this.levels.add(new Level(23, 1000, 3, 3, new LevelEnvironment(LevelEnvironment.LevelEnvironmentType.SPACE, new int[] {1, 3} )));

        /*
        this.levels.add(new Level(2, 1000, 4, 4, new Rule[] {new Rule(2, ResourceType.EARTH)}));
        this.levels.add(new Level(3, 1000, 5, 5, new Rule[] {new Rule(2, ResourceType.EARTH)}));
        this.levels.add(new Level(4, 1000, 3, 3, new Rule[] {new Rule(3, ResourceType.EARTH)}));
        this.levels.add(new Level(5, 1000, 4, 4, new Rule[] {new Rule(3, ResourceType.EARTH)}));
        this.levels.add(new Level(6, 1000, 5, 5, new Rule[] {new Rule(3, ResourceType.EARTH)}));
        this.levels.add(new Level(7, 1000, 3, 3, new Rule[] {new Rule(1, ResourceType.EARTH),new Rule(1, ResourceType.MARS)}));
        this.levels.add(new Level(8, 1000, 4, 4, new Rule[] {new Rule(1, ResourceType.EARTH),new Rule(1, ResourceType.MARS)}));
        this.levels.add(new Level(9, 1000, 5, 5, new Rule[] {new Rule(1, ResourceType.EARTH),new Rule(1, ResourceType.MARS)}));
        this.levels.add(new Level(10, 1000, 3, 3, new Rule[] {new Rule(2, ResourceType.EARTH),new Rule(1, ResourceType.MARS)}));
        this.levels.add(new Level(11, 1000, 4, 4, new Rule[] {new Rule(2, ResourceType.PUMPKIN),new Rule(1, ResourceType.FOOD)}));
        this.levels.add(new Level(12, 1000, 5, 5, new Rule[] {new Rule(2, ResourceType.PUMPKIN),new Rule(1, ResourceType.FOOD)}));
        this.levels.add(new Level(13, 1000, 3, 3, new Rule[] {new Rule(4, ResourceType.PEAS)}));
        this.levels.add(new Level(14, 1000, 4, 4, new Rule[] {new Rule(4, ResourceType.PEAS)}));
        this.levels.add(new Level(15, 1000, 5, 5, new Rule[] {new Rule(4, ResourceType.PEAS)}));
        this.levels.add(new Level(16, 1000, 3, 3, new Rule[] {new Rule(2, ResourceType.PUMPKIN),new Rule(2, ResourceType.PEAS)}));
        this.levels.add(new Level(17, 1000, 4, 4, new Rule[] {new Rule(2, ResourceType.PUMPKIN),new Rule(2, ResourceType.PEAS)}));
        this.levels.add(new Level(18, 1000, 5, 5, new Rule[] {new Rule(2, ResourceType.FOOD),new Rule(2, ResourceType.PUMPKIN)}));
        this.levels.add(new Level(19, 1000, 3, 3, new Rule[] {new Rule(1, ResourceType.PEAS),new Rule(1, ResourceType.FOOD),new Rule(1, ResourceType.PUMPKIN)}));
        this.levels.add(new Level(20, 1000, 4, 4, new Rule[] {new Rule(2, ResourceType.PUMPKIN),new Rule(1, ResourceType.PEAS)}));

        this.levels.add(new Level(21, 1000, 3, 3, new Rule[] {new Rule(2, ResourceType.EARTH)}));
        this.levels.add(new Level(22, 1000, 4, 4, new Rule[] {new Rule(2, ResourceType.EARTH)}));
        this.levels.add(new Level(23, 1000, 5, 5, new Rule[] {new Rule(2, ResourceType.EARTH)}));
        this.levels.add(new Level(24, 1000, 3, 3, new Rule[] {new Rule(3, ResourceType.EARTH)}));
        this.levels.add(new Level(25, 1000, 4, 4, new Rule[] {new Rule(3, ResourceType.EARTH)}));
        this.levels.add(new Level(26, 1000, 5, 5, new Rule[] {new Rule(3, ResourceType.EARTH)}));
        this.levels.add(new Level(27, 1000, 3, 3, new Rule[] {new Rule(1, ResourceType.EARTH),new Rule(1, ResourceType.MARS)}));
        this.levels.add(new Level(28, 1000, 4, 4, new Rule[] {new Rule(1, ResourceType.EARTH),new Rule(1, ResourceType.MARS)}));
        this.levels.add(new Level(29, 1000, 5, 5, new Rule[] {new Rule(1, ResourceType.EARTH),new Rule(1, ResourceType.MARS)}));
        this.levels.add(new Level(30, 1000, 3, 3, new Rule[] {new Rule(2, ResourceType.EARTH),new Rule(1, ResourceType.MARS)}));
        */

        initLevels(levels);
    }

    private void initLevels(List<Level> levels) {
        Cursor cursor = database.query(LevelDatabaseHelper.LevelEntry.TABLE_NAME, LevelDatabaseHelper.LevelEntry.COLUMNS, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(LevelDatabaseHelper.LevelEntry._ID));
                int starsCount = cursor.getInt(cursor.getColumnIndex(LevelDatabaseHelper.LevelEntry.LEVEL_STARS_COUNT_COLUMN));
                int enabled = cursor.getInt(cursor.getColumnIndex(LevelDatabaseHelper.LevelEntry.LEVEL_ENABLED_COLUMN));

                Level level = levels.get(id - 1);
                if (level != null) {
                    level.setRecord(starsCount);
                    level.setEnabled(enabled == 1); // true
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public Level getLevel(int levelId) {
        if (levelId > levels.size())
            return null;

        return levels.get(levelId - 1);
    }

    public Level updateRecord(int levelId, int record) {
        Level updatingLevel = levels.get(levelId - 1);
        updatingLevel.setRecord(record);

        Cursor cursor = database.query(LevelDatabaseHelper.LevelEntry.TABLE_NAME, LevelDatabaseHelper.LevelEntry.COLUMNS, LevelDatabaseHelper.LevelEntry._ID + " = ?", new String[] {String.valueOf(levelId)}, null, null, null);
        if (cursor.moveToFirst()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(LevelDatabaseHelper.LevelEntry.LEVEL_STARS_COUNT_COLUMN, record);

            int co = database.update(LevelDatabaseHelper.LevelEntry.TABLE_NAME, contentValues, LevelDatabaseHelper.LevelEntry._ID + " = ?", new String[] { String.valueOf(levelId) });
            Log.d("DATABASE", "updated records count =  " +  String.valueOf(co));
        }
        cursor.close();

        return updatingLevel;
    }

    public Level unlockLevel(int levelId) {
        Level updatingLevel = levels.get(levelId - 1);
        updatingLevel.setEnabled(true);

        ContentValues contentValues = new ContentValues();
        contentValues.put(LevelDatabaseHelper.LevelEntry._ID, levelId);
        //contentValues.put(LevelDatabaseHelper.LevelEntry.LEVEL_STARS_COUNT_COLUMN, 0);
        contentValues.put(LevelDatabaseHelper.LevelEntry.LEVEL_ENABLED_COLUMN, true);

        long t = database.insert(LevelDatabaseHelper.LevelEntry.TABLE_NAME, null, contentValues);
        Log.d("DATABASE", "Added new level, id =  " +  String.valueOf(t));

        return updatingLevel;
    }




    //GOVNOKOD
    private static final int LEVELS_PER_PAGE = 20;

    public int getLevelsPagesCount() {
        return levels.size() / LEVELS_PER_PAGE + 1;
    }

    public Level[] getLevelsPage(int pageIndex) {
        int startIndex = pageIndex * LEVELS_PER_PAGE;
        int endIndex = (pageIndex + 1) * LEVELS_PER_PAGE;

        if (endIndex > levels.size()) {
            endIndex = levels.size();
        }

        return levels.subList(startIndex, endIndex).toArray(new Level[endIndex - startIndex]);
    }



/*
    private Level updateLevel(int levelId, int record, boolean enabled) {
        Cursor cursor = database.query(LevelDatabaseHelper.LevelEntry.TABLE_NAME, LevelDatabaseHelper.LevelEntry.COLUMNS, LevelDatabaseHelper.LevelEntry._ID + " = ?", new String[] {String.valueOf(levelId)}, null, null, null);
        if (cursor.moveToFirst()) {
            //Уже есть запись
            ContentValues contentValues = new ContentValues();

            contentValues.put(LevelDatabaseHelper.LevelEntry.LEVEL_STARS_COUNT_COLUMN, record);
            contentValues.put(LevelDatabaseHelper.LevelEntry.LEVEL_ENABLED_COLUMN, enabled);

            int co = database.update(LevelDatabaseHelper.LevelEntry.TABLE_NAME, contentValues, LevelDatabaseHelper.LevelEntry._ID + " = ?", new String[] { String.valueOf(levelId) });
            Log.d("DATABASE", "updated records count =  " +  String.valueOf(co));
        } else {
            //Ещё не существует
            ContentValues firstLevelContentValues = new ContentValues();
            firstLevelContentValues.put(LevelDatabaseHelper.LevelEntry._ID, levelId);
            firstLevelContentValues.put(LevelDatabaseHelper.LevelEntry.LEVEL_STARS_COUNT_COLUMN, 0);
            firstLevelContentValues.put(LevelDatabaseHelper.LevelEntry.LEVEL_ENABLED_COLUMN, true);

            long t = database.insert(LevelDatabaseHelper.LevelEntry.TABLE_NAME, null, firstLevelContentValues);
            Log.d("DATABASE", "Added new level id =  " +  String.valueOf(t));
        }

        cursor.close();

        Level updatingLevel = levels.get(levelId);
        updatingLevel.setRecord(record);
        updatingLevel.setEnabled(enabled);

        return updatingLevel;
    }
*/

    /*
    public Level findLevel(int levelId) {
        if (levelId > levels.size())
            return null;

        Level level = levels.get(levelId - 1);

        Cursor cursor = database.rawQuery("SELECT * FROM " + LevelDatabaseHelper.LevelEntry.TABLE_NAME + " WHERE " + LevelDatabaseHelper.LevelEntry._ID + "='" + levelId + "'" , null);
        if (cursor.moveToFirst()) {
            int starsCount = cursor.getInt(cursor.getColumnIndex(LevelDatabaseHelper.LevelEntry.LEVEL_STARS_COUNT_COLUMN));
            int enabled = cursor.getInt(cursor.getColumnIndex(LevelDatabaseHelper.LevelEntry.LEVEL_ENABLED_COLUMN));

            level.setRecord(starsCount);
            level.setEnabled(enabled == 1);
        }
        cursor.close();

        return level;
    }*/

}
