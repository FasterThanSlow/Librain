package com.greenkey.librain.level.gameround;

import com.greenkey.librain.entity.ItemType;

/**
 * Created by Alexander on 31.03.2017.
 */

public class SecondGameRound extends GameRound {

    private final ItemType[] firstPart;
    private final ItemType[] secondPart;

    public ItemType[] getFirstPart() {
        return firstPart;
    }

    public ItemType[] getSecondPart() {
        return secondPart;
    }

    public SecondGameRound(ItemType[] answer, ItemType[] firstPart, ItemType[] secondPart) {
        super(answer);
        this.firstPart = firstPart;
        this.secondPart = secondPart;
    }
}
