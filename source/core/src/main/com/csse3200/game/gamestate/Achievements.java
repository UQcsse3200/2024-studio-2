package com.csse3200.game.gamestate;

import com.csse3200.game.files.FileLoader;
import com.csse3200.game.gamestate.data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data struct that contains other data structs to be saved to JSONs by SaveHandler.
 * Fields MUST be static.
 * To be kept between playthroughs.
 */
public class Achievements {
    private Achievements() {}

    public static AchievementSave achievements = new AchievementSave();

    /**
     * Clears the contents of the GameState.
     */
    public static void resetState() {
        SaveHandler.load(Achievements.class, "defaultsaves/achievement", FileLoader.Location.INTERNAL);
    }

    /**
     * Checks whether GameState correctly loaded.
     * @return a boolean to indicate whether GameState was correctly loaded.
     */

    public static boolean checkState() {
        return (achievements == null);
    }

    /**
     * Clears all the GameState contents and resets them to their original state.
     */
    public static void clearState() {
        achievements = new AchievementSave();
    }
}