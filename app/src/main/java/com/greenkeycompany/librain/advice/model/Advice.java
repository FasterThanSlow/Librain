package com.greenkeycompany.librain.advice.model;

/**
 * Created by tert0 on 04.09.2017.
 */

public class Advice {

    private boolean favorite;

    private final String title;
    private final String message;

    public Advice(String title, String message) {
        this.title = title;
        this.message = message;
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
}
