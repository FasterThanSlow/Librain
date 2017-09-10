package com.greenkeycompany.librain.training.game.presenter;

import android.support.annotation.NonNull;

import com.greenkeycompany.librain.training.entity.TrainingConfig;
import com.greenkeycompany.librain.training.game.view.ITrainingGameView;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;

/**
 * Created by tert0 on 08.09.2017.
 */

public interface ITrainingGamePresenter extends MvpPresenter<ITrainingGameView> {
    void init(@NonNull TrainingConfig config);

    void requestToCheckResult();
}
