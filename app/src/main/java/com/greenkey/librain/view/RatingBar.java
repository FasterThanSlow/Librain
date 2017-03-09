package com.greenkey.librain.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.greenkey.librain.R;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: document your custom view class.
 */
public class RatingBar extends LinearLayout {

    private static final int DEFAULT_MAX = 3;
    private static final int DEFAULT_PROGRESS = 0;

    private static final int DEFAULT_STARS_DRAWABLE = R.drawable.star;
    private static final int DEFAULT_STARS_COMPLETED_DRAWABLE = R.drawable.star_completed;

    private int max;
    private int progress;

    private int starsSrc;
    private int starsCompletedSrc;

    private List<ImageView> starImageViews;

    public RatingBar(Context context) {
        super(context);
        init(null, 0);
    }

    public RatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public RatingBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        final TypedArray typedArray = getContext().obtainStyledAttributes(
                attrs, R.styleable.RatingBar, defStyle, 0);

        max = typedArray.getInteger(R.styleable.RatingBar_max, DEFAULT_MAX);
        progress = typedArray.getInteger(R.styleable.RatingBar_progress, DEFAULT_PROGRESS);

        starsSrc = typedArray.getResourceId(R.styleable.RatingBar_starsSrc, DEFAULT_STARS_DRAWABLE);
        starsCompletedSrc = typedArray.getResourceId(R.styleable.RatingBar_starsCompletedSrc, DEFAULT_STARS_COMPLETED_DRAWABLE);

        typedArray.recycle();

        if (max < 0) {
            max = 0;
        }

        if (progress > max) {
            progress = max;
        }

        createItems(progress, max, starsSrc, starsCompletedSrc);
    }

    private void createItems(int progress, int max, int starsResource, int starsCompletedResource) {
        this.removeAllViews();

        starImageViews = new ArrayList<>(max);
        for (int i = 0; i < max; i++) {
            ImageView imageView = new ImageView(getContext());

            if (i < progress) {
                imageView.setImageResource(starsCompletedResource);
            } else {
                imageView.setImageResource(starsResource);
            }

            starImageViews.add(imageView);

            this.addView(imageView);
        }
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        if (max < 0) {
            this.max = 0;
        } else {
            this.max = max;
        }

        createItems(this.progress, this.max, this.starsSrc, this.starsCompletedSrc);
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        if (progress < 0) {
            this.progress = 0;
        } else if (progress > max) {
            this.progress = max;
        } else {
            this.progress = progress;
        }

        createItems(this.progress, this.max, this.starsSrc, this.starsCompletedSrc);
    }

}
