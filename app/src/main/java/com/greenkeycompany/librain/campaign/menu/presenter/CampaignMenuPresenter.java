package com.greenkeycompany.librain.campaign.menu.presenter;

import com.greenkeycompany.librain.campaign.menu.view.ICampaignMenuView;
import com.greenkeycompany.librain.dao.LevelDao;
import com.greenkeycompany.librain.level.LevelsPage;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.List;

/**
 * Created by tert0 on 04.09.2017.
 */

public class CampaignMenuPresenter extends MvpBasePresenter<ICampaignMenuView>
        implements ICampaignMenuPresenter {

    private LevelDao levelDao;
    public CampaignMenuPresenter(LevelDao levelDao) {
        this.levelDao = levelDao;
    }

    @Override
    public void requestToUpdateViews() {
        int starCount = levelDao.getStarCount();
        int completedStarCount = levelDao.getCompletedStarCount();

        int levelCount = levelDao.getLevelCount();
        int enabledLevelCount = levelDao.getEnabledLevelCount();

        List<LevelsPage> pageList = levelDao.getLevelsPages();

        if (isViewAttached()) {
            getView().setStarCountView(completedStarCount, starCount);
            getView().setLevelCountView(enabledLevelCount, levelCount);

            getView().setLevelsPages(pageList);
        }
    }
}
