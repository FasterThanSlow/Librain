package com.greenkey.librain.level;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.greenkey.librain.entity.ResourceType;
import com.greenkey.librain.entity.Rule;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by Alexander on 16.02.2017.
 */

public class Level implements Parcelable {

    public enum LevelType {

        SPACE(new ResourceType[] {ResourceType.MARS, ResourceType.EARTH, ResourceType.ASTEROID,ResourceType.JUPITER,ResourceType.MOON}),
        FRUIT(new ResourceType[] {ResourceType.ORANGE, ResourceType.CHERRY, ResourceType.FRUIT, ResourceType.BANANAS, ResourceType.APRICOT,ResourceType.FIG});

        private ResourceType[] resources;
        LevelType(ResourceType[] resources) {
            this.resources = resources;
        }

        public ResourceType[] getResources() {
            return resources;
        }
    }

    private final int levelId;
    private final int showingTime;
    private final int rowCount;
    private final int columnCount;
    private int record;
    private boolean isEnabled;

    private LevelType levelType;
    private int[] items;

    public int getLevelId() {
        return levelId;
    }

    public int getShowingTime() {
        return showingTime;
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

    public boolean isEnabled() {
        return isEnabled;
    }

    public LevelType getLevelType() {
        return levelType;
    }

    public int[] getItems() {
        return items;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public void setRecord(int record) {
        this.record = record;
    }

    public Level(int levelId, int showingTime, int rowCount, int columnCount,
                 @NonNull LevelType levelType, @NonNull int[] items) {
        this.levelId = levelId;
        this.showingTime = showingTime;
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.levelType = levelType;
        this.items = items;
    }

    public Level(Parcel in) {
        this.levelId = in.readInt();
        this.showingTime = in.readInt();
        this.rowCount = in.readInt();
        this.columnCount = in.readInt();
        this.levelType = LevelType.valueOf(in.readString());
        this.items = in.createIntArray();

        this.record = in.readInt();
        this.isEnabled = in.readInt() == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(levelId);
        dest.writeInt(showingTime);
        dest.writeInt(rowCount);
        dest.writeInt(columnCount);
        dest.writeString(levelType.name());
        dest.writeIntArray(items);

        dest.writeInt(record);
        dest.writeInt(isEnabled ? 1 : 0);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Level> CREATOR = new Creator<Level>() {

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
