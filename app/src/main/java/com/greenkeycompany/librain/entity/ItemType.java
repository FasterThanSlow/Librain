package com.greenkeycompany.librain.entity;

import com.greenkeycompany.librain.R;

/**
 * Created by Alexander on 09.02.2017.
 */

public enum ItemType {
    NONE(0),

    EARTH(R.drawable.ic_planet_earth),
    MARS(R.drawable.ic_mars),
    JUPITER(R.drawable.ic_jupiter),
    MOON(R.drawable.ic_moon),
    GLOBAL(R.drawable.planet_global),

    CHERRY(R.drawable.fruit_cherry),
    FRUIT(R.drawable.ic_fruit),
    BANANAS(R.drawable.ic_bananas),
    PEAR(R.drawable.fruit_pear),
    APPLE(R.drawable.fruit_apple),

    PEAS(R.drawable.ic_peas),
    PUMPKIN(R.drawable.ic_pumpkin),
    FOOD(R.drawable.ic_food),
    CHEESE(R.drawable.vegatable_cheese);

    private final int enabledItemResourceId;

    ItemType(int enabledItemResourceId) {
        this.enabledItemResourceId = enabledItemResourceId;
    }

    public int getEnabledItemResourceId() {
        return enabledItemResourceId;
    }
}
