package com.greenkey.librain.training;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import com.greenkey.librain.R;
import com.greenkey.librain.view.boardview.BoardView;

public class TrainingMenuActivity extends AppCompatActivity implements
        TrainingBoardFragment.TrainingBoardFragmentListener,
        TrainingItemsFragment.TrainingItemsFragmentListener {

    private static final int ROW_COUNT_DEFAULT_VALUE = 3;
    private static final int COLUMN_COUNT_DEFAULT_VALUE = 3;

    private static final String ENABLED_ROW_COUNT_KEY = "enabled_row_count";
    private static final String ENABLED_COLUMN_COUNT_KEY = "enabled_column_count";


    private static final String ITEM_COUNT_KEY = "item_count_key";
    private static final int ITEM_COUNT_DEFAULT_VALUE = 3;

    private static final String ITEM_TYPE_COUNT_KEY = "item_type_count_key";
    private static final int ITEM_TYPE_COUNT_DEFAULT_VALUE = 1;

    private int enabledColumnCount;
    private int enabledRowCount;

    private int itemCount;
    private int itemTypeCount;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.training_menu_activity);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        enabledColumnCount = sharedPreferences.getInt(ENABLED_COLUMN_COUNT_KEY, COLUMN_COUNT_DEFAULT_VALUE);
        enabledRowCount = sharedPreferences.getInt(ENABLED_ROW_COUNT_KEY, ROW_COUNT_DEFAULT_VALUE);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, TrainingBoardFragment.newInstance(enabledColumnCount, enabledRowCount))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

/*
    @Override
    protected void onPause() {
        super.onPause();

        sharedPreferences.edit()
                .putInt(ENABLED_COLUMN_COUNT_KEY, enabledColumnCount)
                .putInt(ENABLED_ROW_COUNT_KEY, enabledRowCount)
                .putInt(ITEM_COUNT_KEY, itemCount)
                .putInt(ITEM_TYPE_COUNT_KEY, itemTypeCount)
                .apply();
    }
*/

    @Override
    public void onNext(int columnCount, int rowCount) {
        this.enabledColumnCount = columnCount;
        this.enabledRowCount = rowCount;

        sharedPreferences.edit()
                .putInt(ENABLED_COLUMN_COUNT_KEY, columnCount)
                .putInt(ENABLED_ROW_COUNT_KEY, rowCount)
                .apply();

        itemTypeCount = sharedPreferences.getInt(ITEM_TYPE_COUNT_KEY, ITEM_TYPE_COUNT_DEFAULT_VALUE);
        itemCount = sharedPreferences.getInt(ITEM_COUNT_KEY, ITEM_COUNT_DEFAULT_VALUE);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, TrainingItemsFragment.newInstance(columnCount, rowCount, itemTypeCount, itemCount))
                .addToBackStack(null)
                .commit();
    }

    private static final String LEVEL_PARAM = "training_level";

    @Override
    public void onItemsFragmentNext(int itemTypeCount, int itemCount, int[] items) {
        sharedPreferences.edit()
                .putInt(ITEM_TYPE_COUNT_KEY, itemTypeCount)
                .putInt(ITEM_COUNT_KEY, itemCount)
                .apply();

        Intent intent = new Intent(TrainingMenuActivity.this, TrainingActivity.class);
        TrainingLevel trainingLevel = new TrainingLevel(enabledColumnCount,enabledRowCount, items);
        trainingLevel.setFirstRound(true);
        trainingLevel.setThirdRound(true);
        intent.putExtra(LEVEL_PARAM, trainingLevel);

        startActivity(intent);
    }

    @Override
    public void onItemsFragmentPrevious() {
        onBackPressed();
    }
}
