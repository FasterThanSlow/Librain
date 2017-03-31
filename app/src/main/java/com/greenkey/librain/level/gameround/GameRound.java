package com.greenkey.librain.level.gameround;

import com.greenkey.librain.entity.ResourceType;

/**
 * Created by Alexander on 31.03.2017.
 */

public abstract class GameRound {

    private final ResourceType[] answer;

    public ResourceType[] getAnswer() {
        return answer;
    }

    public GameRound(ResourceType[] answer) {
        this.answer = answer;
    }

}
