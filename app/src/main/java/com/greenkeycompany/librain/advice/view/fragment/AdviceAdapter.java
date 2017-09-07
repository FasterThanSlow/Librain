package com.greenkeycompany.librain.advice.view.fragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.greenkeycompany.librain.R;
import com.greenkeycompany.librain.advice.model.Advice;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tert0 on 05.09.2017.
 */

public class AdviceAdapter extends RecyclerView.Adapter<AdviceAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.item_favorite_image_view)
        ImageView favoriteImageView;
        @BindView(R.id.item_title_text_view)
        TextView titleTextView;
        @BindView(R.id.item_message_text_view) TextView messageTextView;

        private OnItemClickListener itemClickListener;

        ViewHolder(View itemView, OnItemClickListener itemClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.itemClickListener = itemClickListener;
            this.favoriteImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }

    private List<Advice> items = new ArrayList<>();
    private OnItemClickListener itemClickListener;

    public AdviceAdapter(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void addItem(@NonNull Advice advice) {
        this.items.add(advice);
        this.notifyDataSetChanged();
    }

    public void removeItem(int position) {
        this.items.remove(position);
        this.notifyItemRemoved(position);
    }

    public void setItems(@NonNull List<Advice> items) {
        this.items.clear();
        this.items.addAll(items);
        this.notifyDataSetChanged();
    }

    @Override
    public AdviceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.advice_item, parent, false);

        return new ViewHolder(itemView, itemClickListener);
    }

    @Override
    public void onBindViewHolder(final AdviceAdapter.ViewHolder holder, int position) {
        Advice advice = items.get(position);

        holder.titleTextView.setText(advice.getTitle());
        holder.messageTextView.setText(advice.getMessage());
        holder.favoriteImageView.setImageResource(advice.isFavorite() ?
                R.drawable.advices_item_favorite_icon_selected :
                R.drawable.advices_item_favorite_icon);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}