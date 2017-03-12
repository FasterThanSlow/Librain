package com.greenkey.librain.level;

import android.os.Parcel;
import android.os.Parcelable;

import com.greenkey.librain.entity.Rule;

/**
 * Created by Alexander on 16.02.2017.
 */

public class Level implements Parcelable {

    private final int levelId;

    private final int showingTime;

    private final int rowCount;
    private final int columnCount;

    private int record;
    private boolean isEnabled;

    private LevelEnvironment levelEnvironment;

    public Rule[] getRules() {
        return levelEnvironment.getRules();
    }

    public int getShowingTime() {
        return showingTime;
    }

    public int getLevelId() {
        return levelId;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getRecord() {
        return record;
    }

    public void setRecord(int record) {
        this.record = record;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public Level(int levelId, int showingTime, int rowCount, int columnCount, LevelEnvironment levelEnvironment) {
        this.levelId = levelId;
        this.showingTime = showingTime;
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.levelEnvironment = levelEnvironment;
    }

    public Level(Parcel in) {
        this.levelId = in.readInt();
        this.showingTime = in.readInt();
        this.rowCount = in.readInt();
        this.columnCount = in.readInt();
        this.levelEnvironment = in.readParcelable(LevelEnvironment.class.getClassLoader());

        this.record = in.readInt();
        this.isEnabled = in.readInt() == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(levelId);
        dest.writeInt(showingTime);
        dest.writeInt(rowCount);
        dest.writeInt(columnCount);
        dest.writeParcelable(levelEnvironment, flags);

        dest.writeInt(record);
        dest.writeInt(isEnabled ? 1 : 0);
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
