package com.greenkey.librain.reciverview;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.greenkey.librain.PixelConverter;
import com.greenkey.librain.R;
import com.greenkey.librain.ResourceType;

/**
 * Created by Alexander on 10.02.2017.
 */

public class BoardItemView extends FrameLayout {

    private OnBoardItemDragListener boardItemDragListener;
    public void setBoardItemDragListener(OnBoardItemDragListener listener) {
        this.boardItemDragListener = listener;
    }

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

    private OnTouchListener imageViewOnTouchListener;
    public void setImageViewOnTouchListener(OnTouchListener listener) {
        imageViewOnTouchListener = listener;
        if (imageView != null) {
            imageView.setOnTouchListener(listener);
        }
    }

    private static final FrameLayout.LayoutParams imageLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER);

    public void createImageView(ResourceType resourceType) {
        this.resourceType = resourceType;

        imageView = new ImageView(context);

        imageView.setLayoutParams(imageLayoutParams);
        imageView.setImageResource(resourceType.getEnabledItemResourceId());
        imageView.setOnTouchListener(imageViewOnTouchListener);

        this.addView(imageView);
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

    @Override
    public boolean onDragEvent(DragEvent event) {
        if (boardItemDragListener != null) {
            return boardItemDragListener.onBoardItemDrag(this, event);
        }

        return super.onDragEvent(event);
    }
}