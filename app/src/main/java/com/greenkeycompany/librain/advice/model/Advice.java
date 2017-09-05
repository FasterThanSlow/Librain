package com.greenkeycompany.librain.advice.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tert0 on 04.09.2017.
 */

public class Advice implements Parcelable {

    private boolean favorite;

    private final int id;
    private final String title;
    private final String message;

    public Advice(int id, String title, String message) {
        this.id = id;
        this.title = title;
        this.message = message;
    }

    public Advice(boolean favorite, int id, String title, String message) {
        this.favorite = favorite;
        this.id = id;
        this.title = title;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    protected Advice(Parcel in) {
        favorite = in.readByte() != 0x00;
        id = in.readInt();
        title = in.readString();
        message = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (favorite ? 0x01 : 0x00));
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(message);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Advice> CREATOR = new Parcelable.Creator<Advice>() {
        @Override
        public Advice createFromParcel(Parcel in) {
            return new Advice(in);
        }

        @Override
        public Advice[] newArray(int size) {
            return new Advice[size];
        }
    };
}
