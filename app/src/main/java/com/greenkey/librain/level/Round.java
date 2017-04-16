package com.greenkey.librain.level;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by Alexander on 16.04.2017.
 */

public class Round implements Parcelable {

    private final int showingTime;

    private final int rowCount;
    private final int columnCount;

    private final int[] roundItems;

    public int getShowingTime() {
        return showingTime;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int[] getRoundItems() {
        return roundItems;
    }

    public Round(int showingTime, int rowCount, int columnCount, @NonNull int[] roundItems) {
        this.showingTime = showingTime;
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.roundItems = roundItems;
    }

    public Round(Parcel in) {
        this.showingTime = in.readInt();
        this.rowCount = in.readInt();;
        this.columnCount = in.readInt();;
        this.roundItems = in.createIntArray();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(showingTime);
        dest.writeInt(rowCount);
        dest.writeInt(columnCount);
        dest.writeIntArray(roundItems);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Round> CREATOR = new Creator<Round>() {

        @Override
        public Round createFromParcel(Parcel source) {
            return new Round(source);
        }

        @Override
        public Round[] newArray(int size) {
            return new Round[size];
        }
    };
}
