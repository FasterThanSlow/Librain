package com.greenkey.librain.view.distributorview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.greenkey.librain.R;
import com.greenkey.librain.entity.ItemType;
import com.greenkey.librain.entity.Rule;
import com.greenkey.librain.view.PixelConverter;

import java.util.ArrayList;
import java.util.List;

import static com.greenkey.librain.view.PixelConverter.dpToPx;

/**
 * Created by Alexander on 10.02.2017.
 */

public class DistributorView extends LinearLayout {

    private AnimatorSet showAnimationSet;

    private static final int SHOW_ANIMATION_DURATION = 100;

    @Override
    public void setX(float x) {
        super.setX(x);
        showAnimationSet.start();
    }

    private static final int TRIANGLE_SIZE_DP = 20;
    private int triangleViewSizePx;

    private static final int ITEMS_LAYOUT_PADDING_DP = 4;

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

    private float itemSize = 60;

    public DistributorView(Context context) {
        super(context);
        this.context = context;
        init(null, 0);
    }

    public DistributorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs, 0);
    }

    public DistributorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        this.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        this.setOrientation(VERTICAL);

        final TypedArray typedArray = context.obtainStyledAttributes(
                attrs, R.styleable.DistributorView, defStyle, 0);

        itemSize = typedArray.getDimension(R.styleable.DistributorView_item_size, itemSize);

        typedArray.recycle();

        int itemsPadding =  PixelConverter.dpToPx(context, ITEMS_LAYOUT_PADDING_DP);

        itemsLayout = new LinearLayout(context);
        itemsLayout.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        itemsLayout.setOrientation(HORIZONTAL);
        itemsLayout.setBackgroundResource(R.drawable.distributor_background);
        itemsLayout.setPadding(itemsPadding, itemsPadding, itemsPadding, itemsPadding);
        this.addView(itemsLayout);

        triangleViewSizePx =  PixelConverter.dpToPx(context, TRIANGLE_SIZE_DP);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(triangleViewSizePx, triangleViewSizePx);
        layoutParams.setMargins(0, PixelConverter.dpToPx(context, -1), 0, 0);
        triangleImageView = new ImageView(context);
        triangleImageView.setLayoutParams(layoutParams);
        triangleImageView.setImageResource(R.drawable.distributor_view_traingle);
        this.addView(triangleImageView);

        items = new ArrayList<>();


        ObjectAnimator showAnimation1 = ObjectAnimator.ofFloat(this, View.SCALE_Y, 0f, 1f);
        ObjectAnimator showAnimation2 = ObjectAnimator.ofFloat(this, View.SCALE_X, 0f, 1f);

        showAnimation1.setInterpolator(new LinearInterpolator());
        showAnimation2.setInterpolator(new LinearInterpolator());

        showAnimationSet = new AnimatorSet();
        showAnimationSet.setDuration(SHOW_ANIMATION_DURATION);
        showAnimationSet.play(showAnimation1).with(showAnimation2);
    }

    public int getTriangleViewSizePx() {
        return triangleViewSizePx;
    }

    public void setTriangleOffset(int offsetX) {
        triangleImageView.setX(offsetX); //check
    }

    public void setItems(@NonNull Rule[] rules) {
        this.removeItems();

        for (Rule rule : rules) {
            DistributorItemView distributorItemView = new DistributorItemView(rule, itemSize);
            distributorItemView.setOnTouchListener(itemsOnTouchListener);

            items.add(distributorItemView);

            itemsLayout.addView(distributorItemView);
        }
    }

    public void removeItems() {
        items.clear();
        itemsLayout.removeAllViews();
    }

    public void addResource(ItemType itemType) {
        final DistributorItemView itemView = getItem(itemType);
        if (itemView != null) {
            itemView.addImageView();
        } else {
            DistributorItemView distributorItemView = new DistributorItemView(new Rule(1, itemType), itemSize);
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

    private DistributorItemView getItem(ItemType itemType) {
        for (DistributorItemView itemView : items) {
            if (itemView.getItemType() == itemType)
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
        private ItemType itemType;

        private ImageView imageView;
        private TextView itemsCountTextView;

        private static final int ITEM_MARGIN_DP = 4;
        private static final int ITEM_PADDING_DP = 2;

        DistributorItemView(Rule rule, float size) {
            super(context);
            init(rule, (int) (size + 0.5f));
        }

        private void init(Rule rule, int size) {
            int itemMarginPx = dpToPx(context, ITEM_MARGIN_DP);
            int itemPaddingPx = dpToPx(context, ITEM_PADDING_DP);

            LinearLayout.LayoutParams itemLayoutParams = new LinearLayout.LayoutParams(size, size);
            itemLayoutParams.setMargins(itemMarginPx, itemMarginPx, itemMarginPx, itemMarginPx);

            this.setLayoutParams(itemLayoutParams);
            this.setPadding(itemPaddingPx, itemPaddingPx, itemPaddingPx, itemPaddingPx);
            //this.setBackgroundResource(R.drawable.destributer_item_background);

            this.itemsCount = rule.getItemsCount();
            this.itemType = rule.getItemType();

            createImageView(context);

            if (itemType != ItemType.NONE)
                imageView.setImageResource(itemType.getEnabledItemResourceId());

            createItemsCountTextView(context);
            updateCountItemsTextView(itemsCount);
        }

        public boolean allResourcesUsed() {
            return itemsCount == 0;
        }

        public ItemType getItemType() {
            return itemType;
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
                imageView.setImageResource(itemType.getEnabledItemResourceId());
            }
        }

        public void removeImageView() {
            itemsCount--;
            updateCountItemsTextView(itemsCount);

            if (itemsCount == 0) {
                items.remove(this);
                itemsLayout.removeView(this);
            }
        }

        private static final int TEXT_PADDING_DP = 5;
        private static final int TEXT_SIZE_SP = 12;

        private final FrameLayout.LayoutParams itemsCountTextViewLayoutParams = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM | Gravity.END);

        private void createItemsCountTextView(Context context) {
            int paddingPx = dpToPx(context, TEXT_PADDING_DP);

            itemsCountTextView = new TextView(context);

            itemsCountTextView.setLayoutParams(itemsCountTextViewLayoutParams);
            itemsCountTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE_SP);
            itemsCountTextView.setTextColor(Color.WHITE);

            itemsCountTextView.setPadding(paddingPx, 0, paddingPx, 0);
            itemsCountTextView.setBackgroundResource(R.drawable.distributor_item_count_shape);

            this.addView(itemsCountTextView);
        }

        private void updateCountItemsTextView(int itemsCount) {
            itemsCountTextView.setText(String.valueOf(itemsCount));
        }
    }
}
