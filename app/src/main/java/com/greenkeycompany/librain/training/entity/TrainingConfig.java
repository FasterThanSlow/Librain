package com.greenkeycompany.librain.training.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tert0 on 07.09.2017.
 */

public class TrainingConfig implements Parcelable {

    private int rowCount;
    private int columnCount;

    private int itemCount;
    private int itemTypeCount;

    private boolean isFirstRoundSelected;
    private boolean  isSecondRoundSelected;
    private boolean isThirdRoundSelected;

    public TrainingConfig() {
    }

    public TrainingConfig(int rowCount, int columnCount,
                          int itemCount, int itemTypeCount,
                          boolean isFirstRoundSelected, boolean isSecondRoundSelected, boolean isThirdRoundSelected) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.itemCount = itemCount;
        this.itemTypeCount = itemTypeCount;
        this.isFirstRoundSelected = isFirstRoundSelected;
        this.isSecondRoundSelected = isSecondRoundSelected;
        this.isThirdRoundSelected = isThirdRoundSelected;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public int getItemTypeCount() {
        return itemTypeCount;
    }

    public void setItemTypeCount(int itemTypeCount) {
        this.itemTypeCount = itemTypeCount;
    }

    public boolean isFirstRoundSelected() {
        return isFirstRoundSelected;
    }

    public void setFirstRoundSelected(boolean firstRoundSelected) {
        isFirstRoundSelected = firstRoundSelected;
    }

    public boolean isSecondRoundSelected() {
        return isSecondRoundSelected;
    }

    public void setSecondRoundSelected(boolean secondRoundSelected) {
        isSecondRoundSelected = secondRoundSelected;
    }

    public boolean isThirdRoundSelected() {
        return isThirdRoundSelected;
    }

    public void setThirdRoundSelected(boolean thirdRoundSelected) {
        isThirdRoundSelected = thirdRoundSelected;
    }

    protected TrainingConfig(Parcel in) {
        rowCount = in.readInt();
        columnCount = in.readInt();
        itemCount = in.readInt();
        itemTypeCount = in.readInt();
        isFirstRoundSelected = in.readByte() != 0x00;
        isSecondRoundSelected = in.readByte() != 0x00;
        isThirdRoundSelected = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(rowCount);
        dest.writeInt(columnCount);
        dest.writeInt(itemCount);
        dest.writeInt(itemTypeCount);
        dest.writeByte((byte) (isFirstRoundSelected ? 0x01 : 0x00));
        dest.writeByte((byte) (isSecondRoundSelected ? 0x01 : 0x00));
        dest.writeByte((byte) (isThirdRoundSelected ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TrainingConfig> CREATOR = new Parcelable.Creator<TrainingConfig>() {
        @Override
        public TrainingConfig createFromParcel(Parcel in) {
            return new TrainingConfig(in);
        }

        @Override
        public TrainingConfig[] newArray(int size) {
            return new TrainingConfig[size];
        }
    };
}
