package com.greenkeycompany.librain.advice.view;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.greenkeycompany.librain.R;
import com.greenkeycompany.librain.advice.model.Advice;
import com.greenkeycompany.librain.advice.presenter.AdvicePresenter;
import com.greenkeycompany.librain.advice.presenter.IAdvicePresenter;
import com.greenkeycompany.librain.advice.view.fragment.advice.AdviceFragment;
import com.greenkeycompany.librain.advice.view.fragment.favorite.FavoriteAdviceFragment;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdviceActivity extends MvpActivity<IAdviseView, IAdvicePresenter>
        implements IAdviseView {

    @NonNull
    @Override
    public IAdvicePresenter createPresenter() {
        return new AdvicePresenter(this);
    }

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tab_layout) TabLayout tabLayout;

    @BindView(R.id.view_pager) ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advice_activity);
        ButterKnife.bind(this);

        //toolbar.setTitle(R.string.advices);
        //toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.action_bar_back_icon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presenter.init();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.saveData();
    }

    private AdviceFragment adviceFragment;
    private FavoriteAdviceFragment favoriteAdviceFragment;

    @Override
    public void initAdviceFragments(@NonNull ArrayList<Advice> adviceList, @NonNull ArrayList<Advice> favoriteAdviceList) {
        adviceFragment = AdviceFragment.newInstance(adviceList);
        favoriteAdviceFragment = FavoriteAdviceFragment.newInstance(favoriteAdviceList);

        viewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            private static final int FRAGMENT_COUNT = 2;

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0: return adviceFragment;
                    case 1: return favoriteAdviceFragment;
                    default: return null;
                }
            }

            @Override
            public int getCount() {
                return FRAGMENT_COUNT;
            }
        });
        tabLayout.setupWithViewPager(viewPager);

        TabLayout.Tab advicesTab = tabLayout.getTabAt(0);
        advicesTab.setIcon(R.drawable.advice_tab_advice_icon);
        //advicesTab.setText(R.string.advice_all);

        TabLayout.Tab favoriteAdvicesTab  = tabLayout.getTabAt(1);
        //favoriteAdvicesTab.setText(R.string.advice_favorite);
        favoriteAdvicesTab.setIcon(R.drawable.advice_tab_favorite_advice_icon);
    }

    @Override
    public void onFavoriteAdviceFragmentRemoveFavoriteItem(int id) {
        presenter.onFavoriteAdviceFragmentRemoveFavoriteItem(id);
    }

    @Override
    public void onAdviceFragmentAddFavoriteItem(int id) {
        presenter.onAdviceFragmentAddFavoriteItem(id);
    }

    @Override
    public void onAdviceFragmentRemoveFavoriteItem(int id) {
        presenter.onAdviceFragmentRemoveFavoriteItem(id);
    }

    @Override
    public void addItemToFavoriteAdviceFragment(@NonNull Advice advice) {
        favoriteAdviceFragment.addItem(advice);
    }

    @Override
    public void removeItemFromFavoriteAdviceFragment(int id) {
        favoriteAdviceFragment.removeItem(id);
    }

    @Override
    public void notifyItemChangeInAdviceFragment(int id) {
        adviceFragment.notifyItemChange(id);
    }
}
