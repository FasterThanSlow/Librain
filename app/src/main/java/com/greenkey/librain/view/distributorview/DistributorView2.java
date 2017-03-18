package com.greenkey.librain.view.distributorview;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.greenkey.librain.R;
import com.greenkey.librain.entity.ResourceType;
import com.greenkey.librain.entity.Rule;
import com.greenkey.librain.view.PixelConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 10.02.2017.
 */

public class DistributorView2 extends LinearLayout {

    private OnTouchListener itemsOnTouchListener;
    public void setItemsOnTouchListener(OnTouchListener listener) {
        this.itemsOnTouchListener = listener;
        for (DistributorItemView itemView : items) {
            itemView.setOnTouchListener(listener);
        }
    }

    private final Context context;
    private List<DistributorItemView> items;

    private LinearLayout itemsLayout;
    private ImageView triangleImageView;

    public DistributorView2(Context context) {
        super(context);
        this.context = context;
        init(null, 0);
    }

    public DistributorView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs, 0);
    }

    public DistributorView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        this.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        this.setOrientation(VERTICAL);

        itemsLayout = new LinearLayout(context);
        itemsLayout.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        itemsLayout.setOrientation(HORIZONTAL);
        itemsLayout.setBackgroundResource(R.drawable.distributor_background);
        this.addView(itemsLayout);

        triangleImageView = new ImageView(context);
        triangleImageView.setLayoutParams(new FrameLayout.LayoutParams(40, 40));
        triangleImageView.setImageResource(R.drawable.triangle);
        this.addView(triangleImageView);

        items = new ArrayList<>();
    }

    public void setTriangleOffset(int offsetX) {
        triangleImageView.setX(offsetX); //check
    }

    public void setItems(@NonNull Rule[] rules) {
        this.removeItems();

        for (Rule rule : rules) {
            DistributorItemView distributorItemView = new DistributorItemView(rule);
            distributorItemView.setOnTouchListener(itemsOnTouchListener);

            items.add(distributorItemView);

            itemsLayout.addView(distributorItemView);
        }
    }

    public void removeItems() {
        items.clear();
        itemsLayout.removeAllViews();
    }

    public void addResource(ResourceType resourceType) {
        final DistributorItemView itemView = getItem(resourceType);
        if (itemView != null) {
            itemView.addImageView();
        } else {
            DistributorItemView distributorItemView = new DistributorItemView(new Rule(1, resourceType));
            distributorItemView.setOnTouchListener(itemsOnTouchListener);

            items.add(distributorItemView);

            itemsLayout.addView(distributorItemView);
        }
    }

    public DistributorItemView getItem(int index) {
        if (index > items.size()) {
            return null;
        }

        return items.get(index);
    }

    private DistributorItemView getItem(ResourceType resourceType) {
        for (DistributorItemView itemView : items) {
            if (itemView.getResourceType() == resourceType)
                return itemView;
        }

        return null;
    }

    public int getItemsCount() {
        return items.size();
    }

    public int getUnusedResourceTypesCount() {
        int unusedTypesCount = 0;
        for (DistributorItemView itemView : items) {
            if ( ! itemView.allResourcesUsed())
                unusedTypesCount++;
        }

        return unusedTypesCount;
    }

    /*
    public boolean hasOnlyOneUnusedResourceType() {
        return getUnusedResourceTypesCount() == 1;
    }
    */

    public boolean allItemsResourcesUsed() {
        for (DistributorItemView itemView : items) {
            if ( ! itemView.allResourcesUsed()) {
                return false;
            }
        }

        return true;
    }


    //ITEM CLASS
    public class DistributorItemView extends FrameLayout {

        private int itemsCount;
        private ResourceType resourceType;

        private ImageView imageView;
        private TextView itemsCountTextView;

        private static final int ITEM_SIZE_DP = 60;
        private static final int ITEM_MARGIN_DP = 4;
        private static final int ITEM_PADDING_DP = 2;

        //private final Context context;

        DistributorItemView(Rule rule) {
            super(context);
            init(rule);
        }

        private void init(Rule rule) {
            int itemSizePx = PixelConverter.dpToPx(context, ITEM_SIZE_DP);
            int itemMarginPx = PixelConverter.dpToPx(context, ITEM_MARGIN_DP);
            int itemPaddingPx = PixelConverter.dpToPx(context, ITEM_PADDING_DP);

            LinearLayout.LayoutParams itemLayoutParams = new LinearLayout.LayoutParams(itemSizePx, itemSizePx);
            itemLayoutParams.setMargins(itemMarginPx, itemMarginPx, itemMarginPx, itemMarginPx);

            this.setLayoutParams(itemLayoutParams);
            this.setPadding(itemPaddingPx, itemPaddingPx, itemPaddingPx, itemPaddingPx);
            this.setBackgroundResource(R.drawable.destributer_item_background);

            this.itemsCount = rule.getItemsCount();
            this.resourceType = rule.getResourceType();

            createImageView(context);

            if (resourceType != ResourceType.NONE)
                imageView.setImageResource(resourceType.getEnabledItemResourceId());

            createItemsCountTextView(context);
            updateCountItemsTextView(itemsCount);
        }

        public boolean allResourcesUsed() {
            return itemsCount == 0;
        }

        public ResourceType getResourceType() {
            return resourceType;
        }

        private final FrameLayout.LayoutParams imageViewLayoutParams = new FrameLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        private void createImageView(Context context) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(imageViewLayoutParams);

            this.addView(imageView);
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
                items.remove(this);
                DistributorView2.this.removeView(this);
            }
        }

        private static final int TEXT_PADDING_DP = 5;
        private static final int TEXT_SIZE_SP = 12;

        private final FrameLayout.LayoutParams itemsCountTextViewLayoutParams = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM | Gravity.END);

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
}
