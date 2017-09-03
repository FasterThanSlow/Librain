package com.greenkeycompany.librain.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by Alexander on 09.02.2017.
 */

public class Rule implements Parcelable {

    private int itemsCount;
    private ItemType itemType;

    public int getItemsCount() {
        return itemsCount;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public Rule(int itemsCount, @NonNull ItemType itemType) {
        this.itemsCount = itemsCount;
        this.itemType = itemType;
    }

    public Rule(Parcel in) {
        this.itemsCount = in.readInt();
        this.itemType = ItemType.valueOf(in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(itemsCount);
        dest.writeString(itemType.name());
    }

    public static final Parcelable.Creator<Rule> CREATOR = new Parcelable.Creator<Rule>() {

        @Override
        public Rule createFromParcel(Parcel source) {
            return new Rule(source);
        }

        @Override
        public Rule[] newArray(int size) {
            return new Rule[size];
        }
    };
}
