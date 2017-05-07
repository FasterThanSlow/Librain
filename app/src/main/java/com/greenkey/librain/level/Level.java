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
        SPACE(new ItemType[] {ItemType.MARS, ItemType.EARTH, ItemType.JUPITER, ItemType.MOON, ItemType.GLOBAL}),
        FRUIT(new ItemType[] { ItemType.CHERRY, ItemType.FRUIT, ItemType.BANANAS, ItemType.APPLE, ItemType.PEAR}),
        VEGETABLE(new ItemType[] {ItemType.PEAS, ItemType.PUMPKIN, ItemType.FOOD, ItemType.CHEESE});

        private ItemType[] resources;

        LevelType(ItemType[] resources) {
            this.resources = resources;
        }

        public ItemType[] getResources() {
            return resources;
        }
    }

    private final int levelId;

    private int record;
    private boolean isEnabled;
    private boolean isPremium;

    private final LevelType levelType;

    private final Round firstRound;
    private final Round secondRound;
    private final Round thirdRound;

    public int getLevelId() {
        return levelId;
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

    public Round getFirstRound() {
        return firstRound;
    }

    public Round getSecondRound() {
        return secondRound;
    }

    public Round getThirdRound() {
        return thirdRound;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public void setRecord(int record) {
        this.record = record;
    }

    public Level(int levelId,
                 @NonNull LevelType levelType,
                 @NonNull Round firstRound,
                 @NonNull Round secondRound,
                 @NonNull Round thirdRound) {

        this.levelId = levelId;
        this.levelType = levelType;

        this.firstRound = firstRound;
        this.secondRound = secondRound;
        this.thirdRound = thirdRound;
    }

    public Level(int levelId,
                 boolean isPremium,
                 @NonNull LevelType levelType,
                 @NonNull Round firstRound,
                 @NonNull Round secondRound,
                 @NonNull Round thirdRound) {

        this.levelId = levelId;
        this.isPremium = isPremium;
        this.levelType = levelType;

        this.firstRound = firstRound;
        this.secondRound = secondRound;
        this.thirdRound = thirdRound;
    }

    public Level(Parcel in) {
        this.levelId = in.readInt();

        this.isPremium = in.readInt() == 1;
        this.levelType = LevelType.valueOf(in.readString());

        this.firstRound = in.readParcelable(Round.class.getClassLoader());
        this.secondRound = in.readParcelable(Round.class.getClassLoader());
        this.thirdRound = in.readParcelable(Round.class.getClassLoader());

        this.record = in.readInt();
        this.isEnabled = in.readInt() == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(levelId);

        dest.writeInt(isPremium ? 1 : 0);
        dest.writeString(levelType.name());

        dest.writeParcelable(firstRound, flags);
        dest.writeParcelable(secondRound, flags);
        dest.writeParcelable(thirdRound, flags);

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
