package com.greenkey.librain.campaign;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.greenkey.librain.MyApplication;
import com.greenkey.librain.PremiumHelper;
import com.greenkey.librain.R;
import com.greenkey.librain.dao.LevelDao;
import com.greenkey.librain.level.Level;
import com.greenkey.librain.level.LevelsPage;
import com.greenkey.librain.view.viewpagerindicator.ViewPagerIndicator;

import org.solovyev.android.checkout.ActivityCheckout;
import org.solovyev.android.checkout.Checkout;
import org.solovyev.android.checkout.Purchase;

import java.util.List;

import javax.annotation.Nonnull;

public class CampaignMenuActivity extends AppCompatActivity implements
        CampaignMenuLevelsPageFragment.LevelOnClickListener {

    public static final String LEVEL_PARAM = "level";

    private LevelDao levelDao;

    private TextView startCountTextView;
    private TextView enabledLevelCountTextView;

    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;

    private ActivityCheckout checkout;

    private ViewPagerIndicator viewPagerIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.campaign_activity);

        checkout = Checkout.forActivity(this, MyApplication.getInstance().getBilling());
        checkout.start();

        levelDao = LevelDao.getInstance(CampaignMenuActivity.this);

        final ImageView premiumImageView = (ImageView) findViewById(R.id.premium_image_view);
        premiumImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PremiumHelper.PremiumDialog premiumDialog = new PremiumHelper.PremiumDialog(CampaignMenuActivity.this, checkout);
                premiumDialog.setPurchaseListener(new PremiumHelper.PremiumDialogPurchaseListener() {
                    @Override
                    public void onSuccess(@Nonnull Purchase result) {
                        super.onSuccess(result);

                        premiumImageView.setVisibility(View.INVISIBLE);
                        Toast.makeText(CampaignMenuActivity.this, R.string.premium_success_message, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(int response, @Nonnull Exception e) {
                        super.onError(response, e);

                        Toast.makeText(CampaignMenuActivity.this, R.string.premium_error_message, Toast.LENGTH_LONG).show();
                    }
                });
                premiumDialog.show();
            }
        });

        if (PremiumHelper.isPremiumUser()) {
            premiumImageView.setVisibility(View.INVISIBLE);
        }

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

    @Override
    public void onLevelClick(Level level) {
        if (level.isEnabled()) {
            final Intent intent = new Intent(CampaignMenuActivity.this, CampaignGameActivity.class);
            intent.putExtra(LEVEL_PARAM, level);

            if (level.isPremium()) {
                if (PremiumHelper.isPremiumUser()) {
                    startActivityForResult(intent, UPDATE_REQUEST_CODE);
                } else {
                    PremiumHelper.PremiumDialog premiumDialog = new PremiumHelper.PremiumDialog(CampaignMenuActivity.this, checkout);
                    premiumDialog.setPurchaseListener(new PremiumHelper.PremiumDialogPurchaseListener() {
                        @Override
                        public void onSuccess(@Nonnull Purchase result) {
                            super.onSuccess(result);

                            startActivityForResult(intent, UPDATE_REQUEST_CODE);
                            Toast.makeText(CampaignMenuActivity.this, R.string.premium_success_message, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onError(int response, @Nonnull Exception e) {
                            super.onError(response, e);

                            Toast.makeText(CampaignMenuActivity.this, R.string.premium_error_message, Toast.LENGTH_LONG).show();
                        }
                    });
                    premiumDialog.show();
                }
            } else {
                startActivityForResult(intent, UPDATE_REQUEST_CODE);
            }
        }
    }

    private static final int UPDATE_REQUEST_CODE = 100;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        checkout.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPDATE_REQUEST_CODE) {
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
            return CampaignMenuLevelsPageFragment.newInstance(levelsPages.get(position).getLevels());
        }

        @Override
        public int getCount() {
            if (levelsPages == null) {
                return 0;
            }

            return levelsPages.size();
        }

    }



    @Override
    protected void onDestroy() {
        checkout.stop();
        super.onDestroy();
    }

    /*
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

        public class LevelViewHolder extends RecyclerView.ViewHolder {

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
                    if (PremiumHelper.isPremiumUser()) {

                    } else {
                        holder.levelNumberTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PremiumHelper.PremiumDialog premiumDialog = new PremiumHelper.PremiumDialog(CampaignMenuActivity.this, checkout);
                            }
                        });
                    }




                    if (currentLevel.isEnabled()) {


                        holder.levelNumberTextView.setBackgroundResource(R.drawable.campaign_item_premium_circle);
                        holder.levelNumberTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, CampaignGameActivity.class);
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
                                Intent intent = new Intent(context, CampaignGameActivity.class);
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
*/
}
