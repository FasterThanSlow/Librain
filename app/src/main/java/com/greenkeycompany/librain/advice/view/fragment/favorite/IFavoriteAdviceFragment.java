package com.greenkeycompany.librain.advice.view.fragment.favorite;

import android.support.annotation.NonNull;

import com.greenkeycompany.librain.advice.model.Advice;

/**
 * Created by tert0 on 05.09.2017.
 */

public interface IFavoriteAdviceFragment {
    void addItem(@NonNull Advice advice);
    void removeItem(int id);
}
