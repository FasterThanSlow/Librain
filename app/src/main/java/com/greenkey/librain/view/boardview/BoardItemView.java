package com.greenkey.librain.view.boardview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.greenkey.librain.view.PixelConverter;
import com.greenkey.librain.R;
import com.greenkey.librain.entity.ResourceType;

/**
 * Created by Alexander on 10.02.2017.
 */

public class BoardItemView extends FrameLayout {

    private final Context context;

    private ImageView imageView;
    private ResourceType resourceType;

    private static final int ITEM_SIZE_DP = 60;
    private static final int ITEM_MARGIN_DP = 4;
    private static final int ITEM_PADDING_DP = 2;

    public ResourceType getResourceType() {
        if (resourceType == null) {
            return ResourceType.NONE;
        }

        return resourceType;
    }

    public BoardItemView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public BoardItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public BoardItemView(Context context, AttributeSet attrs, int defStyleAttr) {
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

        this.setPadding(itemPaddingPx, itemPaddingPx, itemPaddingPx, itemPaddingPx);
        this.setLayoutParams(itemLayoutParams);
        this.setBackgroundResource(R.drawable.normal_shape);
    }

    private static final FrameLayout.LayoutParams imageViewLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT);

    public void createImageView(ResourceType resourceType) {
        this.resourceType = resourceType;

        if (imageView == null){
            imageView = new ImageView(context);
            imageView.setLayoutParams(imageViewLayoutParams);

            this.addView(imageView);
        }

        imageView.setImageResource(resourceType.getEnabledItemResourceId());
    }

    private boolean isItemPressed;
    public boolean isItemPressed() {
        return isItemPressed;
    }

    public void press() {
        isItemPressed = true;
        this.setBackgroundColor(Color.CYAN);
    }

    public void depress() {
        isItemPressed = false;
        this.setBackgroundResource(R.drawable.normal_shape);
    }

    public void removeImageView() {
        if (imageView != null) {
            this.removeView(imageView);
            this.resourceType= null;
            this.imageView = null;
        }
    }

    public boolean hasImageView() {
        return imageView != null;
    }
}