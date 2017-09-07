package com.greenkeycompany.librain.mainmenu.presenter;

import com.greenkeycompany.librain.mainmenu.view.IMainMenuView;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;

/**
 * Created by tert0 on 07.09.2017.
 */

public interface IMainMenuPresenter extends MvpPresenter<IMainMenuView> {
     void requestToUpdateRatingView(boolean isPremium);
     void requestToUpdateCampaignStarView();
}
