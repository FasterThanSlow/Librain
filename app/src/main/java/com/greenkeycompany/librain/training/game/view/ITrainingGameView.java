package com.greenkeycompany.librain.training.game.view;

import android.support.annotation.NonNull;

import com.greenkeycompany.librain.entity.ItemType;
import com.hannesdorfmann.mosby3.mvp.MvpView;

/**
 * Created by tert0 on 08.09.2017.
 */

public interface ITrainingGameView extends MvpView {
    void initBoard(int rowCount, int columnCount);
    void setBoardItems(@NonNull ItemType[] items);

    void initRatingBar(int max);
    void setRatingBarProgress(int progress);
    void setRatingBarSelected(int selectedIndex);

    void setCheckButtonVisibility(boolean visible);
}
