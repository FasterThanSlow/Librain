package com.greenkeycompany.librain.campaign.menu.view.viewpagerindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.greenkeycompany.librain.R;
import com.greenkeycompany.librain.view.PixelConverter;

/**
 * Created by Alexander on 11.03.2017.
 */

public class ViewPagerIndicator extends LinearLayout {

    private static final int DEFAULT_INDICATOR_DRAWABLE = R.drawable.view_pager_indicator;
    private static final int DEFAULT_SELECTED_DRAWABLE_DRAWABLE = R.drawable.view_pager_indicator_selected;

    private static final int INDICATOR_PADDING_DP = 4;

    private ImageView[] indicatorsImageViews;

    private ViewPager viewPager;

    private int indicatorId;
    private int selectedIndicatorId;

    public ViewPagerIndicator(Context context) {
        super(context);
        init(null, 0);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        this.setOrientation(HORIZONTAL);
        this.setGravity(Gravity.CENTER_VERTICAL);

        final TypedArray typedArray = getContext().obtainStyledAttributes(
                attrs, R.styleable.ViewPagerIndicator, defStyle, 0);

        indicatorId = typedArray.getResourceId(R.styleable.ViewPagerIndicator_indicatorSrc, DEFAULT_INDICATOR_DRAWABLE);
        selectedIndicatorId = typedArray.getResourceId(R.styleable.ViewPagerIndicator_selectedIndicatorSrc, DEFAULT_SELECTED_DRAWABLE_DRAWABLE);

        typedArray.recycle();
    }

    private void initIndicators(int count, int selectedIndex) {
        this.removeAllViews();

        int paddingPx = PixelConverter.dpToPx(getContext(), INDICATOR_PADDING_DP);

        indicatorsImageViews = new ImageView[count];
        for (int i = 0; i < count; i++) {
            indicatorsImageViews[i] = new ImageView(getContext());
            indicatorsImageViews[i].setPadding(paddingPx, paddingPx, paddingPx, paddingPx);
            if (i == selectedIndex) {
                indicatorsImageViews[i].setImageResource(selectedIndicatorId);
            } else {
                indicatorsImageViews[i].setImageResource(indicatorId);
            }

            this.addView(indicatorsImageViews[i]);
        }
    }

    private void setSelectedIndicator(int position) {
        int count = indicatorsImageViews.length;
        for (int i = 0; i < count; i++) {
            if (i == position) {
                indicatorsImageViews[i].setImageResource(selectedIndicatorId);
            } else {
                indicatorsImageViews[i].setImageResource(indicatorId);
            }
        }
    }

    public void addViewPagerObserve(@NonNull ViewPager viewPager) {
        this.viewPager = viewPager;

        PagerAdapter adapter = viewPager.getAdapter();
        if (adapter != null) {
            initIndicators(adapter.getCount(), viewPager.getCurrentItem());
        }

        viewPager.addOnAdapterChangeListener(onAdapterChangeListener);
        viewPager.addOnPageChangeListener(onPageChangeListener);
    }

    public void removeViewPagerObserve() {
        this.removeAllViews();

        viewPager.removeOnAdapterChangeListener(onAdapterChangeListener);
        viewPager.removeOnPageChangeListener(onPageChangeListener);

        viewPager = null;
    }

    private ViewPager.OnAdapterChangeListener onAdapterChangeListener = new ViewPager.OnAdapterChangeListener() {
        @Override
        public void onAdapterChanged(@NonNull ViewPager viewPager, @Nullable PagerAdapter oldAdapter, @Nullable PagerAdapter newAdapter) {
            if (newAdapter != null) {
                initIndicators(newAdapter.getCount(), viewPager.getCurrentItem());
            }
        }
    };

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {


            setSelectedIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}
