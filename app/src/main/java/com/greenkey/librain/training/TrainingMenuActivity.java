package com.greenkey.librain.training;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.greenkey.librain.R;

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

    private int enabledColumnCount;
    private int enabledRowCount;

    private int itemCount;
    private int itemTypeCount;

    private int[] items;

    private boolean isFirstRoundSelected;
    private boolean isSecondRoundSelected;
    private boolean isThirdRoundSelected;

    private SharedPreferences sharedPreferences;

    private ImageView settingsImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.training_menu_activity);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        enabledColumnCount = sharedPreferences.getInt(ENABLED_COLUMN_COUNT_KEY, COLUMN_COUNT_DEFAULT_VALUE);
        enabledRowCount = sharedPreferences.getInt(ENABLED_ROW_COUNT_KEY, ROW_COUNT_DEFAULT_VALUE);

        itemTypeCount = sharedPreferences.getInt(ITEM_TYPE_COUNT_KEY, ITEM_TYPE_COUNT_DEFAULT_VALUE);
        itemCount = sharedPreferences.getInt(ITEM_COUNT_KEY, ITEM_COUNT_DEFAULT_VALUE);

        isFirstRoundSelected = sharedPreferences.getBoolean(FIRST_ROUND_SELECTED_KEY, ROUND_SELECTED_DEFAULT_VALUE);
        isSecondRoundSelected = sharedPreferences.getBoolean(SECOND_ROUND_SELECTED_KEY, ROUND_SELECTED_DEFAULT_VALUE);
        isThirdRoundSelected = sharedPreferences.getBoolean(THIRD_ROUND_SELECTED_KEY, ROUND_SELECTED_DEFAULT_VALUE);

        settingsImageView = (ImageView) findViewById(R.id.settings_icon_image_view);
        settingsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, TrainingBoardFragment.newInstance(enabledColumnCount, enabledRowCount))
                        .addToBackStack(null)
                        .commit();

                settingsImageView.setVisibility(View.INVISIBLE);
            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, TrainingMainFragment.newInstance(enabledColumnCount, enabledRowCount, itemTypeCount, itemCount))
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

    @Override
    public void onMainFragmentCreated() {
        settingsImageView.setVisibility(View.VISIBLE);
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

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, TrainingItemsFragment.newInstance(columnCount, rowCount, itemTypeCount, itemCount))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBoardCancelButtonPressed() {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, TrainingMainFragment.newInstance(enabledColumnCount, enabledRowCount, itemTypeCount, itemCount))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onItemsFragmentNext(int itemTypeCount, int itemCount, int[] items) {
        this.items = items;

        sharedPreferences.edit()
                .putInt(ITEM_TYPE_COUNT_KEY, itemTypeCount)
                .putInt(ITEM_COUNT_KEY, itemCount)
                .apply();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, TrainingRoundFragment.newInstance(isFirstRoundSelected, isSecondRoundSelected, isThirdRoundSelected))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onItemsFragmentPrevious() {
        onBackPressed();
    }

    @Override
    public void onRoundFragmentNext(boolean isFirstRoundSelected, boolean isSecondRoundSelected, boolean isThirdRoundSelected) {
        this.isFirstRoundSelected = isFirstRoundSelected;
        this.isSecondRoundSelected = isSecondRoundSelected;
        this.isThirdRoundSelected = isThirdRoundSelected;

        sharedPreferences.edit()
                .putBoolean(FIRST_ROUND_SELECTED_KEY, isFirstRoundSelected)
                .putBoolean(SECOND_ROUND_SELECTED_KEY, isSecondRoundSelected)
                .putBoolean(THIRD_ROUND_SELECTED_KEY, isThirdRoundSelected)
                .apply();

        Intent intent = new Intent(TrainingMenuActivity.this, TrainingGameActivity.class);

        TrainingLevel trainingLevel = new TrainingLevel(enabledColumnCount, enabledRowCount,
                isFirstRoundSelected, isSecondRoundSelected, isThirdRoundSelected ,items);

        intent.putExtra(LEVEL_PARAM, trainingLevel);

        startActivity(intent);
    }

    @Override
    public void onRoundFragmentPrevious() {
        onBackPressed();
    }
}
