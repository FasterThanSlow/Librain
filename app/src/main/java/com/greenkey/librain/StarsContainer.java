package com.greenkey.librain;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: document your custom view class.
 */
public class StarsContainer extends LinearLayout {

    private static final int DEFAULT_MAX = 3;
    private static final int DEFAULT_PROGRESS = 0;

    private static final int DEFAULT_STARS_DRAWABLE = R.drawable.star;
    private static final int DEFAULT_STARS_COMPLETED_DRAWABLE = R.drawable.star_completed;

    private int max;
    private int progress;

    private int starsSrc;
    private int starsCompletedSrc;

    private List<ImageView> starImageViews;

    public StarsContainer(Context context) {
        super(context);
        init(null, 0);
    }

    public StarsContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public StarsContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        final TypedArray typedArray = getContext().obtainStyledAttributes(
                attrs, R.styleable.StarsContainer, defStyle, 0);

        max = typedArray.getInteger(R.styleable.StarsContainer_max, DEFAULT_MAX);
        progress = typedArray.getInteger(R.styleable.StarsContainer_progress, DEFAULT_PROGRESS);

        starsSrc = typedArray.getResourceId(R.styleable.StarsContainer_starsSrc, DEFAULT_STARS_DRAWABLE);
        starsCompletedSrc = typedArray.getResourceId(R.styleable.StarsContainer_starsCompletedSrc, DEFAULT_STARS_COMPLETED_DRAWABLE);

        typedArray.recycle();

        if (max > 0) {
            starImageViews = new ArrayList<>(max);
        }

        for (int i = 0; i < max; i++) {
            ImageView imageView = new ImageView(getContext());

            if (i < progress) {
                imageView.setImageResource(starsCompletedSrc);
            } else {
                imageView.setImageResource(starsSrc);
            }

            starImageViews.add(imageView);

            this.addView(imageView);
        }
    }

}
