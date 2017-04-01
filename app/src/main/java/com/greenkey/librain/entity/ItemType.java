package com.greenkey.librain.entity;

import com.greenkey.librain.R;

/**
 * Created by Alexander on 09.02.2017.
 */

public enum ItemType {
    NONE(0),

    GB(R.drawable.ic_united_kingdom),
    EU(R.drawable.ic_european_union),
    EN(R.drawable.ic_england),

    EARTH(R.drawable.ic_planet_earth),
    MARS(R.drawable.ic_mars),
    JUPITER(R.drawable.ic_jupiter),
    MOON(R.drawable.ic_moon),
    ASTEROID(R.drawable.ic_asteroid),
    DESTRYED(R.drawable.ic_destroyed_planet),

    ORANGE(R.drawable.ic_orange),
    CHERRY(R.drawable.ic_cherries),
    FRUIT(R.drawable.ic_fruit),
    APRICOT(R.drawable.ic_apricot),
    BANANAS(R.drawable.ic_bananas),
    FIG(R.drawable.ic_fig),

    PEAS(R.drawable.ic_peas),
    PUMPKIN(R.drawable.ic_pumpkin),
    FOOD(R.drawable.ic_food);

    private final int enabledItemResourceId;

    ItemType(int enabledItemResourceId) {
        this.enabledItemResourceId = enabledItemResourceId;
    }

    public int getEnabledItemResourceId() {
        return enabledItemResourceId;
    }
}
