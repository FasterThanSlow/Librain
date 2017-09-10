package com.greenkeycompany.librain.training.menu.view;

import android.support.annotation.NonNull;

import com.greenkeycompany.librain.training.entity.TrainingConfig;
import com.hannesdorfmann.mosby3.mvp.MvpView;

/**
 * Created by tert0 on 07.09.2017.
 */

public interface ITrainingMenuView extends MvpView {
    void setMainFragment(@NonNull TrainingConfig trainingConfig);
    void setSettingsFragment(@NonNull TrainingConfig trainingConfig);

    void setActionBarSettingsVisible(boolean visible);
}
