package com.greenkeycompany.librain.advice.view;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.greenkeycompany.librain.R;
import com.greenkeycompany.librain.advice.model.Advice;
import com.greenkeycompany.librain.advice.presenter.AdvicePresenter;
import com.greenkeycompany.librain.advice.presenter.IAdvicePresenter;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import java.util.List;

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

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advice_activity);
        ButterKnife.bind(this);

        toolbar.setTitle(R.string.advices);
        toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.action_bar_back_icon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presenter.init();
    }

    @Override
    public void setAdvises(@NonNull List<Advice> adviceList) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new AdviceAdapter(adviceList));
    }

    class AdviceAdapter extends RecyclerView.Adapter<AdviceAdapter.ViewHolder> {

        class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.item_title_text_view) TextView titleTextView;
            @BindView(R.id.item_message_text_view) TextView messageTextView;

            ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        private List<Advice> items;

        AdviceAdapter(@NonNull List<Advice> items) {
            this.items = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.advice_item, parent, false);

            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Advice advice = items.get(position);

            holder.titleTextView.setText(advice.getTitle());
            holder.messageTextView.setText(advice.getMessage());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }
}
