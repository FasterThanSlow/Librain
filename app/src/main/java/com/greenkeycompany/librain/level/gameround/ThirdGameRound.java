package com.greenkeycompany.librain.level.gameround;

import com.greenkeycompany.librain.entity.ItemType;

/**
 * Created by Alexander on 01.04.2017.
 */

public class ThirdGameRound extends GameRound {

    private int trueAnswerPart;

    private final ItemType[] firstPart;
    private final ItemType[] secondPart;
   /* private final ItemType[] thirdPart;
*/
    public int getTrueAnswerPart() {
        return trueAnswerPart;
    }

    public ItemType[] getFirstPart() {
        return firstPart;
    }

    public ItemType[] getSecondPart() {
        return secondPart;
    }
/*
    public ItemType[] getThirdPart() {
        return thirdPart;
    }
*/
    public ThirdGameRound(ItemType[] answer, int trueAnswerPart, ItemType[] firstPart, ItemType[] secondPart/*, ItemType[] thirdPart*/) {
        super(answer);
        this.trueAnswerPart = trueAnswerPart;
        this.firstPart = firstPart;
        this.secondPart = secondPart;
        //this.thirdPart = thirdPart;
    }
}
