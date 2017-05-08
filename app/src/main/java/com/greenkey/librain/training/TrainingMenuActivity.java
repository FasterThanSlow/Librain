package com.greenkey.librain.training;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.greenkey.librain.R;
import com.greenkey.librain.training.entity.TrainingLevel;
import com.greenkey.librain.training.fragment.TrainingBoardFragment;
import com.greenkey.librain.training.fragment.TrainingItemsFragment;
import com.greenkey.librain.training.fragment.TrainingMainFragment;
import com.greenkey.librain.training.fragment.TrainingRoundFragment;

public class TrainingMenuActivity extends AppCompatActivity implements
        TrainingMainFragment.MainFragmentListener,
        TrainingBoardFragment.BoardFragmentListener,
        TrainingItemsFragment.ItemsFragmentListener,
        TrainingRoundFragment.RoundFragmentListener {

    private static final String LEVEL_PARAM = "training_level";

    private static final int ROW_COUNT_DEFAULT_VALUE = 3;
    private static final int COLUMN_COUNT_DEFAULT_VALUE = 3;

    private static final String ENABLED_ROW_COUNT_KEY = "enabled_row_count";
    private static final String ENABLED_COLUMN_COUNT_KEY = "enabled_column_count";

    private static final String ITEM_COUNT_KEY = "item_count_key";
    private static final int ITEM_COUNT_DEFAULT_VALUE = 3;

    private static final String ITEM_TYPE_COUNT_KEY = "item_type_count_key";
    private static final int ITEM_TYPE_COUNT_DEFAULT_VALUE = 1;

    private static final String FIRST_ROUND_SELECTED_KEY = "first_round_selected";
    private static final String SECOND_ROUND_SELECTED_KEY = "second_round_selected";
    private static final String THIRD_ROUND_SELECTED_KEY = "third_round_selected";

    private static final boolean ROUND_SELECTED_DEFAULT_VALUE = true;

    private static final String FIRST_TRAINING_LAUNCHING_KEY = "training_first_launching";
    private static final boolean FIRST_TRAINING_LAUNCHING_DEFAULT_VALUE = true;

    private int enabledColumnCount;
    private int enabledRowCount;

    private int itemCount;
    private int itemTypeCount;

    private int[] items;

    private boolean isFirstRoundSelected;
    private boolean isSecondRoundSelected;
    private boolean isThirdRoundSelected;

    private SharedPreferences sharedPreferences;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.training_menu_activity);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        fragmentManager = getSupportFragmentManager();

        enabledColumnCount = sharedPreferences.getInt(ENABLED_COLUMN_COUNT_KEY, COLUMN_COUNT_DEFAULT_VALUE);
        enabledRowCount = sharedPreferences.getInt(ENABLED_ROW_COUNT_KEY, ROW_COUNT_DEFAULT_VALUE);

        itemTypeCount = sharedPreferences.getInt(ITEM_TYPE_COUNT_KEY, ITEM_TYPE_COUNT_DEFAULT_VALUE);
        itemCount = sharedPreferences.getInt(ITEM_COUNT_KEY, ITEM_COUNT_DEFAULT_VALUE);

        isFirstRoundSelected = sharedPreferences.getBoolean(FIRST_ROUND_SELECTED_KEY, ROUND_SELECTED_DEFAULT_VALUE);
        isSecondRoundSelected = sharedPreferences.getBoolean(SECOND_ROUND_SELECTED_KEY, ROUND_SELECTED_DEFAULT_VALUE);
        isThirdRoundSelected = sharedPreferences.getBoolean(THIRD_ROUND_SELECTED_KEY, ROUND_SELECTED_DEFAULT_VALUE);

        final boolean isFirstLaunching = sharedPreferences.getBoolean(FIRST_TRAINING_LAUNCHING_KEY, FIRST_TRAINING_LAUNCHING_DEFAULT_VALUE);
        if (isFirstLaunching) {
            currentFragmentType = FragmentType.BOARD;

            sharedPreferences.edit().putBoolean(FIRST_TRAINING_LAUNCHING_KEY, false).apply();
        } else {
            currentFragmentType = FragmentType.MAIN;
        }

        setCurrentFragment(currentFragmentType);
    }

    @Override
    public void onBackPressed() {
        switch (currentFragmentType) {
            case MAIN:
                finish();
                break;
            case BOARD:
                setCurrentFragment(FragmentType.MAIN);
                break;
            case ITEMS:
                setCurrentFragment(FragmentType.BOARD);
                break;
            case ROUNDS:
                setCurrentFragment(FragmentType.ITEMS);
                break;
        }
    }

    private enum FragmentType {MAIN, BOARD, ITEMS, ROUNDS};
    private FragmentType currentFragmentType;

    private void setCurrentFragment(FragmentType fragmentType) {
        currentFragmentType = fragmentType;

        Fragment fragment = null;

        switch (fragmentType) {
            case MAIN:
                fragment = TrainingMainFragment.newInstance(
                        enabledColumnCount, enabledRowCount,
                        itemTypeCount, itemCount,
                        isFirstRoundSelected, isSecondRoundSelected, isThirdRoundSelected);
            break;
            case BOARD:
                fragment = TrainingBoardFragment.newInstance(enabledColumnCount, enabledRowCount);
                break;
            case ITEMS:
                fragment = TrainingItemsFragment.newInstance(enabledColumnCount, enabledRowCount, itemTypeCount, itemCount);
            break;
            case ROUNDS:
                fragment = TrainingRoundFragment.newInstance(isFirstRoundSelected, isSecondRoundSelected, isThirdRoundSelected);
                break;
        }

        fragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }


    @Override
    public void onMainFragmentSettingsPressed() {
        setCurrentFragment(FragmentType.BOARD);
    }

    @Override
    public void onMainFragmentStartPressed(int[] items) {
        this.items = items;

        Intent intent = new Intent(TrainingMenuActivity.this, TrainingGameActivity.class);

        TrainingLevel trainingLevel = new TrainingLevel(
                enabledRowCount, enabledColumnCount,
                isFirstRoundSelected, isSecondRoundSelected, isThirdRoundSelected,
                items);

        intent.putExtra(LEVEL_PARAM, trainingLevel);

        startActivity(intent);
    }


    @Override
    public void onBoardFragmentNextButtonPressed(int columnCount, int rowCount) {
        this.enabledColumnCount = columnCount;
        this.enabledRowCount = rowCount;

        sharedPreferences.edit()
                .putInt(ENABLED_COLUMN_COUNT_KEY, columnCount)
                .putInt(ENABLED_ROW_COUNT_KEY, rowCount)
                .apply();

        setCurrentFragment(FragmentType.ITEMS);
    }

    @Override
    public void onBoardCancelButtonPressed() {
        setCurrentFragment(FragmentType.MAIN);
    }

    @Override
    public void onItemsFragmentNext(int itemTypeCount, int itemCount, int[] items) {
        this.itemCount = itemCount;
        this.itemTypeCount = itemTypeCount;
        this.items = items;

        sharedPreferences.edit()
                .putInt(ITEM_TYPE_COUNT_KEY, itemTypeCount)
                .putInt(ITEM_COUNT_KEY, itemCount)
                .apply();

        setCurrentFragment(FragmentType.ROUNDS);
    }

    @Override
    public void onItemsFragmentPrevious() {
        setCurrentFragment(FragmentType.BOARD);
    }


    @Override
    public void onRoundFragmentNext(boolean isFirstRoundSelected,
                                    boolean isSecondRoundSelected,
                                    boolean isThirdRoundSelected) {

        this.isFirstRoundSelected = isFirstRoundSelected;
        this.isSecondRoundSelected = isSecondRoundSelected;
        this.isThirdRoundSelected = isThirdRoundSelected;

        sharedPreferences.edit()
                .putBoolean(FIRST_ROUND_SELECTED_KEY, isFirstRoundSelected)
                .putBoolean(SECOND_ROUND_SELECTED_KEY, isSecondRoundSelected)
                .putBoolean(THIRD_ROUND_SELECTED_KEY, isThirdRoundSelected)
                .apply();

        setCurrentFragment(FragmentType.MAIN);
    }

    @Override
    public void onRoundFragmentPrevious() {
        setCurrentFragment(FragmentType.ITEMS);
    }
}
