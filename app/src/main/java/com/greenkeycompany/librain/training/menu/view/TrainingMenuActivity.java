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
import com.greenkeycompany.librain.training.menu.fragment.TrainingRoundsFragment;
import com.greenkeycompany.librain.training.entity.TrainingConfig;
import com.greenkeycompany.librain.training.util.TrainingPreferenceUtil;
import com.greenkeycompany.librain.training.menu.presenter.ITrainingMenuPresenter;
import com.greenkeycompany.librain.training.menu.presenter.TrainingMenuMenuPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrainingMenuActivity extends MvpActivity<ITrainingMenuView, ITrainingMenuPresenter>
        implements ITrainingMenuView,
        TrainingBoardFragment.BoardFragmentListener,
        TrainingItemsFragment.ItemsFragmentListener,
        TrainingRoundsFragment.RoundFragmentListener {


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
                presenter.requestToSetSettingsBoardFragment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        presenter.onBackPressed();
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
    public void setSettingsBoardFragment(int rowCount, int columnCount) {
        setFragment(TrainingBoardFragment.newInstance(rowCount, columnCount));
    }

    @Override
    public void setSettingsItemsFragment(int rowCount, int columnCount, int itemTypeCount, int itemCount) {
        setFragment(TrainingItemsFragment.newInstance(rowCount, columnCount, itemTypeCount, itemCount));
    }

    @Override
    public void setSettingsRoundsFragment(boolean firstRound, boolean secondRound, boolean thirdRound) {
        setFragment(TrainingRoundsFragment.newInstance(firstRound, secondRound, thirdRound));
    }

    @Override
    public void onBoardFragmentNextButtonPressed(int rowCount, int columnCount) {
        presenter.saveBoardSettings(rowCount, columnCount);
        presenter.requestToSetSettingsItemsFragment();
    }

    @Override
    public void onItemsFragmentNext(int itemTypeCount, int itemCount) {
        presenter.saveItemsSettings(itemTypeCount, itemCount);
        presenter.requestToSetSettingsRoundsFragment();
    }

    @Override
    public void onRoundsFragmentNextClick(boolean firstRoundSelected, boolean secondRoundSelected, boolean thirdRoundSelected) {
        presenter.saveRoundsSettings(firstRoundSelected, secondRoundSelected, thirdRoundSelected);
        presenter.requestToSetMainFragment();
    }

    @Override
    public void onBoardFragmentPreviousButtonPressed() {
        presenter.requestToSetMainFragment();
    }

    @Override
    public void onItemsFragmentPrevious() {
        presenter.requestToSetSettingsBoardFragment();
    }

    @Override
    public void onRoundFragmentPreviousClick() {
        presenter.requestToSetSettingsItemsFragment();
    }
}
