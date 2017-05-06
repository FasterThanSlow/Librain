package com.greenkey.librain.training;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Alexander on 03.05.2017.
 */

public class TrainingLevel implements Parcelable {

    private final int showingTime;

    private final int rowCount;
    private final int columnCount;

    private boolean isFirstRound;
    private boolean isSecondRound;
    private boolean isThirdRound;

    private final int[] roundItems;

    public TrainingLevel(int rowCount, int columnCount, boolean isFirstRound, boolean isSecondRound, boolean isThirdRound, int[] roundItems) {
        this.showingTime = 1000;

        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.isFirstRound = isFirstRound;
        this.isSecondRound = isSecondRound;
        this.isThirdRound = isThirdRound;
        this.roundItems = roundItems;
    }

    public boolean isFirstRound() {
        return isFirstRound;
    }

    public boolean isSecondRound() {
        return isSecondRound;
    }

    public boolean isThirdRound() {
        return isThirdRound;
    }

    public void setFirstRound(boolean firstRound) {
        isFirstRound = firstRound;
    }

    public void setSecondRound(boolean secondRound) {
        isSecondRound = secondRound;
    }

    public void setThirdRound(boolean thirdRound) {
        isThirdRound = thirdRound;
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

    public int[] getRoundItems() {
        return roundItems;
    }


    public TrainingLevel(Parcel in) {
        this.showingTime = in.readInt();

        this.columnCount = in.readInt();
        this.rowCount = in.readInt();

        this.isFirstRound = in.readInt() == 1;
        this.isSecondRound = in.readInt() == 1;
        this.isThirdRound = in.readInt() == 1;

        this.roundItems = in.createIntArray();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(showingTime);

        dest.writeInt(columnCount);
        dest.writeInt(rowCount);


        dest.writeInt(isFirstRound ? 1 : 0);
        dest.writeInt(isSecondRound ? 1 : 0);
        dest.writeInt(isThirdRound ? 1 : 0);

        dest.writeIntArray(roundItems);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TrainingLevel> CREATOR = new Creator<TrainingLevel>() {

        @Override
        public TrainingLevel createFromParcel(Parcel source) {
            return new TrainingLevel(source);
        }

        @Override
        public TrainingLevel[] newArray(int size) {
            return new TrainingLevel[size];
        }
    };
}
