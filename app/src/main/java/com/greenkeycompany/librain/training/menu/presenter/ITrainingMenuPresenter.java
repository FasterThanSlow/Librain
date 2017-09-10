package com.greenkeycompany.librain.training.menu.presenter;

import com.greenkeycompany.librain.training.menu.view.ITrainingMenuView;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;

/**
 * Created by tert0 on 07.09.2017.
 */

public interface ITrainingMenuPresenter extends MvpPresenter<ITrainingMenuView> {
    void init();

    void onActionBarSettingsPressed();
    void onBackPressed();


}
