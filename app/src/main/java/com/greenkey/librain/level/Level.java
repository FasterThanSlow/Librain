package com.greenkey.librain.level;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.greenkey.librain.entity.ItemType;

/**
 * Created by Alexander on 16.02.2017.
 */

public class Level implements Parcelable {

    public enum LevelType {

        SPACE(new ItemType[] {ItemType.MARS, ItemType.EARTH, ItemType.JUPITER, ItemType.MOON}),
        FRUIT(new ItemType[] {ItemType.ORANGE, ItemType.CHERRY, ItemType.FRUIT, ItemType.BANANAS, ItemType.APRICOT, ItemType.FIG}),
        VEGETABLE(new ItemType[] {ItemType.PEAS, ItemType.PUMPKIN, ItemType.FOOD});

        private ItemType[] resources;

        LevelType(ItemType[] resources) {
            this.resources = resources;
        }

        public ItemType[] getResources() {
            return resources;
        }
    }

    private final int levelId;
    private final int showingTime;
    private final int rowCount;
    private final int columnCount;
    private int record;
    private boolean isEnabled;
    private boolean isPremium;

    private final LevelType levelType;
    private final int[] firstRoundItems;
    private final int[] secondRoundItems;
    private final int[] thirdRoundItems;

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

    public boolean isPremium() {
        return isPremium;
    }

    public int[] getFirstRoundItems() {
        return firstRoundItems;
    }

    public int[] getSecondRoundItems() {
        return secondRoundItems;
    }

    public int[] getThirdRoundItems() {
        return thirdRoundItems;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public void setRecord(int record) {
        this.record = record;
    }

    public Level(int levelId, int showingTime, int rowCount, int columnCount,
                 @NonNull LevelType levelType,
                 @NonNull int[] firstRoundItems,
                 @NonNull int[] secondRoundItems,
                 @NonNull int[] thirdRoundItems) {
        this.levelId = levelId;
        this.showingTime = showingTime;
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.levelType = levelType;
        this.firstRoundItems = firstRoundItems;
        this.secondRoundItems = secondRoundItems;
        this.thirdRoundItems = thirdRoundItems;
    }

    public Level(int levelId, int showingTime, int rowCount, int columnCount,
                 boolean isPremium,
                 @NonNull LevelType levelType,
                 @NonNull int[] firstRoundItems,
                 @NonNull int[] secondRoundItems,
                 @NonNull int[] thirdRoundItems) {
        this.levelId = levelId;
        this.showingTime = showingTime;
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.isPremium = isPremium;
        this.levelType = levelType;
        this.firstRoundItems = firstRoundItems;
        this.secondRoundItems = secondRoundItems;
        this.thirdRoundItems = thirdRoundItems;
    }

    public Level(Parcel in) {
        this.levelId = in.readInt();
        this.showingTime = in.readInt();
        this.rowCount = in.readInt();
        this.columnCount = in.readInt();
        this.isPremium = in.readInt() == 1;
        this.levelType = LevelType.valueOf(in.readString());
        this.firstRoundItems = in.createIntArray();
        this.secondRoundItems = in.createIntArray();
        this.thirdRoundItems = in.createIntArray();

        this.record = in.readInt();
        this.isEnabled = in.readInt() == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(levelId);
        dest.writeInt(showingTime);
        dest.writeInt(rowCount);
        dest.writeInt(columnCount);
        dest.writeInt(isPremium ? 1 : 0);
        dest.writeString(levelType.name());
        dest.writeIntArray(firstRoundItems);
        dest.writeIntArray(secondRoundItems);
        dest.writeIntArray(thirdRoundItems);

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
