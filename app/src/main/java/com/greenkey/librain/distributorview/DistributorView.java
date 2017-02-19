package com.greenkey.librain.distributorview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.greenkey.librain.PixelConverter;
import com.greenkey.librain.R;
import com.greenkey.librain.ResourceType;
import com.greenkey.librain.Rule;

/**
 * Created by Alexander on 10.02.2017.
 */

public class DistributorView extends LinearLayout {

    public void setItemsImageViewOnTouchListener(OnTouchListener itemOnTouchListener) {
        for (int i = 0; i < itemsCount; i++) {
            if (items[i] != null)
                items[i].setImageViewOnTouchListener(itemOnTouchListener);
        }
    }

    private int itemsCount;
    private DistributorItemView[] items;

    private Context context;

    public DistributorView(Context context) {
        super(context);
        this.context = context;
    }

    public DistributorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public DistributorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    private static final int ITEM_SIZE_DP = 50;
    private static final int ITEM_MARGIN_DP = 8;

    public void setItems(Rule[] rules) {
        this.removeItems();

        if (rules == null) {
            itemsCount = 0;
        } else {
            itemsCount = rules.length;
        }

        int itemSizePx = PixelConverter.dpToPx(context, ITEM_SIZE_DP);
        int itemMarginPx = PixelConverter.dpToPx(context, ITEM_MARGIN_DP);

        LinearLayout.LayoutParams itemLayoutParams = new LinearLayout.LayoutParams(itemSizePx, itemSizePx);
        itemLayoutParams.setMargins(itemMarginPx, itemMarginPx, itemMarginPx, itemMarginPx);

        items = new DistributorItemView[itemsCount];
        for (int i = 0; i < itemsCount; i++) {
            Rule rule = rules[i];
            if (rule != null) {
                DistributorItemView distributorItemView = new DistributorItemView(context, rule.getItemsCount(), rule.getResourceType());

                distributorItemView.setLayoutParams(itemLayoutParams);
                distributorItemView.setBackgroundResource(R.drawable.normal_shape);

                items[i] = distributorItemView;

                this.addView(distributorItemView);
            }
        }
    }

    public void removeItems() {
        this.itemsCount = 0;
        this.items = null;
        this.removeAllViews();
    }

    public DistributorItemView findItem(ResourceType resourceType) {
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
