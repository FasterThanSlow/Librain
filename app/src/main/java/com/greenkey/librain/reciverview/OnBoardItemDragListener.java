package com.greenkey.librain.reciverview;

import android.view.DragEvent;

import com.greenkey.librain.MainActivity;

/**
 * Created by Alexander on 10.02.2017.
 */

public interface OnBoardItemDragListener {
    boolean onBoardItemDrag(BoardItemView receiverView, DragEvent event);
}
