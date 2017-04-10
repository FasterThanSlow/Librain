package com.greenkey.librain.campaign;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.greenkey.librain.GameActivity;
import com.greenkey.librain.R;
import com.greenkey.librain.dao.LevelDao;
import com.greenkey.librain.level.Level;
import com.greenkey.librain.level.LevelsPage;
import com.greenkey.librain.view.RatingBar;

import java.util.ArrayList;
import java.util.List;

public class CampaignActivity extends AppCompatActivity {

    public static final String LEVEL_PARAM = "level";

    private LevelDao levelDao;

    private TextView startCountTextView;
    private TextView enabledLevelCountTextView;

    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;

    private ViewPagerIndicator viewPagerIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.campaign_activity);

        levelDao = LevelDao.getInstance(CampaignActivity.this);

        startCountTextView = (TextView) findViewById(R.id.campaign_star_count_text_view);
        setStarsCount(levelDao.getCompletedStarCount(), levelDao.getStarCount());

        enabledLevelCountTextView = (TextView) findViewById(R.id.campaign_enabled_level_count_text_view);
        setEnabledLevelCount(levelDao.getEnabledLevelCount(), levelDao.getLevelCount());

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), levelDao.getLevelsPages());

        viewPager = (ViewPager) findViewById(R.id.campaign_level_pages_container);
        viewPager.setAdapter(sectionsPagerAdapter);

        viewPagerIndicator = (ViewPagerIndicator) findViewById(R.id.campaign_view_pager_indicator);
        viewPagerIndicator.addViewPagerObserve(viewPager);
    }

    private void setStarsCount(int completedStarsCount, int starsCount) {
        startCountTextView.setText(completedStarsCount + " / " + starsCount);
    }

    private void setEnabledLevelCount(int enabledLevelCount, int levelCount) {
        enabledLevelCountTextView.setText(String.valueOf(enabledLevelCount + " / " + levelCount));
    }

    private static final int UPDATE_REQUEST_CODE = 100;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                setStarsCount(levelDao.getCompletedStarCount(), levelDao.getStarCount());
                setEnabledLevelCount(levelDao.getEnabledLevelCount(), levelDao.getLevelCount());

                sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), levelDao.getLevelsPages());

                viewPager.setAdapter(sectionsPagerAdapter);
            }
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<LevelsPage> levelsPages;

        public SectionsPagerAdapter(FragmentManager fm, List<LevelsPage> levelsPages) {
            super(fm);

            this.levelsPages = levelsPages;
        }

        @Override
        public Fragment getItem(int position) {
            return LevelsPageFragment.newInstance(levelsPages.get(position).getLevels());
        }

        @Override
        public int getCount() {
            if (levelsPages == null) {
                return 0;
            }

            return levelsPages.size();
        }

    }

    public static class LevelsPageFragment extends Fragment {

        private static final int COLUMNS_COUNT = 4;
        private static final String LEVELS_PARAM = "levels";

        public LevelsPageFragment() {
        }

        public static LevelsPageFragment newInstance(ArrayList<Level> levels) {
            LevelsPageFragment fragment = new LevelsPageFragment();
            Bundle args = new Bundle();
            args.putParcelableArrayList(LEVELS_PARAM, levels);
            fragment.setArguments(args);
            return fragment;
        }

        private LevelsRecycleViewAdapter adapter;
        private RecyclerView recyclerView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.campaign_fragment, container, false);

            final ArrayList<Level> levels = getArguments().getParcelableArrayList(LEVELS_PARAM);
            if (levels != null) {
                adapter = new LevelsRecycleViewAdapter(getContext(), levels);

                recyclerView = (RecyclerView) rootView.findViewById(R.id.campaign_fragment_recycler_view);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), COLUMNS_COUNT));
                recyclerView.setAdapter(adapter);
            }

            return rootView;
        }
    }

    private static class LevelsRecycleViewAdapter extends RecyclerView.Adapter<LevelsRecycleViewAdapter.LevelViewHolder> {

        public static class LevelViewHolder extends RecyclerView.ViewHolder {

            public TextView levelNumberTextView;
            public RatingBar ratingBar;

            public LevelViewHolder(View itemView) {
                super(itemView);

                //backgroundView = itemView.findViewById(R.id.campaign_item_background_view);
                levelNumberTextView = (TextView) itemView.findViewById(R.id.campaign_item_level_number);
                ratingBar = (RatingBar) itemView.findViewById(R.id.campaign_item_rating_bar);
            }
        }

        private final Context context;
        private final ArrayList<Level> levels;

        public LevelsRecycleViewAdapter(Context context, ArrayList<Level> levels) {
            this.context = context;
            this.levels = levels;
        }

        @Override
        public LevelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new LevelViewHolder(LayoutInflater.from(context).inflate(R.layout.campaign_level_item, parent, false));
        }

        @Override
        public void onBindViewHolder(LevelViewHolder holder, int position) {
            final Level currentLevel = levels.get(position);

            if (currentLevel != null) {

                holder.levelNumberTextView.setText(String.valueOf(currentLevel.getLevelId()));

                holder.ratingBar.setProgress(currentLevel.getRecord());

                if (currentLevel.isPremium()) {
                    if (currentLevel.isEnabled()) {
                        holder.levelNumberTextView.setBackgroundResource(R.drawable.campaign_item_premium_circle);
                        holder.levelNumberTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, GameActivity.class);
                                intent.putExtra(LEVEL_PARAM, currentLevel);

                                ((Activity) context).startActivityForResult(intent, UPDATE_REQUEST_CODE);
                            }
                        });
                    } else {
                        holder.levelNumberTextView.setBackgroundResource(R.drawable.campaign_item_premium_disabled_circle);
                    }
                } else {
                    if (currentLevel.isEnabled()) {
                        holder.levelNumberTextView.setBackgroundResource(R.drawable.campaign_item_circle);
                        holder.levelNumberTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, GameActivity.class);
                                intent.putExtra(LEVEL_PARAM, currentLevel);

                                ((Activity) context).startActivityForResult(intent, UPDATE_REQUEST_CODE);
                            }
                        });
                    } else {
                        holder.levelNumberTextView.setBackgroundResource(R.drawable.campaign_item_disabled_circle);
                    }
                }
            }
        }

        @Override
        public int getItemCount() {
            if (levels == null) {
                return 0;
            }

            return levels.size();
        }
    }

}
