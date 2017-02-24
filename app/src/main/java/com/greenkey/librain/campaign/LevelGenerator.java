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
        levels.add(new Level(1, 1000,3, 3, new Rule[] {new Rule(2, ResourceType.EU)}));
        levels.add(new Level(2, 1000, 4, 4, new Rule[] {new Rule(2, ResourceType.EU)}));
        levels.add(new Level(3, 1000, 5, 5, new Rule[] {new Rule(2, ResourceType.EU)}));
        levels.add(new Level(4, 1000, 3, 3, new Rule[] {new Rule(3, ResourceType.EU)}));
        levels.add(new Level(5, 1000, 4, 4, new Rule[] {new Rule(3, ResourceType.EU)}));
        levels.add(new Level(6, 1000, 5, 5, new Rule[] {new Rule(3, ResourceType.EU)}));
        levels.add(new Level(7, 1000, 3, 3, new Rule[] {new Rule(1, ResourceType.EU),new Rule(1, ResourceType.GB)}));
        levels.add(new Level(8, 1000, 4, 4, new Rule[] {new Rule(1, ResourceType.EU),new Rule(1, ResourceType.GB)}));
        levels.add(new Level(9, 1000, 5, 5, new Rule[] {new Rule(1, ResourceType.EU),new Rule(1, ResourceType.GB)}));
        levels.add(new Level(10, 1000, 3, 3, new Rule[] {new Rule(2, ResourceType.EU),new Rule(1, ResourceType.GB)}));
        levels.add(new Level(11, 1000, 4, 4, new Rule[] {new Rule(2, ResourceType.EU),new Rule(1, ResourceType.GB)}));
        levels.add(new Level(12, 1000, 5, 5, new Rule[] {new Rule(2, ResourceType.EU),new Rule(1, ResourceType.GB)}));
        levels.add(new Level(13, 1000, 3, 3, new Rule[] {new Rule(4, ResourceType.EU)}));
        levels.add(new Level(14, 1000, 4, 4, new Rule[] {new Rule(4, ResourceType.EU)}));
        levels.add(new Level(15, 1000, 5, 5, new Rule[] {new Rule(4, ResourceType.EU)}));
        levels.add(new Level(16, 1000, 3, 3, new Rule[] {new Rule(2, ResourceType.EU),new Rule(2, ResourceType.GB)}));
        levels.add(new Level(17, 1000, 4, 4, new Rule[] {new Rule(2, ResourceType.EU),new Rule(2, ResourceType.GB)}));
        levels.add(new Level(18, 1000, 5, 5, new Rule[] {new Rule(2, ResourceType.EU),new Rule(2, ResourceType.GB)}));
        levels.add(new Level(19, 1000, 3, 3, new Rule[] {new Rule(1, ResourceType.EU),new Rule(1, ResourceType.GB),new Rule(1, ResourceType.EN)}));
        levels.add(new Level(20, 1000, 4, 4, new Rule[] {new Rule(1, ResourceType.EU),new Rule(1, ResourceType.GB),new Rule(1, ResourceType.EN)}));
        levels.add(new Level(21, 1000, 5, 5, new Rule[] {new Rule(1, ResourceType.EU),new Rule(1, ResourceType.GB),new Rule(1, ResourceType.EN)}));
        levels.add(new Level(22, 1000, 3, 3, new Rule[] {new Rule(2, ResourceType.EU),new Rule(1, ResourceType.GB),new Rule(1, ResourceType.EN)}));
        levels.add(new Level(23, 1000, 4, 4, new Rule[] {new Rule(2, ResourceType.EU),new Rule(1, ResourceType.GB),new Rule(1, ResourceType.EN)}));
        levels.add(new Level(24, 1000, 5, 5, new Rule[] {new Rule(2, ResourceType.EU),new Rule(1, ResourceType.GB),new Rule(1, ResourceType.EN)}));
        levels.add(new Level(25, 1000, 3, 3, new Rule[] {new Rule(3, ResourceType.EU),new Rule(2, ResourceType.GB)}));
        levels.add(new Level(26, 1000, 4, 4, new Rule[] {new Rule(3, ResourceType.EU),new Rule(2, ResourceType.GB)}));
        levels.add(new Level(27, 1000, 5, 5, new Rule[] {new Rule(3, ResourceType.EU),new Rule(2, ResourceType.GB)}));
        levels.add(new Level(28, 1000, 3, 3, new Rule[] {new Rule(2, ResourceType.EU),new Rule(2, ResourceType.GB),new Rule(1, ResourceType.EN)}));
        levels.add(new Level(29, 1000, 4, 4, new Rule[] {new Rule(2, ResourceType.EU),new Rule(2, ResourceType.GB),new Rule(1, ResourceType.EN)}));
        levels.add(new Level(30, 1000, 5, 5, new Rule[] {new Rule(2, ResourceType.EU),new Rule(2, ResourceType.GB),new Rule(1, ResourceType.EN)}));
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
