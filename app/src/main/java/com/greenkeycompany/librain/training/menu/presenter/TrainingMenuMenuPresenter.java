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

    @Override
    public void init() {
        TrainingConfig trainingConfig = new TrainingConfig(
                preferenceUtil.getRowCount(),
                preferenceUtil.getColumnCount(),
                preferenceUtil.getItemCount(),
                preferenceUtil.getItemTypeCount(),
                preferenceUtil.isFirstRoundSelected(),
                preferenceUtil.isSecondRoundSelected(),
                preferenceUtil.isThirdRoundSelected());

        if (isViewAttached()) {
            getView().setMainFragment(trainingConfig);
        }
    }

    @Override
    public void onActionBarSettingsPressed() {

    }

    @Override
    public void onBackPressed() {

    }
}
