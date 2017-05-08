package com.greenkey.librain.campaign;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.greenkey.librain.R;
import com.greenkey.librain.level.Level;
import com.greenkey.librain.view.ratingbar.RatingBar;

import java.util.ArrayList;

/**
 * Created by Alexander on 08.05.2017.
 */

public class CampaignMenuLevelsPageFragment extends Fragment {

    private static final int COLUMNS_COUNT = 4;
    private static final String LEVELS_PARAM = "levels";

    private LevelOnClickListener listener;

    public CampaignMenuLevelsPageFragment() {
    }

    public static CampaignMenuLevelsPageFragment newInstance(ArrayList<Level> levels) {
        CampaignMenuLevelsPageFragment fragment = new CampaignMenuLevelsPageFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(LEVELS_PARAM, levels);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            levels = getArguments().getParcelableArrayList(LEVELS_PARAM);
        }
    }

    private LevelsRecycleViewAdapter adapter;
    private RecyclerView recyclerView;

    private ArrayList<Level> levels;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.campaign_fragment, container, false);

        if (levels != null) {
            adapter = new LevelsRecycleViewAdapter(getContext(), levels);

            recyclerView = (RecyclerView) rootView.findViewById(R.id.campaign_fragment_recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), COLUMNS_COUNT));
            recyclerView.setAdapter(adapter);
        }

        return rootView;
    }

    private class LevelsRecycleViewAdapter extends RecyclerView.Adapter<LevelsRecycleViewAdapter.LevelViewHolder> {

        public class LevelViewHolder extends RecyclerView.ViewHolder {

            public TextView levelNumberTextView;
            public RatingBar ratingBar;

            public LevelViewHolder(View itemView) {
                super(itemView);
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
        public LevelsRecycleViewAdapter.LevelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new LevelsRecycleViewAdapter.LevelViewHolder(LayoutInflater.from(context).inflate(R.layout.campaign_level_item, parent, false));
        }

        @Override
        public void onBindViewHolder(LevelsRecycleViewAdapter.LevelViewHolder holder, int position) {
            final Level currentLevel = levels.get(position);

            if (currentLevel != null) {
                holder.levelNumberTextView.setText(String.valueOf(currentLevel.getLevelId()));
                holder.ratingBar.setProgress(currentLevel.getRecord());

                holder.levelNumberTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onLevelClick(currentLevel);
                        }
                    }
                });

                if (currentLevel.isPremium()) {
                   if (currentLevel.isEnabled()) {
                        holder.levelNumberTextView.setBackgroundResource(R.drawable.campaign_item_premium_circle);
                        /*
                       holder.levelNumberTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, CampaignGameActivity.class);
                                intent.putExtra(LEVEL_PARAM, currentLevel);

                                ((Activity) context).startActivityForResult(intent, UPDATE_REQUEST_CODE);
                            }
                        });*/
                    } else {
                        holder.levelNumberTextView.setBackgroundResource(R.drawable.campaign_item_premium_disabled_circle);
                    }
                } else {
                    if (currentLevel.isEnabled()) {
                        holder.levelNumberTextView.setBackgroundResource(R.drawable.campaign_item_circle);
                        /*holder.levelNumberTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, CampaignGameActivity.class);
                                intent.putExtra(LEVEL_PARAM, currentLevel);

                                ((Activity) context).startActivityForResult(intent, UPDATE_REQUEST_CODE);
                            }
                        });*/
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LevelOnClickListener) {
            listener = (LevelOnClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement LevelOnClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface LevelOnClickListener {
        void onLevelClick(Level level);
    }
}

