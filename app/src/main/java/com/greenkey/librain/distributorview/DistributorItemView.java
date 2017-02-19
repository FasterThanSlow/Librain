package com.greenkey.librain.distributorview;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.greenkey.librain.PixelConverter;
import com.greenkey.librain.R;
import com.greenkey.librain.ResourceType;

/**
 * Created by Alexander on 10.02.2017.
 */

public class DistributorItemView extends FrameLayout {

    private int itemsCount;
    private ResourceType resourceType;

    private ImageView imageView;
    private TextView itemsCountTextView;

    private OnTouchListener imageViewOnTouchListener;
    public void setImageViewOnTouchListener(OnTouchListener listener) {
        imageViewOnTouchListener = listener;
        if (imageView != null) {
            imageView.setOnTouchListener(listener);
        }
    }

    public DistributorItemView(Context context, int itemsCount, ResourceType resourceType) {
        super(context);

        this.itemsCount = itemsCount;
        this.resourceType = resourceType;

        createImageView(context);
        setImageResource(resourceType, itemsCount);

        createItemsCountTextView(context);
        updateCountItemsTextView(itemsCount);
    }

    public boolean allResourcesUsed() {
        return itemsCount == 0;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    private static final FrameLayout.LayoutParams imageViewLayoutParams = new FrameLayout.LayoutParams
            (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

    private void createImageView(Context context) {
        imageView = new ImageView(context);
        imageView.setLayoutParams(imageViewLayoutParams);

        this.addView(imageView);
    }

    private void setImageResource(ResourceType resourceType, int itemsCount) {
        if (itemsCount > 0) {
            imageView.setImageResource(resourceType.getEnabledItemResourceId());
        } else {
            imageView.setImageResource(resourceType.getDisabledItemResourceId());
        }
    }

    public void addImageView() {
        itemsCount++;
        updateCountItemsTextView(itemsCount);

        if (itemsCount == 1) {
            imageView.setOnTouchListener(imageViewOnTouchListener);
            imageView.setImageResource(resourceType.getEnabledItemResourceId());
        }
    }

    public void removeImageView() {
        itemsCount--;
        updateCountItemsTextView(itemsCount);

        if (itemsCount == 0) {
            imageView.setOnTouchListener(null);
            imageView.setImageResource(resourceType.getDisabledItemResourceId());
        }
    }

    private static final int TEXT_PADDING_DP = 5;

    private static final int TEXT_SIZE_SP = 12;
    private static final FrameLayout.LayoutParams itemsCountTextViewLayoutParams = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM | Gravity.END);

    private void createItemsCountTextView(Context context) {
        int paddingPx = PixelConverter.dpToPx(context, TEXT_PADDING_DP);

        itemsCountTextView = new TextView(context);

        itemsCountTextView.setLayoutParams(itemsCountTextViewLayoutParams);
        itemsCountTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE_SP);
        itemsCountTextView.setTextColor(Color.WHITE); //

        itemsCountTextView.setPadding(paddingPx, 0, paddingPx, 0);
        itemsCountTextView.setBackgroundResource(R.drawable.distributor_item_count_background); ////

        this.addView(itemsCountTextView);
    }

    private void updateCountItemsTextView(int itemsCount) {
        itemsCountTextView.setText(String.valueOf(itemsCount));
    }
}