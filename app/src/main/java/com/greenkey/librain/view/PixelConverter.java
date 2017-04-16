package com.greenkey.librain.view;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by Alexander on 11.02.2017.
 */

public class PixelConverter {
    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT) +0.5f);
    }

    public static int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int)(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)  +0.5f);
    }
}
