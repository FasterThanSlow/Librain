package com.greenkey.librain.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.greenkey.librain.level.Level;
import com.greenkey.librain.level.Round;
import com.greenkey.librain.level.LevelsPage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 02.03.2017.
 */

public class LevelDao {

    private static final int STARS_PER_LEVEL = 3;

    private static LevelDao levelDao;
    public static LevelDao getInstance(Context context) {
        if (levelDao == null) {
            levelDao = new LevelDao(context);
        }

        return levelDao;
    }

    private final SQLiteDatabase database;

    private final List<Level> levels;
    private List<LevelsPage> levelsPages;

    private LevelDao(Context context) {
        this.database = new LevelDatabaseHelper(context).getWritableDatabase();

        this.levels = new ArrayList<>();
        this.levelsPages = new ArrayList<>();

        ArrayList<Level> spaceLevels = new ArrayList<>();

        spaceLevels.add(new Level(1, Level.LevelType.SPACE, new Round(1000, 3, 3, new int[]{1}), new Round(1000, 3, 3, new int[]{2}), new Round(1000, 3, 3, new int[]{1})));
        spaceLevels.add(new Level(2, Level.LevelType.SPACE, new Round(1000, 3, 3, new int[]{2}), new Round(1000, 3, 4, new int[]{2}), new Round(1000, 3, 3, new int[]{2})));
        spaceLevels.add(new Level(3, Level.LevelType.SPACE, new Round(1000, 3, 3, new int[]{3}), new Round(1000, 3, 3, new int[]{3}), new Round(1000, 3, 3, new int[]{3})));
        spaceLevels.add(new Level(4, Level.LevelType.SPACE, new Round(1000, 3, 3, new int[]{4}), new Round(1000, 3, 3, new int[]{4}), new Round(1000, 3, 3, new int[]{4})));
        spaceLevels.add(new Level(5, Level.LevelType.SPACE, new Round(1000, 3, 3, new int[]{5}), new Round(1000, 3, 3, new int[]{5}), new Round(1000, 3, 3, new int[]{5})));
        spaceLevels.add(new Level(6, Level.LevelType.SPACE, new Round(1000, 3, 3, new int[]{6}), new Round(1000, 3, 3, new int[]{6}), new Round(1000, 3, 4, new int[]{5})));
        spaceLevels.add(new Level(7, Level.LevelType.SPACE, new Round(1000, 4, 4, new int[]{6}), new Round(1000, 4, 4, new int[]{6}), new Round(1000, 4, 4, new int[]{5})));
        spaceLevels.add(new Level(8, Level.LevelType.SPACE, new Round(1000, 4, 5, new int[]{6}), new Round(1000, 4, 5, new int[]{6}), new Round(1000, 4, 5, new int[]{5})));
        spaceLevels.add(new Level(9, Level.LevelType.SPACE, new Round(1000, 5, 5, new int[]{6}), new Round(1000, 5, 5, new int[]{6}), new Round(1000, 5, 5, new int[]{5})));
        spaceLevels.add(new Level(10, Level.LevelType.SPACE, new Round(1000, 4, 3, new int[]{7}), new Round(1000, 4, 3, new int[]{7}), new Round(1000, 3, 3, new int[]{6})));

        spaceLevels.add(new Level(11, Level.LevelType.SPACE, new Round(1000, 4, 4, new int[]{7}), new Round(1000, 4, 4, new int[]{7}), new Round(1000, 3, 4, new int[]{6})));
        spaceLevels.add(new Level(12, Level.LevelType.SPACE, new Round(1000, 4, 5, new int[]{7}), new Round(1000, 4, 5, new int[]{7}), new Round(1000, 4, 4, new int[]{6})));
        spaceLevels.add(new Level(13, Level.LevelType.SPACE, new Round(1000, 5, 5, new int[]{7}), new Round(1000, 5, 5, new int[]{7}), new Round(1000, 4, 5, new int[]{6})));
        spaceLevels.add(new Level(14, Level.LevelType.SPACE, new Round(1000, 4, 4, new int[]{8}), new Round(1000, 4, 4, new int[]{8}), new Round(1000, 5, 5, new int[]{6})));
        spaceLevels.add(new Level(15, Level.LevelType.SPACE, new Round(1000, 5, 4, new int[]{8}), new Round(1000, 5, 4, new int[]{8}), new Round(1000, 3, 4, new int[]{7})));
        spaceLevels.add(new Level(16, Level.LevelType.SPACE, new Round(1000, 3, 3, new int[]{1, 1}), new Round(1000, 3, 3, new int[]{1, 1}), new Round(1000, 3, 3, new int[]{1, 1})));
        spaceLevels.add(new Level(17, Level.LevelType.SPACE, new Round(1000, 3, 3, new int[]{2, 1}), new Round(1000, 3, 3, new int[]{2, 1}), new Round(1000, 3, 3, new int[]{2, 1})));
        spaceLevels.add(new Level(18, Level.LevelType.SPACE, new Round(1000, 3, 3, new int[]{2, 2}), new Round(1000, 3, 3, new int[]{2, 2}), new Round(1000, 3, 3, new int[]{2, 2})));
        spaceLevels.add(new Level(19, Level.LevelType.SPACE, new Round(1000, 3, 3, new int[]{3, 2}), new Round(1000, 3, 3, new int[]{3, 2}), new Round(1000, 3, 3, new int[]{3, 2})));
        spaceLevels.add(new Level(20, Level.LevelType.SPACE, new Round(1000, 3, 3, new int[]{3, 3}), new Round(1000, 3, 3, new int[]{3, 3}), new Round(1000, 3, 4, new int[]{3, 2})));

        this.levelsPages.add(new LevelsPage(Level.LevelType.SPACE, spaceLevels));
        this.levels.addAll(spaceLevels);

        ArrayList<Level> fruitLevels = new ArrayList<>();

        fruitLevels.add(new Level(21, Level.LevelType.FRUIT, new Round(1000, 3, 4, new int[]{3, 3}), new Round(1000, 3, 4, new int[]{3, 3}), new Round(1000, 4, 4, new int[]{3, 2})));
        fruitLevels.add(new Level(22, Level.LevelType.FRUIT, new Round(1000, 4, 4, new int[]{3, 3}), new Round(1000, 4, 4, new int[]{3, 3}), new Round(1000, 5, 4, new int[]{3, 2})));
        fruitLevels.add(new Level(23, Level.LevelType.FRUIT, new Round(1000, 5, 4, new int[]{3, 3}), new Round(1000, 5, 4, new int[]{3, 3}), new Round(1000, 5, 5, new int[]{3, 2})));
        fruitLevels.add(new Level(24, Level.LevelType.FRUIT, new Round(1000, 5, 5, new int[]{3, 3}), new Round(1000, 5, 5, new int[]{3, 3}), new Round(1000, 3, 3, new int[]{3, 3})));
        fruitLevels.add(new Level(25, Level.LevelType.FRUIT, new Round(1000, 3, 4, new int[]{4, 3}), new Round(1000, 3, 4, new int[]{4, 3}), new Round(1000, 3, 4, new int[]{3, 3})));
        fruitLevels.add(new Level(26, Level.LevelType.FRUIT, new Round(1000, 4, 4, new int[]{4, 3}), new Round(1000, 4, 4, new int[]{4, 3}), new Round(1000, 4, 4, new int[]{3, 3})));
        fruitLevels.add(new Level(27, Level.LevelType.FRUIT, new Round(1000, 5, 4, new int[]{4, 3}), new Round(1000, 5, 4, new int[]{4, 3}), new Round(1000, 4, 5, new int[]{3, 3})));
        fruitLevels.add(new Level(28, Level.LevelType.FRUIT, new Round(1000, 5, 5, new int[]{4, 3}), new Round(1000, 5, 5, new int[]{4, 3}), new Round(1000, 5, 5, new int[]{3, 3})));
        fruitLevels.add(new Level(29, Level.LevelType.FRUIT, new Round(1000, 4, 4, new int[]{4, 4}), new Round(1000, 4, 4, new int[]{4, 4}), new Round(1000, 3, 4, new int[]{4, 3})));
        fruitLevels.add(new Level(30, Level.LevelType.FRUIT, new Round(1000, 5, 4, new int[]{4, 4}), new Round(1000, 5, 4, new int[]{4, 4}), new Round(1000, 4, 4, new int[]{4, 3})));

        fruitLevels.add(new Level(31, Level.LevelType.FRUIT, new Round(1000, 3, 3, new int[]{1, 1, 1}), new Round(1000, 3, 3, new int[]{1, 1, 1}), new Round(1000, 3, 3, new int[]{1, 1, 1})));
        fruitLevels.add(new Level(32, Level.LevelType.FRUIT, new Round(1000, 3, 3, new int[]{2, 1, 1}), new Round(1000, 3, 3, new int[]{2, 1, 1}), new Round(1000, 3, 3, new int[]{2, 1, 1})));
        fruitLevels.add(new Level(33, Level.LevelType.FRUIT, new Round(1000, 3, 3, new int[]{2, 2, 1}), new Round(1000, 3, 3, new int[]{2, 2, 1}), new Round(1000, 3, 4, new int[]{2, 1, 1})));
        fruitLevels.add(new Level(34, Level.LevelType.FRUIT, new Round(1000, 3, 4, new int[]{2, 2, 1}), new Round(1000, 3, 4, new int[]{2, 2, 1}), new Round(1000, 4, 4, new int[]{2, 1, 1})));
        fruitLevels.add(new Level(35, Level.LevelType.FRUIT, new Round(1000, 4, 4, new int[]{2, 2, 1}), new Round(1000, 4, 4, new int[]{2, 2, 1}), new Round(1000, 4, 5, new int[]{2, 1, 1})));
        fruitLevels.add(new Level(36, Level.LevelType.FRUIT, new Round(1000, 4, 5, new int[]{2, 2, 1}), new Round(1000, 4, 5, new int[]{2, 2, 1}), new Round(1000, 5, 5, new int[]{2, 1, 1})));
        fruitLevels.add(new Level(37, Level.LevelType.FRUIT, new Round(1000, 5, 5, new int[]{2, 2, 1}), new Round(1000, 5, 5, new int[]{2, 2, 1}), new Round(1000, 3, 3, new int[]{2, 2, 1})));
        fruitLevels.add(new Level(38, Level.LevelType.FRUIT, new Round(1000, 3, 3, new int[]{2, 2, 2}), new Round(1000, 3, 3, new int[]{2, 2, 2}), new Round(1000, 3, 4, new int[]{2, 2, 1})));
        fruitLevels.add(new Level(39, Level.LevelType.FRUIT, new Round(1000, 3, 4, new int[]{2, 2, 2}), new Round(1000, 3, 4, new int[]{2, 2, 2}), new Round(1000, 4, 4, new int[]{2, 2, 1})));
        fruitLevels.add(new Level(40, Level.LevelType.FRUIT, new Round(1000, 4, 4, new int[]{2, 2, 2}), new Round(1000, 4, 4, new int[]{2, 2, 2}), new Round(1000, 4, 5, new int[]{2, 2, 1})));

        this.levelsPages.add(new LevelsPage(Level.LevelType.FRUIT, fruitLevels));
        this.levels.addAll(fruitLevels);

        ArrayList<Level> vegetableLevels = new ArrayList<>();
        vegetableLevels.add(new Level(41, Level.LevelType.VEGETABLE, new Round(1000, 4, 5, new int[]{2, 2, 2}), new Round(1000, 4, 5, new int[]{2, 2, 2}), new Round(1000, 5, 5, new int[]{2, 2, 1})));
        vegetableLevels.add(new Level(42, Level.LevelType.VEGETABLE, new Round(1000, 5, 5, new int[]{2, 2, 2}), new Round(1000, 5, 5, new int[]{2, 2, 2}), new Round(1000, 3, 3, new int[]{2, 2, 2})));
        vegetableLevels.add(new Level(43, Level.LevelType.VEGETABLE, new Round(1000, 3, 4, new int[]{3, 2, 2}), new Round(1000, 3, 4, new int[]{3, 2, 2}), new Round(1000, 3, 4, new int[]{2, 2, 2})));
        vegetableLevels.add(new Level(44, Level.LevelType.VEGETABLE, new Round(1000, 4, 4, new int[]{3, 2, 2}), new Round(1000, 4, 4, new int[]{3, 2, 2}), new Round(1000, 4, 4, new int[]{2, 2, 2})));
        vegetableLevels.add(new Level(45, Level.LevelType.VEGETABLE, new Round(1000, 4, 5, new int[]{3, 2, 2}), new Round(1000, 4, 5, new int[]{3, 2, 2}), new Round(1000, 4, 5, new int[]{2, 2, 2})));
        vegetableLevels.add(new Level(46, Level.LevelType.VEGETABLE, new Round(1000, 5, 5, new int[]{3, 2, 2}), new Round(1000, 5, 5, new int[]{3, 2, 2}), new Round(1000, 5, 5, new int[]{2, 2, 2})));
        vegetableLevels.add(new Level(47, Level.LevelType.VEGETABLE, new Round(1000, 3, 4, new int[]{3, 3, 2}), new Round(1000, 3, 4, new int[]{3, 3, 2}), new Round(1000, 3, 3, new int[]{3, 2, 2})));
        vegetableLevels.add(new Level(48, Level.LevelType.VEGETABLE, new Round(1000, 4, 4, new int[]{3, 3, 2}), new Round(1000, 4, 4, new int[]{3, 3, 2}), new Round(1000, 3, 4, new int[]{3, 2, 2})));
        vegetableLevels.add(new Level(49, Level.LevelType.VEGETABLE, new Round(1000, 4, 5, new int[]{3, 3, 2}), new Round(1000, 4, 5, new int[]{3, 3, 2}), new Round(1000, 4, 4, new int[]{3, 2, 2})));
        vegetableLevels.add(new Level(50, Level.LevelType.VEGETABLE, new Round(1000, 5, 5, new int[]{3, 3, 2}), new Round(1000, 5, 5, new int[]{3, 3, 2}), new Round(1000, 4, 5, new int[]{3, 2, 2})));

        vegetableLevels.add(new Level(51, Level.LevelType.VEGETABLE, new Round(1000, 4, 4, new int[]{3, 3, 3}), new Round(1000, 4, 4, new int[]{3, 3, 3}), new Round(1000, 5, 5, new int[]{3, 2, 2})));
        vegetableLevels.add(new Level(52, Level.LevelType.VEGETABLE, new Round(1000, 4, 5, new int[]{3, 3, 3}), new Round(1000, 4, 5, new int[]{3, 3, 3}), new Round(1000, 3, 4, new int[]{3, 3, 2})));
        vegetableLevels.add(new Level(53, Level.LevelType.VEGETABLE, new Round(1000, 5, 5, new int[]{3, 3, 3}), new Round(1000, 5, 5, new int[]{3, 3, 3}), new Round(1000, 4, 4, new int[]{3, 3, 2})));
        vegetableLevels.add(new Level(54, Level.LevelType.VEGETABLE, new Round(1000, 4, 4, new int[]{4, 3, 3}), new Round(1000, 4, 4, new int[]{4, 3, 3}), new Round(1000, 4, 5, new int[]{3, 3, 2})));
        vegetableLevels.add(new Level(55, Level.LevelType.VEGETABLE, new Round(1000, 4, 5, new int[]{4, 3, 3}), new Round(1000, 4, 5, new int[]{4, 3, 3}), new Round(1000, 5, 5, new int[]{3, 3, 2})));
        vegetableLevels.add(new Level(56, Level.LevelType.VEGETABLE, new Round(1000, 5, 5, new int[]{4, 3, 3}), new Round(1000, 5, 5, new int[]{4, 3, 3}), new Round(1000, 3, 4, new int[]{3, 3, 3})));
        vegetableLevels.add(new Level(57, Level.LevelType.VEGETABLE, new Round(1000, 4, 4, new int[]{4, 4, 3}), new Round(1000, 4, 4, new int[]{4, 4, 3}), new Round(1000, 4, 4, new int[]{3, 3, 3})));
        vegetableLevels.add(new Level(58, Level.LevelType.VEGETABLE, new Round(1000, 4, 5, new int[]{4, 4, 3}), new Round(1000, 4, 5, new int[]{4, 4, 3}), new Round(1000, 4, 5, new int[]{3, 3, 3})));
        vegetableLevels.add(new Level(59, Level.LevelType.VEGETABLE, new Round(1000, 5, 5, new int[]{4, 4, 3}), new Round(1000, 5, 5, new int[]{4, 4, 3}), new Round(1000, 5, 5, new int[]{3, 3, 3})));
        vegetableLevels.add(new Level(60, Level.LevelType.VEGETABLE, new Round(1000, 5, 5, new int[]{4, 4, 4}), new Round(1000, 5, 5, new int[]{4, 4, 4}), new Round(1000, 5, 5, new int[]{4, 3, 3})));

      this.levelsPages.add(new LevelsPage(Level.LevelType.VEGETABLE, vegetableLevels));
        this.levels.addAll(vegetableLevels);

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

    public int getCompletedStarCount() {
        int starCount = 0;

        Cursor cursor = database.rawQuery("SELECT SUM(" + LevelDatabaseHelper.LevelEntry.LEVEL_STARS_COUNT_COLUMN + ") FROM " + LevelDatabaseHelper.LevelEntry.TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            starCount = cursor.getInt(0);
        }
        cursor.close();

        return starCount;
    }

    public int getStarCount() {
        return levels.size() * STARS_PER_LEVEL;
    }

    public int getEnabledLevelCount() {
        int enabledLevelCount = 0;

        Cursor cursor = database.rawQuery("SELECT SUM(" + LevelDatabaseHelper.LevelEntry.LEVEL_ENABLED_COLUMN + ") FROM " + LevelDatabaseHelper.LevelEntry.TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            enabledLevelCount = cursor.getInt(0);
        }
        cursor.close();

        return enabledLevelCount;
    }

    public int getLevelCount() {
        return levels.size();
    }

    public List<LevelsPage> getLevelsPages() {
        return levelsPages;
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

            database.update(LevelDatabaseHelper.LevelEntry.TABLE_NAME, contentValues, LevelDatabaseHelper.LevelEntry._ID + " = ?", new String[] { String.valueOf(levelId) });
        }
        cursor.close();

        return updatingLevel;
    }

    public Level unlockLevel(int levelId) {
        Level updatingLevel = levels.get(levelId - 1);
        updatingLevel.setEnabled(true);

        ContentValues contentValues = new ContentValues();
        contentValues.put(LevelDatabaseHelper.LevelEntry._ID, levelId);
        contentValues.put(LevelDatabaseHelper.LevelEntry.LEVEL_ENABLED_COLUMN, true);

        database.insert(LevelDatabaseHelper.LevelEntry.TABLE_NAME, null, contentValues);

        return updatingLevel;
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