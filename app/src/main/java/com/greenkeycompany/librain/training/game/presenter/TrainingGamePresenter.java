package com.greenkeycompany.librain.training.game.presenter;

import android.support.annotation.NonNull;

import com.greenkeycompany.librain.training.entity.TrainingConfig;
import com.greenkeycompany.librain.training.game.view.ITrainingGameView;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tert0 on 08.09.2017.
 */

public class TrainingGamePresenter extends MvpBasePresenter<ITrainingGameView>
        implements ITrainingGamePresenter {

    private List<Round> roundList;

    private Round round;
    private enum Round {FIRST, SECOND, THIRD}

    private int score;
    private int roundIndex;

    private TrainingConfig config;

    @Override
    public void init(@NonNull TrainingConfig config) {
        this.config = config;
        this.roundList = new ArrayList<>();

        if (config.isFirstRoundSelected()) roundList.add(Round.FIRST);
        if (config.isSecondRoundSelected()) roundList.add(Round.SECOND);
        if (config.isThirdRoundSelected()) roundList.add(Round.THIRD);

        if (isViewAttached()) {
            getView().initRatingBar(roundList.size());
            getView().setRatingBarProgress(score);
            getView().setRatingBarSelected(roundIndex);
            getView().initBoard(config.getRowCount(), config.getColumnCount());
        }
    }

    @Override
    public void requestToCheckResult() {

    }
}
