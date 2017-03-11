package com.greenkey.librain.campaign;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.greenkey.librain.MainActivity;
import com.greenkey.librain.R;
import com.greenkey.librain.dao.LevelDao;
import com.greenkey.librain.view.RatingBar;

public class CampaignActivity extends AppCompatActivity {

    public static final String LEVEL_PARAM = "level";

    private LevelDao levelDao;

    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;

    private ViewPagerIndicator viewPagerIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.campaign_activity);

        levelDao = LevelDao.getInstance(CampaignActivity.this);

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), levelDao); //Спорно

        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(sectionsPagerAdapter);

        viewPagerIndicator = (ViewPagerIndicator) findViewById(R.id.view_pager_indicator);
        viewPagerIndicator.addViewPagerObserve(viewPager);
    }


    public static class LevelsPageFragment extends Fragment {

        private static final int COLUMNS_COUNT = 4;
        private static final String LEVELS_PARAM = "levels";

        public LevelsPageFragment() {
        }

        public static LevelsPageFragment newInstance(Level[] levels) {
            LevelsPageFragment fragment = new LevelsPageFragment();
            Bundle args = new Bundle();
            args.putParcelableArray(LEVELS_PARAM, levels);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.campaign_fragment, container, false);

            final Level[] levels = (Level[]) getArguments().getParcelableArray(LEVELS_PARAM);
            if (levels != null) {
                final LevelsRecycleViewAdapter adapter = new LevelsRecycleViewAdapter(getContext(), levels);

                final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.campaign_fragment_recyclerview);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), COLUMNS_COUNT));
                recyclerView.setAdapter(adapter);
            }
            
            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final LevelDao levelDao;

        public SectionsPagerAdapter(FragmentManager fm, LevelDao levelDao) {
            super(fm);

            this.levelDao = levelDao;
        }

        @Override
        public Fragment getItem(int position) {
            return LevelsPageFragment.newInstance(levelDao.getLevelsPage(position));
        }

        @Override
        public int getCount() {
            return levelDao.getLevelsPagesCount();
        }

    }

    private static class LevelsRecycleViewAdapter extends RecyclerView.Adapter<LevelsRecycleViewAdapter.LevelViewHolder> {

        public static class LevelViewHolder extends RecyclerView.ViewHolder {

            public View backgroundView;
            public TextView levelNumberTextView;
            public RatingBar ratingBar;

            public LevelViewHolder(View itemView) {
                super(itemView);

                backgroundView = itemView.findViewById(R.id.campaign_item_background_view);
                levelNumberTextView = (TextView) itemView.findViewById(R.id.campaign_item_level_number);
                ratingBar = (RatingBar) itemView.findViewById(R.id.campaign_item_rating_bar);
            }
        }

        private Context context;
        private Level[] levels;

        public LevelsRecycleViewAdapter(Context context, @NonNull Level[] levels) {
            this.context = context;
            this.levels = levels;
        }

        @Override
        public LevelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new LevelViewHolder(LayoutInflater.from(context).inflate(R.layout.campaign_level_item, parent, false));
        }

        @Override
        public void onBindViewHolder(LevelViewHolder holder, int position) {
            final Level currentLevel = levels[position];

            if (currentLevel != null) {
                holder.backgroundView.setBackgroundResource(R.drawable.campaign_card_view_background_selector);

                holder.levelNumberTextView.setText(String.valueOf(currentLevel.getLevelId()));

                holder.ratingBar.setVisibility(View.VISIBLE);
                holder.ratingBar.setProgress(currentLevel.getRecord());

                if (currentLevel.isEnabled()) {
                    holder.backgroundView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra(LEVEL_PARAM, currentLevel);

                            context.startActivity(intent);
                        }
                    });
                } else {
                    holder.backgroundView.setBackgroundResource(R.drawable.campaign_disabled_card_view_background_selector);
                    holder.ratingBar.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public int getItemCount() {
            if (levels == null) {
                return 0;
            }

            return levels.length;
        }
    }
}
