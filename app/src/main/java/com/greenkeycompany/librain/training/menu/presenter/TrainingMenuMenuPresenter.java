package com.greenkeycompany.librain.training.menu.presenter;

import com.greenkeycompany.librain.training.entity.TrainingConfig;
import com.greenkeycompany.librain.training.util.TrainingPreferenceUtil;
import com.greenkeycompany.librain.training.menu.view.ITrainingMenuView;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

/**
 * Created by tert0 on 07.09.2017.
 */

public class TrainingMenuMenuPresenter extends MvpBasePresenter<ITrainingMenuView>
        implements ITrainingMenuPresenter {

    private enum FragmentType { MAIN, BOARD, ITEMS, ROUNDS };
    private FragmentType fragmentType;

    private TrainingPreferenceUtil preferenceUtil;
    public TrainingMenuMenuPresenter(TrainingPreferenceUtil preferenceUtil) {
        this.preferenceUtil = preferenceUtil;
    }

    private TrainingConfig config;

    @Override
    public void init() {
        config = new TrainingConfig(
                preferenceUtil.getRowCount(),
                preferenceUtil.getColumnCount(),
                preferenceUtil.getItemCount(),
                preferenceUtil.getItemTypeCount(),
                preferenceUtil.isFirstRoundSelected(),
                preferenceUtil.isSecondRoundSelected(),
                preferenceUtil.isThirdRoundSelected());

        fragmentType = FragmentType.MAIN;
        if (isViewAttached()) {
            getView().setMainFragment(config);
        }
    }

    @Override
    public void saveBoardSettings(int rowCount, int columnCount) {
        config.setRowCount(rowCount);
        config.setColumnCount(columnCount);
        preferenceUtil.setRowCount(rowCount);
        preferenceUtil.setColumnCount(columnCount);
    }

    @Override
    public void saveItemsSettings(int itemTypeCount, int itemCount) {
        config.setItemTypeCount(itemTypeCount);
        config.setItemCount(itemCount);
        preferenceUtil.setItemTypeCount(itemTypeCount);
        preferenceUtil.setItemCount(itemCount);
    }

    @Override
    public void saveRoundsSettings(boolean firstRound, boolean secondRound, boolean thirdRound) {
        config.setFirstRoundSelected(firstRound);
        config.setSecondRoundSelected(secondRound);
        config.setThirdRoundSelected(thirdRound);
        preferenceUtil.setFirstRoundSelected(firstRound);
        preferenceUtil.setSecondRoundSelected(secondRound);
        preferenceUtil.setThirdRoundSelected(thirdRound);
    }

    @Override
    public void requestToSetMainFragment() {
        fragmentType = FragmentType.MAIN;
        if (isViewAttached()) {
            getView().setMainFragment(config);
            getView().setActionBarSettingsVisible(true);
        }
    }

    @Override
    public void requestToSetSettingsBoardFragment() {
        fragmentType = FragmentType.BOARD;
        if (isViewAttached()) {
            getView().setSettingsBoardFragment(config.getRowCount(), config.getColumnCount());
            getView().setActionBarSettingsVisible(false);
        }
    }

    @Override
    public void requestToSetSettingsItemsFragment() {
        fragmentType = FragmentType.ITEMS;
        if (isViewAttached()) {
            getView().setSettingsItemsFragment(config.getRowCount(), config.getColumnCount(), config.getItemTypeCount(), config.getItemCount());
            getView().setActionBarSettingsVisible(false);
        }
    }

    @Override
    public void requestToSetSettingsRoundsFragment() {
        fragmentType = FragmentType.ROUNDS;
        if (isViewAttached()) {
            getView().setSettingsRoundsFragment(config.isFirstRoundSelected(), config.isSecondRoundSelected(), config.isThirdRoundSelected());
            getView().setActionBarSettingsVisible(false);
        }
    }

    @Override
    public void onBackPressed() {
        switch (fragmentType) {
            case MAIN:
                if (isViewAttached()) {
                    getView().finish();
                }
                break;
            default:
                fragmentType = FragmentType.MAIN;
                if (isViewAttached()) {
                    getView().setMainFragment(config);
                }
                break;
        }
    }
}
