package com.greenkeycompany.librain.advice.view;

import android.support.annotation.NonNull;

import com.greenkeycompany.librain.advice.model.Advice;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import java.util.List;

/**
 * Created by tert0 on 04.09.2017.
 */

public interface IAdviseView extends MvpView {
    void setAdvises(@NonNull List<Advice> adviceList);
}
