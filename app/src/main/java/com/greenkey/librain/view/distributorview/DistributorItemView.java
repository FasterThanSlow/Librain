package com.greenkey.librain.view.distributorview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.greenkey.librain.view.PixelConverter;
import com.greenkey.librain.R;
import com.greenkey.librain.entity.ResourceType;
import com.greenkey.librain.entity.Rule;

/**
 * Created by Alexander on 10.02.2017.
 */

public class DistributorItemView extends FrameLayout {

    private int itemsCount;
    private ResourceType resourceType;

    private ImageView imageView;
    private TextView itemsCountTextView;

    private static final int ITEM_SIZE_DP = 60;
    private static final int ITEM_MARGIN_DP = 4;
    private static final int ITEM_PADDING_DP = 2;

    private final Context context;

    public DistributorItemView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public DistributorItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public DistributorItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        int itemSizePx = PixelConverter.dpToPx(context, ITEM_SIZE_DP);
        int itemMarginPx = PixelConverter.dpToPx(context, ITEM_MARGIN_DP);
        int itemPaddingPx = PixelConverter.dpToPx(context, ITEM_PADDING_DP);

        LinearLayout.LayoutParams itemLayoutParams = new LinearLayout.LayoutParams(itemSizePx, itemSizePx);
        itemLayoutParams.setMargins(itemMarginPx, itemMarginPx, itemMarginPx, itemMarginPx);

        this.setLayoutParams(itemLayoutParams);
        this.setPadding(itemPaddingPx, itemPaddingPx, itemPaddingPx, itemPaddingPx);
        this.setBackgroundResource(R.drawable.destributer_item_background);
    }

    public void setRule(Rule rule) {
        if (rule != null) {
            this.itemsCount = rule.getItemsCount();
            this.resourceType = rule.getResourceType();

            createImageView(context);

            if (resourceType != ResourceType.NONE)
                setImageResource(resourceType, itemsCount);

            createItemsCountTextView(context);
            updateCountItemsTextView(itemsCount);
        }
    }

    private boolean isItemSelected;
    public boolean isItemSelected() {
        return isItemSelected;
    }

    public void select() {
        this.isItemSelected = true;
        this.setBackgroundColor(Color.CYAN);
    }

    public void deselect() {
        this.isItemSelected = false;
        this.setBackgroundResource(R.drawable.normal_shape);
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
        itemsCountTextView.setTextColor(Color.WHITE);

        itemsCountTextView.setPadding(paddingPx, 0, paddingPx, 0);
        itemsCountTextView.setBackgroundResource(R.drawable.distributor_items_count_background);

        this.addView(itemsCountTextView);
    }

    private void updateCountItemsTextView(int itemsCount) {
        itemsCountTextView.setText(String.valueOf(itemsCount));
    }
}