package com.greenkeycompany.librain.campaign.menu.view;

import android.content.Intent;
import android.support.annotation.NonNull;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.widget.TextView;
import android.widget.Toast;

import com.greenkeycompany.librain.R;
import com.greenkeycompany.librain.app.App;
import com.greenkeycompany.librain.app.util.PremiumUtil;
import com.greenkeycompany.librain.campaign.CampaignGameActivity;
import com.greenkeycompany.librain.campaign.menu.presenter.ICampaignMenuPresenter;
import com.greenkeycompany.librain.campaign.menu.view.fragment.LevelPageFragment;
import com.greenkeycompany.librain.campaign.menu.presenter.CampaignMenuPresenter;
import com.greenkeycompany.librain.dao.LevelDao;
import com.greenkeycompany.librain.level.Level;
import com.greenkeycompany.librain.level.LevelsPage;
import com.greenkeycompany.librain.mainmenu.view.MainMenuActivity;
import com.greenkeycompany.librain.purchase.PurchaseActivity;
import com.greenkeycompany.librain.campaign.menu.view.viewpagerindicator.ViewPagerIndicator;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CampaignMenuActivity extends MvpActivity<ICampaignMenuView, ICampaignMenuPresenter>
        implements ICampaignMenuView,
        LevelPageFragment.LevelPageFragmentListener {

    @NonNull
    @Override
    public ICampaignMenuPresenter createPresenter() {
        return new CampaignMenuPresenter(LevelDao.getInstance(this));
    }

    private PremiumUtil premiumUtil = App.get().getPremiumUtil();

    @BindView(R.id.toolbar) Toolbar toolbar;

    @BindView(R.id.level_count_text_view) TextView levelCountTextView;
    @BindView(R.id.star_count_text_view) TextView starCountTextView;
    @BindView(R.id.premium_view) View premiumView;

    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.view_pager_indicator) ViewPagerIndicator viewPagerIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.campaign_menu_activity);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.action_bar_back_icon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        premiumView.setVisibility(premiumUtil.isPremiumUser() ? View.INVISIBLE : View.VISIBLE);

        viewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        viewPagerIndicator.addViewPagerObserve(viewPager);

        presenter.requestToUpdateViews();
    }

    @OnClick(R.id.premium_view)
    public void onPremiumViewClick() {
        startActivityForResult(new Intent(this, PurchaseActivity.class), PURCHASE_REQUEST_CODE);
    }

    private final String format = "%1$d/%2$d";

    @Override
    public void setStarCountView(int completedStarCount, int starCount) {
        starCountTextView.setText(String.format(Locale.getDefault(), format, completedStarCount, starCount));
    }

    @Override
    public void setLevelCountView(int enabledLevelCount, int levelCount) {
        levelCountTextView.setText(String.format(Locale.getDefault(), format, enabledLevelCount, levelCount));
    }

    @Override
    public void setLevelsPages(@NonNull List<LevelsPage> levelsPageList) {
        viewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager(), levelsPageList));
    }

    @Override
    public void onLevelPageFragmentLevelClick(Level level) {
        if (level.isEnabled()) {
            Intent startPurchaseIntent = new Intent(this, PurchaseActivity.class);
            Intent startGameIntent = new Intent(this, CampaignGameActivity.class).
                    putExtra(CampaignGameActivity.LEVEL_PARAM, level);

            if (level.isPremium()) {
                if (premiumUtil.isPremiumUser()) {
                    startActivityForResult(startGameIntent, UPDATE_LEVEL_PAGES_REQUEST_CODE);
                } else {
                    startActivityForResult(startPurchaseIntent, PURCHASE_REQUEST_CODE);
                }
            } else {
                startActivityForResult(startGameIntent, UPDATE_LEVEL_PAGES_REQUEST_CODE);
            }
        }
    }

    private static final int UPDATE_LEVEL_PAGES_REQUEST_CODE = 300;
    private static final int PURCHASE_REQUEST_CODE = 400;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case UPDATE_LEVEL_PAGES_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    presenter.requestToUpdateViews();
                    setResult(RESULT_OK); //FLAG TO UPDATE STAR COUNT IN MAIN MENU
                }
                break;
            case PURCHASE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    premiumUtil.setPremiumUser(true);
                    premiumView.setVisibility(View.INVISIBLE);

                    Toast.makeText(this, "ТЫ КУПИЛ ПРЕМИУМ!, " + premiumUtil.isPremiumUser(), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    private class SectionsPagerAdapter extends FragmentPagerAdapter {
        private final List<LevelsPage> levelsPageList;
        private SectionsPagerAdapter(FragmentManager fragmentManager, @NonNull List<LevelsPage> levelsPageList) {
            super(fragmentManager);
            this.levelsPageList = levelsPageList;
        }

        @Override
        public Fragment getItem(int position) {
            return LevelPageFragment.newInstance(levelsPageList.get(position).getLevels());
        }

        @Override
        public int getCount() {
            return levelsPageList.size();
        }
    }
}
