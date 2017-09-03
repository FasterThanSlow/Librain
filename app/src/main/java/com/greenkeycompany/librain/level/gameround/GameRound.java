package com.greenkeycompany.librain.level.gameround;

import com.greenkeycompany.librain.entity.ItemType;

/**
 * Created by Alexander on 31.03.2017.
 */

public abstract class GameRound {

    private final ItemType[] answer;

    public ItemType[] getAnswer() {
        return answer;
    }

    public GameRound(ItemType[] answer) {
        this.answer = answer;
    }

}
