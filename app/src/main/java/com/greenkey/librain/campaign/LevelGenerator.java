package com.greenkey.librain.campaign;

import android.support.annotation.NonNull;

import com.greenkey.librain.ResourceType;
import com.greenkey.librain.Rule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 16.02.2017.
 */

public class LevelGenerator {

    private static final int LEVELS_COUNT = 25;

    private static final List<Level> levels = new ArrayList<>(LEVELS_COUNT);
    static {
        levels.add(new Level(1, 2000, 3, 3, new Rule[] {new Rule(2, ResourceType.EU)}));
        levels.add(new Level(2, 5000 ,3, 4, new Rule[] {new Rule(2, ResourceType.EU), new Rule(2, ResourceType.GB)}));
        levels.add(new Level(3, 3000, 3, 3, new Rule[] {new Rule(2, ResourceType.EU)}));
        levels.add(new Level(4, 1000, 3, 4, new Rule[] {new Rule(2, ResourceType.EU), new Rule(2, ResourceType.GB)}));
    }

    @NonNull
    public static List<Level> getLevels() {
        return levels;
    }

    public static Level findLevel(int levelNumber) {
        if (levelNumber > LEVELS_COUNT)
            return null;

        return levels.get(levelNumber - 1);
    }
}
