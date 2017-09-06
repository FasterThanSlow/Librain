package com.greenkeycompany.librain.app.view.boardview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.greenkeycompany.librain.R;
import com.greenkeycompany.librain.entity.ItemType;
import com.greenkeycompany.librain.app.util.PixelConverterUtil;

/**
 * Created by Alexander on 10.02.2017.
 */

public class BoardView extends LinearLayout {

    private int rowCount = 3;
    private int columnCount = 3;

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    private float itemSize = 60;

    private OnTouchListener itemsOnTouchListener;
    public void setItemsOnTouchListener(OnTouchListener listener) {
        this.itemsOnTouchListener = listener;

        int itemsCount = rowCount * columnCount;
        for (int i = 0; i < itemsCount; i++) {
            items[i].setOnTouchListener(listener);
        }
    }

    private BoardItemView[] items;
    public BoardItemView[] getItems() {
        return items;
    }

    private final Context context;

    public BoardView(Context context) {
        super(context);
        this.context = context;
        init(null, 0);
    }

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs, 0);
    }

    public BoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        this.setOrientation(VERTICAL);

        final TypedArray typedArray = context.obtainStyledAttributes(
                attrs, R.styleable.BoardView, defStyle, 0);

        rowCount = typedArray.getInteger(R.styleable.BoardView_rowCount, rowCount);
        columnCount = typedArray.getInteger(R.styleable.BoardView_columnCount, columnCount);

        itemSize = typedArray.getDimension(R.styleable.BoardView_item_size, itemSize);

        typedArray.recycle();

        createItems(rowCount, columnCount);
    }

    private static final LinearLayout.LayoutParams boardRowParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    public void removeItems() {
        this.removeAllViews();
        this.rowCount = 0;
        this.columnCount = 0;
        this.items = null;
    }

    public void createItems(int rowCount, int columnCount) {
        removeItems();

        this.rowCount = rowCount;
        this.columnCount = columnCount;

        this.items = new BoardItemView[rowCount * columnCount];

        int currentIndex = 0;
        for (int i = 0; i < rowCount; i++) {
            LinearLayout lineLinearLayout = new LinearLayout(context);
            lineLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            lineLinearLayout.setLayoutParams(boardRowParams);

            for (int j = 0; j < columnCount; j++) {
                final BoardItemView boardItemView = new BoardItemView(context, itemSize);

                boardItemView.setOnTouchListener(itemsOnTouchListener);

                items[currentIndex] = boardItemView;

                lineLinearLayout.addView(boardItemView);

                currentIndex++;
            }

            this.addView(lineLinearLayout);
        }
    }

    public void setItemsResources(@NonNull ItemType[] resources) {
        this.removeItemsResources();

        int itemsCount = rowCount * columnCount;
        if (resources.length < itemsCount) {
            throw new IllegalArgumentException("Wrong array length. Item count must be " + itemsCount);
        }

        for (int i = 0; i < itemsCount; i++) {
            if (resources[i] != ItemType.NONE) {
                items[i].createImageView(resources[i]);
            }
        }
    }

    public void removeItemsResources() {
        int itemsCount = rowCount * columnCount;

        for (int i = 0; i < itemsCount; i++) {
            items[i].removeImageView();
        }
    }

    public ItemType[] getItemsResources() {
        int itemsCount = rowCount * columnCount;

        ItemType[] result = new ItemType[itemsCount];
        for (int i = 0; i < itemsCount; i++) {
            result[i] = items[i].getItemType();
        }

        return result;
    }


    //ItemView
    public static class BoardItemView extends FrameLayout {

        private final static int SCALE_ANIMATION_DURATION = 350;

        private final Context context;

        private ImageView imageView;
        private ItemType itemType;

        private final int itemSize;

        private static final int ITEM_MARGIN_DP = 4;
        private static final int ITEM_PADDING_DP = 4;

        public ItemType getItemType() {
            if (itemType == null) {
                return ItemType.NONE;
            }

            return itemType;
        }

        public BoardItemView(Context context, float size) {
            super(context);
            this.context = context;
            this.itemSize = (int) (size + 0.5f);
            init();
        }

        private void init() {
            int itemMarginPx = PixelConverterUtil.dpToPx(context, ITEM_MARGIN_DP);
            int itemPaddingPx = PixelConverterUtil.dpToPx(context, ITEM_PADDING_DP);

            LinearLayout.LayoutParams itemLayoutParams = new LinearLayout.LayoutParams(itemSize, itemSize);
            itemLayoutParams.setMargins(itemMarginPx, itemMarginPx, itemMarginPx, itemMarginPx);

            this.setPadding(itemPaddingPx, itemPaddingPx, itemPaddingPx, itemPaddingPx);
            this.setLayoutParams(itemLayoutParams);
            this.setBackgroundResource(R.drawable.board_item_background);
        }

        private final FrameLayout.LayoutParams imageViewLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);

        @Override
        public float getX() {
            return super.getX() + ((LinearLayout)getParent()).getX();
        }

        @Override
        public float getY() {
            return super.getY() + ((LinearLayout)getParent()).getY();
        }

        private boolean isItemPressed;
        public boolean isItemPressed() {
            return isItemPressed;
        }

        public void createImageView(ItemType itemType) {
            this.itemType = itemType;

            if (imageView == null){
                imageView = new ImageView(context);
                imageView.setLayoutParams(imageViewLayoutParams);

                this.addView(imageView);
            }

            imageView.setImageResource(itemType.getEnabledItemResourceId());

            final ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(imageView, View.SCALE_X, 0.0f, 1.0f);
            final ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(imageView, View.SCALE_Y, 0.0f, 1.0f);
            scaleDownX.setInterpolator(new BounceInterpolator());
            scaleDownY.setInterpolator(new BounceInterpolator());
            scaleDownX.setDuration(SCALE_ANIMATION_DURATION);
            scaleDownY.setDuration(SCALE_ANIMATION_DURATION);

            AnimatorSet scaleDown = new AnimatorSet();
            scaleDown.play(scaleDownX).with(scaleDownY);

            scaleDown.start();
        }

        public void removeImageView() {
            if (imageView != null) {
                this.removeView(imageView);
                this.itemType = null;
                this.imageView = null;
            }
        }

        public boolean hasImageView() {
            return imageView != null;
        }
    }

}
