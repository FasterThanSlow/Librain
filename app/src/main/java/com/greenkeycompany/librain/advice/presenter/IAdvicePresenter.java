package com.greenkeycompany.librain.advice.presenter;

import com.greenkeycompany.librain.advice.view.IAdviseView;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;

/**
 * Created by tert0 on 04.09.2017.
 */

public interface IAdvicePresenter extends MvpPresenter<IAdviseView> {
    void init();
    void onItemSelected(int index);
}
