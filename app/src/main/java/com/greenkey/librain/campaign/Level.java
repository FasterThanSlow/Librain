package com.greenkey.librain.campaign;

import android.os.Parcel;
import android.os.Parcelable;

import com.greenkey.librain.Rule;

/**
 * Created by Alexander on 16.02.2017.
 */

public class Level implements Parcelable {

    private final int levelNumber;

    private final int rowCount;
    private final int columnCount;

    private final Rule[] rules;

    public Rule[] getRules() {
        return rules;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public Level(int levelNumber, int rowCount, int columnCount, Rule[] rules) {
        this.levelNumber = levelNumber;
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.rules = rules;
    }

    public Level(Parcel in) {
        this.levelNumber = in.readInt();
        this.rowCount = in.readInt();
        this.columnCount = in.readInt();
        this.rules = in.createTypedArray(Rule.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(levelNumber);
        dest.writeInt(rowCount);
        dest.writeInt(columnCount);
        dest.writeTypedArray(rules, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Level> CREATOR = new Parcelable.Creator<Level>() {

        @Override
        public Level createFromParcel(Parcel source) {
            return new Level(source);
        }

        @Override
        public Level[] newArray(int size) {
            return new Level[size];
        }
    };
}
