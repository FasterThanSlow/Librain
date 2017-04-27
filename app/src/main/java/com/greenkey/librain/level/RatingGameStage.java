package com.greenkey.librain.level;

import com.greenkey.librain.entity.Rule;

/**
 * Created by Alexander on 27.04.2017.
 */

public class RatingGameStage {

    private final int showingTime;

    private final int rowCount;
    private final int columnCount;

    private final int[] items;

    private Rule[] rules;

    public int getShowingTime() {
        return showingTime;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int[] getItems() {
        return items;
    }

    public Rule[] getRules() {
        return rules;
    }

    public RatingGameStage(int showingTime, int rowCount, int columnCount, int[] items, Rule[] rules) {
        this.showingTime = showingTime;
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.items = items;
        this.rules = rules;
    }
}
