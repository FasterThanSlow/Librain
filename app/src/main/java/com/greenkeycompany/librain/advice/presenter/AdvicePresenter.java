package com.greenkeycompany.librain.advice.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.greenkeycompany.librain.R;
import com.greenkeycompany.librain.advice.model.Advice;
import com.greenkeycompany.librain.advice.view.IAdviseView;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tert0 on 04.09.2017.
 */

public class AdvicePresenter extends MvpBasePresenter<IAdviseView>
        implements IAdvicePresenter {

    private Context context;
    public AdvicePresenter(@NonNull Context context) {
        this.context = context;
    }

    private List<Advice> adviceList;

    @Override
    public void init() {
        String[] adviceTextArray = context.getResources().getStringArray(R.array.advices_array);

        adviceList = new ArrayList<>(adviceTextArray.length);
        for (String advice : adviceTextArray) {
            adviceList.add(new Advice(advice));
        }

        if (isViewAttached()) {
            getView().setAdvises(adviceList);
        }
    }

    @Override
    public void onItemSelected(int index) {

    }
}
