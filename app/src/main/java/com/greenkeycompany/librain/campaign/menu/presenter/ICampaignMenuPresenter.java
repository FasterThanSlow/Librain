package com.greenkeycompany.librain.campaign.menu.presenter;

import com.greenkeycompany.librain.campaign.menu.view.ICampaignMenuView;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;

/**
 * Created by tert0 on 04.09.2017.
 */

public interface ICampaignMenuPresenter extends MvpPresenter<ICampaignMenuView> {
    void init();
}
