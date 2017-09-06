package com.greenkeycompany.librain.mainmenu.presenter;

import com.greenkeycompany.librain.dao.LevelDao;
import com.greenkeycompany.librain.mainmenu.view.IMainMenuView;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

/**
 * Created by tert0 on 07.09.2017.
 */

public class MainMenuPresenter extends MvpBasePresenter<IMainMenuView>
        implements IMainMenuPresenter {

    private LevelDao levelDao;
    public MainMenuPresenter(LevelDao levelDao) {
        this.levelDao = levelDao;
    }

    @Override
    public void requestToUpdateCampaignStarView() {

    }

    @Override
    public void requestToUpdateRatingView() {

    }
}
