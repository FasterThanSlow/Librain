package com.greenkey.librain.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.greenkey.librain.entity.ResourceType;
import com.greenkey.librain.entity.Rule;
import com.greenkey.librain.campaign.Level;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 02.03.2017.
 */

public class LevelDao {

    public static final int LEVELS_COUNT = 30;

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
        this.levels.add(new Level(2, 1000, 3, 3, new LevelEnvironment(LevelEnvironment.LevelEnvironmentType.SPACE, new int[] {1, 3} )));
        this.levels.add(new Level(3, 1000, 3, 3, new LevelEnvironment(LevelEnvironment.LevelEnvironmentType.SPACE, new int[] {1, 3} )));
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

        for (Level le : levels) {
            le.setEnabled(true);
        }
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
    }

    private static final int LEVELS_PER_PAGE = 20;

    public int getLevelsPagesCount() {
        return 2;
    }

    public Level[] getLevelsPage(int pageIndex) {
        int startIndex = pageIndex * LEVELS_PER_PAGE;
        int endIndex = (pageIndex + 1) * LEVELS_PER_PAGE;

        if (endIndex > levels.size()) {
            endIndex = levels.size();
        }

        return levels.subList(startIndex, endIndex).toArray(new Level[endIndex - startIndex]);
    }

    public List<Level> getLevels() {
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

                    level.setEnabled(true); // true
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        return levels;
    }

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

            level.setEnabled(true);
        }
        cursor.close();

        return level;
    }

    public void updateLevel(Level level) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(LevelDatabaseHelper.LevelEntry.LEVEL_STARS_COUNT_COLUMN, level.getRecord());
        contentValues.put(LevelDatabaseHelper.LevelEntry.LEVEL_ENABLED_COLUMN, level.isEnabled());

        database.update(LevelDatabaseHelper.LevelEntry.TABLE_NAME, contentValues, LevelDatabaseHelper.LevelEntry._ID + " = '" + level.getLevelId() + "'", null);
    }
}
