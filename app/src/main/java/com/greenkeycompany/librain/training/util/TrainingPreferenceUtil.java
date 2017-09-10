package com.greenkeycompany.librain.training.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by tert0 on 07.09.2017.
 */

public class TrainingPreferenceUtil {

    private static final String ROW_COUNT_KEY = "row_count";
    private static final int ROW_COUNT_DEFAULT_VALUE = 3;

    private static final String COLUMN_COUNT_KEY = "column_count";
    private static final int COLUMN_COUNT_DEFAULT_VALUE = 3;

    private static final String ITEM_COUNT_KEY = "item_count_key";
    private static final int ITEM_COUNT_DEFAULT_VALUE = 3;

    private static final String ITEM_TYPE_COUNT_KEY = "item_type_count_key";
    private static final int ITEM_TYPE_COUNT_DEFAULT_VALUE = 1;

    private static final String FIRST_ROUND_SELECTED_KEY = "first_round_selected";
    private static final String SECOND_ROUND_SELECTED_KEY = "second_round_selected";
    private static final String THIRD_ROUND_SELECTED_KEY = "third_round_selected";
    private static final boolean ROUND_SELECTED_DEFAULT_VALUE = true;

    //private static final String FIRST_TRAINING_LAUNCHING_KEY = "training_first_launching";
    //private static final boolean FIRST_TRAINING_LAUNCHING_DEFAULT_VALUE = true;

    private SharedPreferences sharedPreferences;
    public TrainingPreferenceUtil(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public int getColumnCount() {
        return sharedPreferences.getInt(COLUMN_COUNT_KEY, COLUMN_COUNT_DEFAULT_VALUE);
    }

    public void setColumnCount(int columnCount) {
        this.sharedPreferences.edit().putInt(COLUMN_COUNT_KEY, columnCount).apply();
    }

    public int getRowCount() {
        return sharedPreferences.getInt(ROW_COUNT_KEY, ROW_COUNT_DEFAULT_VALUE);
    }

    public void setRowCount(int rowCount) {
        this.sharedPreferences.edit().putInt(ROW_COUNT_KEY, rowCount).apply();;
    }

    public int getItemCount() {
        return sharedPreferences.getInt(ITEM_COUNT_KEY, ITEM_COUNT_DEFAULT_VALUE);
    }

    public void setItemCount(int itemCount) {
        this.sharedPreferences.edit().putInt(ITEM_COUNT_KEY, itemCount).apply();
    }

    public int getItemTypeCount() {
        return sharedPreferences.getInt(ITEM_TYPE_COUNT_KEY, ITEM_TYPE_COUNT_DEFAULT_VALUE);
    }

    public void setItemTypeCount(int itemTypeCount) {
        this.sharedPreferences.edit().putInt(ITEM_TYPE_COUNT_KEY, itemTypeCount).apply();
    }

    public boolean isFirstRoundSelected() {
        return sharedPreferences.getBoolean(FIRST_ROUND_SELECTED_KEY, ROUND_SELECTED_DEFAULT_VALUE);
    }

    public void setFirstRoundSelected(boolean firstRoundSelected) {
        this.sharedPreferences.edit().putBoolean(FIRST_ROUND_SELECTED_KEY, firstRoundSelected).apply();
    }

    public boolean isSecondRoundSelected() {
        return sharedPreferences.getBoolean(SECOND_ROUND_SELECTED_KEY, ROUND_SELECTED_DEFAULT_VALUE);
    }

    public void setSecondRoundSelected(boolean secondRoundSelected) {
        this.sharedPreferences.edit().putBoolean(SECOND_ROUND_SELECTED_KEY, secondRoundSelected).apply();
    }

    public boolean isThirdRoundSelected() {
        return sharedPreferences.getBoolean(THIRD_ROUND_SELECTED_KEY, ROUND_SELECTED_DEFAULT_VALUE);
    }

    public void setThirdRoundSelected(boolean thirdRoundSelected) {
        this.sharedPreferences.edit().putBoolean(THIRD_ROUND_SELECTED_KEY, thirdRoundSelected).apply();
    }
}
