package com.greenkey.librain.reciverview;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.view.DragEvent;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.greenkey.librain.ResourceType;

/**
 * Created by Alexander on 10.02.2017.
 */

public class BoardItemView extends FrameLayout {

    private OnBoardItemDragListener boardItemDragListener;
    public void setBoardItemDragListener(OnBoardItemDragListener listener) {
        this.boardItemDragListener = listener;
    }

    private Context context;

    private ImageView imageView;
    private ResourceType resourceType;

    public ResourceType getResourceType() {
        if (resourceType == null) {
            return ResourceType.NONE;
        }

        return resourceType;
    }

    public BoardItemView(Context context) {
        super(context);

        this.context = context;
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