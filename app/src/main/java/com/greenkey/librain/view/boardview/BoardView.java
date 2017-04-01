package com.greenkey.librain.view.boardview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.greenkey.librain.R;
import com.greenkey.librain.entity.ItemType;
import com.greenkey.librain.view.PixelConverter;

/**
 * Created by Alexander on 10.02.2017.
 */

public class BoardView extends LinearLayout {

    private static final int ROW_COUNT_DEFAULT_VALUE = 3;
    private static final int COLUMN_COUNT_DEFAULT_VALUE = 3;

    private int rowCount;
    private int columnCount;

    private OnTouchListener itemsOnTouchListener;
    public void setItemsOnTouchListener(OnTouchListener listener) {
        this.itemsOnTouchListener = listener;

        int itemsCount = rowCount * columnCount;
        for (int i = 0; i < itemsCount; i++) {
            items[i].setOnTouchListener(listener);
        }
    }

    private BoardItemView[] items;

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

        rowCount = typedArray.getInteger(R.styleable.BoardView_rowCount, ROW_COUNT_DEFAULT_VALUE);
        columnCount = typedArray.getInteger(R.styleable.BoardView_columnCount, COLUMN_COUNT_DEFAULT_VALUE);

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
                final BoardItemView boardItemView = new BoardItemView(context);

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
            throw new IllegalArgumentException("Wrong array length. Items count must be " + itemsCount);
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
    public class BoardItemView extends FrameLayout {

        private final Context context;

        private ImageView imageView;
        private ItemType itemType;

        private static final int ITEM_SIZE_DP = 60;
        private static final int ITEM_MARGIN_DP = 4;
        private static final int ITEM_PADDING_DP = 2;

        public ItemType getItemType() {
            if (itemType == null) {
                return ItemType.NONE;
            }

            return itemType;
        }

        BoardItemView(Context context) {
            super(context);
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
            this.setBackgroundResource(R.drawable.game_board_item_background);
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

        public void press() {
            isItemPressed = true;
            this.setBackgroundResource(R.drawable.game_board_item_selected_background);
        }

        public void depress() {
            isItemPressed = false;
            this.setBackgroundResource(R.drawable.game_board_item_background);
        }


        public void createImageView(ItemType itemType) {
            this.itemType = itemType;

            if (imageView == null){
                imageView = new ImageView(context);
                imageView.setLayoutParams(imageViewLayoutParams);

                this.addView(imageView);
            }

            imageView.setImageResource(itemType.getEnabledItemResourceId());
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
