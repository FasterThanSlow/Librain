package com.greenkey.librain.level.gameround;

import com.greenkey.librain.entity.ResourceType;

/**
 * Created by Alexander on 31.03.2017.
 */

public class SecondGameRound extends GameRound {

    private final ResourceType[] firstPart;
    private final ResourceType[] secondPart;

    public ResourceType[] getFirstPart() {
        return firstPart;
    }

    public ResourceType[] getSecondPart() {
        return secondPart;
    }

    public SecondGameRound(ResourceType[] answer, ResourceType[] firstPart, ResourceType[] secondPart) {
        super(answer);
        this.firstPart = firstPart;
        this.secondPart = secondPart;
    }
}
