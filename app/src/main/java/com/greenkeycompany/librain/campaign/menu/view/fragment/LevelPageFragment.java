package com.greenkeycompany.librain.campaign.menu.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.greenkeycompany.librain.R;
import com.greenkeycompany.librain.level.Level;
import com.greenkeycompany.librain.app.view.ratingbar.RatingBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Alexander on 08.05.2017.
 */

public class LevelPageFragment extends Fragment {

    public LevelPageFragment() {
    }

    public static LevelPageFragment newInstance(ArrayList<Level> levelArrayList) {
        LevelPageFragment fragment = new LevelPageFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(LEVEL_LIST_PARAM, levelArrayList);
        fragment.setArguments(args);
        return fragment;
    }

    private static final String LEVEL_LIST_PARAM = "level_list";
    private List<Level> levelList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            levelList = getArguments().getParcelableArrayList(LEVEL_LIST_PARAM);
        }
    }

    private static final int COLUMNS_COUNT = 4;

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.campaign_level_page_fragment, container, false);

        unbinder = ButterKnife.bind(this, rootView);

        LevelsAdapter adapter = new LevelsAdapter(levelList, new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                fragmentListener.onLevelPageFragmentLevelClick(levelList.get(position));
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), COLUMNS_COUNT));
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    interface OnItemClickListener {
        void onItemClick(int position);
    }
    class LevelsAdapter extends RecyclerView.Adapter<LevelsAdapter.LevelViewHolder> {

        class LevelViewHolder extends RecyclerView.ViewHolder
                implements View.OnClickListener {

            @BindView(R.id.item_level_number) TextView levelNumberTextView;
            @BindView(R.id.item_rating_bar) RatingBar ratingBar;

            private OnItemClickListener listener;
            LevelViewHolder(View itemView, OnItemClickListener listener) {
                super(itemView);
                ButterKnife.bind(this, itemView);

                this.listener = listener;
                this.itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(getAdapterPosition());
                }
            }
        }

        private final List<Level> levels;
        private OnItemClickListener itemClickListener;
        LevelsAdapter(@NonNull List<Level> levels, OnItemClickListener itemClickListener) {
            this.levels = levels;
            this.itemClickListener = itemClickListener;
        }

        @Override
        public LevelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.campaign_level_item, parent, false);

            return new LevelViewHolder(view, itemClickListener);
        }

        @Override
        public void onBindViewHolder(LevelViewHolder holder, int position) {
            final Level level = levels.get(position);

            holder.ratingBar.setProgress(level.getRecord());
            holder.levelNumberTextView.setText(String.valueOf(level.getId()));

            if (level.isPremium()) {
                holder.levelNumberTextView.setBackgroundResource(level.isEnabled() ?
                        R.drawable.campaign_item_premium_circle :
                        R.drawable.campaign_item_premium_disabled_circle);
            } else {
                holder.levelNumberTextView.setBackgroundResource(level.isEnabled() ?
                        R.drawable.campaign_item_circle :
                        R.drawable.campaign_item_disabled_circle);
            }
        }

        @Override
        public int getItemCount() {
           return levels.size();
        }
    }


    public interface LevelPageFragmentListener {
        void onLevelPageFragmentLevelClick(Level level);
    }
    private LevelPageFragmentListener fragmentListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LevelPageFragmentListener) {
            fragmentListener = (LevelPageFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement LevelPageFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentListener = null;
    }
}

