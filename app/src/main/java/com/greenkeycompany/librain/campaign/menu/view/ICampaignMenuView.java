package com.greenkeycompany.librain.campaign.menu.view;

import android.support.annotation.NonNull;

import com.greenkeycompany.librain.level.LevelsPage;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import java.util.List;

/**
 * Created by tert0 on 04.09.2017.
 */

public interface ICampaignMenuView extends MvpView {
    void setStarCountView(int completedStarCount, int starCount);
    void setLevelCountView(int enabledLevelCount, int levelCount);

    void setLevelsPages(@NonNull List<LevelsPage> levelsPageList);
}
