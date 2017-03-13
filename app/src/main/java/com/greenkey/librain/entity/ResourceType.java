package com.greenkey.librain.entity;

import android.os.Parcelable;

import com.greenkey.librain.R;

/**
 * Created by Alexander on 09.02.2017.
 */

public enum ResourceType {
    NONE(0,0),

    GB(R.drawable.ic_united_kingdom, R.drawable.ic_united_kingdom_dis),
    EU(R.drawable.ic_european_union, R.drawable.ic_european_union_dis),
    EN(R.drawable.ic_england, R.drawable.ic_england_dis),

    EARTH(R.drawable.ic_planet_earth, R.drawable.ic_planet_earth),
    MARS(R.drawable.ic_mars, R.drawable.ic_mars),
    JUPITER(R.drawable.ic_jupiter, R.drawable.ic_jupiter),
    MOON(R.drawable.ic_moon,R.drawable.ic_moon),
    ASTEROID(R.drawable.ic_asteroid,R.drawable.ic_asteroid),
    DESTRYED(R.drawable.ic_destroyed_planet,R.drawable.ic_destroyed_planet),

    ORANGE(R.drawable.ic_orange,R.drawable.ic_orange),
    CHERRY(R.drawable.ic_cherries,R.drawable.ic_cherries),
    FRUIT(R.drawable.ic_fruit,R.drawable.ic_fruit),
    APRICOT(R.drawable.ic_apricot,R.drawable.ic_apricot),
    BANANAS(R.drawable.ic_bananas,R.drawable.ic_bananas),
    FIG(R.drawable.ic_fig,R.drawable.ic_fig),

    PEAS(R.drawable.ic_peas,R.drawable.ic_peas),
    PUMPKIN(R.drawable.ic_pumpkin,R.drawable.ic_pumpkin),
    FOOD(R.drawable.ic_food,R.drawable.ic_food);

    private int enabledItemResourceId;
    private int disabledItemResourceId;

    ResourceType(int enabledItemResourceId, int disabledItemResourceId) {
        this.enabledItemResourceId = enabledItemResourceId;
        this.disabledItemResourceId = disabledItemResourceId;
    }

    public int getEnabledItemResourceId() {
        return enabledItemResourceId;
    }

    public int getDisabledItemResourceId() {
        return disabledItemResourceId;
    }
}
