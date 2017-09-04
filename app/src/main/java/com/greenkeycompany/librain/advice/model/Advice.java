package com.greenkeycompany.librain.advice.model;

/**
 * Created by tert0 on 04.09.2017.
 */

public class Advice {

    private boolean favorite;
    private final String text;

    public Advice(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
