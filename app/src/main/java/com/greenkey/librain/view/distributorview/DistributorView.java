package com.greenkey.librain.view.distributorview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.greenkey.librain.R;
import com.greenkey.librain.entity.ResourceType;
import com.greenkey.librain.entity.Rule;

/**
 * Created by Alexander on 10.02.2017.
 */

public class DistributorView extends LinearLayout {

    private static final int ITEMS_COUNT_DEFAULT_VALUE = 3;

    private OnTouchListener itemsOnTouchListener;
    public void setItemsOnTouchListener(OnTouchListener listener) {

        this.itemsOnTouchListener = listener;
        for (int i = 0; i < itemsCount; i++) {
            if (items[i] != null)
                items[i].setOnTouchListener(listener);
        }
    }

    private int itemsCount;
    private DistributorItemView[] items;

    private Context context;

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
        this.setOrientation(HORIZONTAL);

        final TypedArray typedArray = context.obtainStyledAttributes(
                attrs, R.styleable.DistributorView, defStyle, 0);

        itemsCount = typedArray.getInteger(R.styleable.DistributorView_itemsCount, ITEMS_COUNT_DEFAULT_VALUE);

        typedArray.recycle();

        createItems(itemsCount);
    }

    public void createItems(int itemsCount) {
        this.removeItems();

        if (itemsCount < 0) {
            this.itemsCount = 0;
        } else {
            this.itemsCount = itemsCount;
        }

        items = new DistributorItemView[this.itemsCount];
        for (int i = 0; i < itemsCount; i++) {
            DistributorItemView distributorItemView = new DistributorItemView(context);
            distributorItemView.setOnTouchListener(itemsOnTouchListener);

            items[i] = distributorItemView;

            this.addView(distributorItemView);
        }
    }

    public void setRules(Rule[] rules) {
        if (rules != null) {
            for (int i = 0; i < itemsCount; i++) {
                items[i].setRule(rules[i]);
            }
        }
    }

    public void createItems(Rule[] rules) {
        this.removeItems();

        if (rules == null) {
            itemsCount = 0;
        } else {
            itemsCount = rules.length;
        }

        items = new DistributorItemView[itemsCount];
        for (int i = 0; i < itemsCount; i++) {
            DistributorItemView distributorItemView = new DistributorItemView(context);
            distributorItemView.setRule(rules[i]);
            distributorItemView.setOnTouchListener(itemsOnTouchListener);

            items[i] = distributorItemView;

            this.addView(distributorItemView);
        }
    }

    public void removeItems() {
        this.itemsCount = 0;
        this.items = null;
        this.removeAllViews();
    }

    public void addResource(ResourceType resourceType) {
        final DistributorItemView itemView = findItem(resourceType);
        if (itemView != null) {
            itemView.addImageView();
        }
    }


    private DistributorItemView findItem(ResourceType resourceType) {
        for (int i = 0; i < itemsCount; i++) {
            if (items[i].getResourceType() == resourceType)
                return items[i];
        }

        return null;
    }

    public boolean allItemsResourcesUsed() {
        for (int i = 0; i < itemsCount; i++) {
            if ( ! items[i].allResourcesUsed()) {
                return false;
            }
        }

        return true;
    }
}
