package com.greenkeycompany.librain.level;

import java.util.ArrayList;

/**
 * Created by Alexander on 13.03.2017.
 */

public class LevelsPage {

    private final Level.LevelType levelType;
    private final ArrayList<Level> levels;

    public LevelsPage(Level.LevelType levelType, ArrayList<Level> levels) {
        this.levelType = levelType;
        this.levels = levels;
    }

    public Level.LevelType getLevelType() {
        return levelType;
    }

    public ArrayList<Level> getLevels() {
        return levels;
    }
}
