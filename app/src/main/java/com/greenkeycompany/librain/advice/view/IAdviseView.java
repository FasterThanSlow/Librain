package com.greenkeycompany.librain.advice.view;

import android.support.annotation.NonNull;

import com.greenkeycompany.librain.advice.model.Advice;
import com.greenkeycompany.librain.advice.view.fragment.advice.AdviceFragmentListener;
import com.greenkeycompany.librain.advice.view.fragment.favorite.FavoriteAdviceFragmentListener;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import java.util.ArrayList;

/**
 * Created by tert0 on 04.09.2017.
 */

public interface IAdviseView extends MvpView,
        AdviceFragmentListener,
        FavoriteAdviceFragmentListener {

    void initAdviceFragments(@NonNull ArrayList<Advice> adviceList, @NonNull ArrayList<Advice> favoriteAdviceList);

    void addItemToFavoriteAdviceFragment(@NonNull Advice advice);
    void removeItemFromFavoriteAdviceFragment(int id);

    void notifyItemChangeInAdviceFragment(int id);
}
