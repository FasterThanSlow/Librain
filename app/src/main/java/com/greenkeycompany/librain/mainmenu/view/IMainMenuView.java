package com.greenkeycompany.librain.mainmenu.view;

import com.hannesdorfmann.mosby3.mvp.MvpView;

/**
 * Created by tert0 on 07.09.2017.
 */

public interface IMainMenuView extends MvpView {
    void updateRatingView(boolean isPremiumUser);
    void updateCampaignStarView(int starCount);
}
