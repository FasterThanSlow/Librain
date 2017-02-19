package com.greenkey.librain.reciverview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import com.greenkey.librain.PixelConverter;
import com.greenkey.librain.R;
import com.greenkey.librain.ResourceType;

import java.util.Arrays;

/**
 * Created by Alexander on 10.02.2017.
 */

public class BoardView extends LinearLayout {

    private static final int ROW_COUNT_DEFAULT_VALUE = 3;
    private static final int COLUMN_COUNT_DEFAULT_VALUE = 3;

    private int rowCount;
    private int columnCount;

    //private OnTouchListener itemsImageViewOnTouchListener;
    public void setItemsImageViewOnTouchListener(OnTouchListener listener) {
        //this.itemsImageViewOnTouchListener = listener;

        int itemsCount = rowCount * columnCount;
        for (int i = 0; i < itemsCount; i++) {
            items[i].setImageViewOnTouchListener(listener);
        }
    }

    //private OnBoardItemDragListener itemsDragListener;
    public void setItemsDragListener(OnBoardItemDragListener listener) {
        int itemsCount = rowCount * columnCount;
        for (int i = 0; i < itemsCount; i++) {
            items[i].setBoardItemDragListener(listener);
        }
    }

    private BoardItemView[] items;

    public BoardView(Context context) {
        super(context);
        init(null, 0);
    }

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public BoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray typedArray = getContext().obtainStyledAttributes(
                attrs, R.styleable.BoardView, defStyle, 0);

        rowCount = typedArray.getInteger(R.styleable.BoardView_rowCount, ROW_COUNT_DEFAULT_VALUE);
        columnCount = typedArray.getInteger(R.styleable.BoardView_columnCount, COLUMN_COUNT_DEFAULT_VALUE);

        typedArray.recycle();

        createItems(getContext(), rowCount, columnCount);
    }

    private static final LinearLayout.LayoutParams boardItemParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    private static final int ITEM_SIZE_DP = 60;
    private static final int ITEM_MARGIN_DP = 4;
    private static final int ITEM_PADDING_DP = 2;

    public void createItems(Context context, int rowCount, int columnCount) {
        int itemSizePx = PixelConverter.dpToPx(context, ITEM_SIZE_DP);
        int itemMarginPx = PixelConverter.dpToPx(context, ITEM_MARGIN_DP);
        int itemPaddingPx = PixelConverter.dpToPx(context, ITEM_PADDING_DP);

        LinearLayout.LayoutParams itemLayoutParams = new LinearLayout.LayoutParams(itemSizePx, itemSizePx);
        itemLayoutParams.setMargins(itemMarginPx, itemMarginPx, itemMarginPx, itemMarginPx);

        this.rowCount = rowCount;
        this.columnCount = columnCount;

        items = new BoardItemView[rowCount * columnCount];

        int currentIndex = 0;
        for (int i = 0; i < rowCount; i++) {
            LinearLayout lineLinearLayout = new LinearLayout(context);
            lineLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            lineLinearLayout.setLayoutParams(boardItemParams);

            for (int j = 0; j < columnCount; j++) {
                BoardItemView boardItemView = new BoardItemView(context);
                boardItemView.setPadding(itemPaddingPx, itemPaddingPx, itemPaddingPx, itemPaddingPx);
                boardItemView.setLayoutParams(itemLayoutParams);
                boardItemView.setBackgroundResource(R.drawable.normal_shape);

                items[currentIndex] = boardItemView;

                lineLinearLayout.addView(boardItemView);

                currentIndex++;
            }

            this.addView(lineLinearLayout);
        }
    }

    public void setItemsResources(@NonNull ResourceType[] resources) {
        this.removeItemsResources();

        int itemsCount = rowCount * columnCount;
        if (resources.length < itemsCount) {
            throw new IllegalArgumentException("Wrong array length. Items count must be " + itemsCount);
        }

        for (int i = 0; i < itemsCount; i++) {
            if (resources[i] != ResourceType.NONE) {
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

    public ResourceType[] getItemsResources() {
        int itemsCount = rowCount * columnCount;

        ResourceType[] result = new ResourceType[itemsCount];
        for (int i = 0; i < itemsCount; i++) {
            result[i] = items[i].getResourceType();
        }

        return result;
    }

}
