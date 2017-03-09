package com.greenkey.librain.view.boardview;

import android.view.DragEvent;

/**
 * Created by Alexander on 10.02.2017.
 */

public interface OnBoardItemDragListener {
    boolean onBoardItemDrag(BoardItemView receiverView, DragEvent event);
}
