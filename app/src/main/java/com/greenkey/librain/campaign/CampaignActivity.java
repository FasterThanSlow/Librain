package com.greenkey.librain.campaign;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.greenkey.librain.MainActivity;
import com.greenkey.librain.R;
import com.greenkey.librain.RatingBar;
import com.greenkey.librain.LevelDao;

import java.util.List;

public class CampaignActivity extends AppCompatActivity {

    private LevelDao levelDao;

    private static final int COLUMNS_COUNT = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign);

        levelDao = LevelDao.getInstance(CampaignActivity.this);

        final CampaignGridViewAdapter adapter = new CampaignGridViewAdapter(CampaignActivity.this, levelDao.getLevels());

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(CampaignActivity.this, COLUMNS_COUNT));
        recyclerView.setAdapter(adapter);
    }

    private static class CampaignGridViewAdapter extends RecyclerView.Adapter<CampaignGridViewAdapter.CampaignViewHolder> {

        public static class CampaignViewHolder extends RecyclerView.ViewHolder {

            public View backgroundView;
            public TextView levelNumberTextView;
            public RatingBar ratingBar;

            public CampaignViewHolder(View itemView) {
                super(itemView);

                backgroundView = itemView.findViewById(R.id.campaign_item_background_view);
                levelNumberTextView = (TextView) itemView.findViewById(R.id.campaign_item_level_number);
                ratingBar = (RatingBar) itemView.findViewById(R.id.campaign_item_rating_bar);
            }
        }

        private Context context;
        private List<Level> levels;

        public CampaignGridViewAdapter(Context context, @NonNull List<Level> levels) {
            this.context = context;
            this.levels = levels;
        }

        @Override
        public CampaignViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CampaignViewHolder(LayoutInflater.from(context).inflate(R.layout.campaign_item, parent, false));
        }

        @Override
        public void onBindViewHolder(CampaignViewHolder holder, int position) {
            final Level currentLevel = levels.get(position);

            if (currentLevel != null) {
                holder.levelNumberTextView.setText(String.valueOf(currentLevel.getLevelId()));
                holder.ratingBar.setProgress(currentLevel.getRecord());

                if (currentLevel.isEnabled()) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, MainActivity.class);

                            intent.putExtra("level", currentLevel);

                            context.startActivity(intent);
                        }
                    });
                } else {
                    holder.itemView.setClickable(false);

                    holder.backgroundView.setBackgroundColor(Color.CYAN);
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
