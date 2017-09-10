package com.greenkeycompany.librain.training.menu.view;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.greenkeycompany.librain.R;
import com.greenkeycompany.librain.training.menu.fragment.TrainingBoardFragment;
import com.greenkeycompany.librain.training.menu.fragment.TrainingItemsFragment;
import com.greenkeycompany.librain.training.menu.fragment.TrainingMainFragment;
import com.greenkeycompany.librain.training.menu.fragment.TrainingRoundFragment;
import com.greenkeycompany.librain.training.entity.TrainingConfig;
import com.greenkeycompany.librain.training.util.TrainingPreferenceUtil;
import com.greenkeycompany.librain.training.menu.presenter.ITrainingMenuPresenter;
import com.greenkeycompany.librain.training.menu.presenter.TrainingMenuMenuPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrainingMenuActivity extends MvpActivity<ITrainingMenuView, ITrainingMenuPresenter> implements
        ITrainingMenuView,

        TrainingBoardFragment.BoardFragmentListener,
        TrainingItemsFragment.ItemsFragmentListener,
        TrainingRoundFragment.RoundFragmentListener {


    @NonNull
    @Override
    public ITrainingMenuPresenter createPresenter() {
        return new TrainingMenuMenuPresenter(new TrainingPreferenceUtil(this));
    }

    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.training_menu_activity);
        ButterKnife.bind(this);

        toolbar.setTitle(R.string.training);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.action_bar_back_icon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presenter.init();
    }



    private Menu menu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.training_menu, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public void setActionBarSettingsVisible(boolean visible) {
        menu.findItem(R.id.settings).setVisible(visible);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                presenter.onActionBarSettingsPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        presenter.onBackPressed();
        /*
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
        */
    }

    private void setFragment(Fragment fragment) {
        getSupportFragmentManager().
                beginTransaction().
                replace(R.id.fragment_container, fragment).
                commit();
    }

    @Override
    public void setMainFragment(@NonNull TrainingConfig config) {
        setFragment(TrainingMainFragment.newInstance(config));
    }

    @Override
    public void setSettingsFragment(@NonNull TrainingConfig config) {

    }

    /*

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
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

*/

    @Override
    public void onBoardFragmentNextButtonPressed(int columnCount, int rowCount) {
        //this.enabledColumnCount = columnCount;
        //this.enabledRowCount = rowCount;
/*
        sharedPreferences.edit()
                .putInt(ENABLED_COLUMN_COUNT_KEY, columnCount)
                .putInt(ENABLED_ROW_COUNT_KEY, rowCount)
                .apply();

        setCurrentFragment(FragmentType.ITEMS);
        */
    }

    @Override
    public void onBoardFragmentPreviousButtonPressed() {

    }

    /*@Override
    public void onBoardCancelButtonPressed() {
        //setCurrentFragment(FragmentType.MAIN);
    }

    @Override
    public void onItemsFragmentNext(int itemTypeCount, int itemCount, int[] items) {
        //this.itemCount = itemCount;
        //this.itemTypeCount = itemTypeCount;
        //this.items = items;
/*
        sharedPreferences.edit()
                .putInt(ITEM_TYPE_COUNT_KEY, itemTypeCount)
                .putInt(ITEM_COUNT_KEY, itemCount)
                .apply();

        setCurrentFragment(FragmentType.ROUNDS);
        */
    /*}*/

    @Override
    public void onItemsFragmentNext(int itemTypeCount, int itemCount) {

    }

    @Override
    public void onItemsFragmentPrevious() {
        //setCurrentFragment(FragmentType.BOARD);
    }


    @Override
    public void onRoundFragmentNextClick(boolean firstRoundSelected,
                                         boolean secondRoundSelected,
                                         boolean thirdRoundSelected) {

        //this.isFirstRoundSelected = isFirstRoundSelected;
        //this.isSecondRoundSelected = isSecondRoundSelected;
        //this.isThirdRoundSelected = isThirdRoundSelected;
/*
        sharedPreferences.edit()
                .putBoolean(FIRST_ROUND_SELECTED_KEY, isFirstRoundSelected)
                .putBoolean(SECOND_ROUND_SELECTED_KEY, isSecondRoundSelected)
                .putBoolean(THIRD_ROUND_SELECTED_KEY, isThirdRoundSelected)
                .apply();

        setCurrentFragment(FragmentType.MAIN);
        */
    }

    @Override
    public void onRoundFragmentPreviousClick() {
        //setCurrentFragment(FragmentType.ITEMS);
    }
}
