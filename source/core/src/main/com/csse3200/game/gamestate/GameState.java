package com.csse3200.game.gamestate;

import com.csse3200.game.files.FileLoader;

import com.csse3200.game.gamestate.data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data struct that contains other data structs to be saved to JSONs by SaveHandler.
 * Fields MUST be static.
 * Not to be kept between playthroughs.
 */
public class GameState {
    private GameState() {}

    public static QuestSave quests = new QuestSave();

    public static InventorySave inventory = new InventorySave();

    public static PlayerSave player = new PlayerSave();

    public static StatSave stats = new StatSave();

    public static MinigameHighscore minigame = new MinigameHighscore();

    /**
     * Clears the contents of the GameState and replaces them with the default saves.
     */
    public static void resetState() {
        Logger logger = LoggerFactory.getLogger(GameState.class);
        logger.info("state is reset");
        SaveHandler.getInstance().load(GameState.class, "defaultsaves", FileLoader.Location.INTERNAL);
    }

    /**
     * Checks whether GameState correctly loaded.
     * @return a boolean to indicate whether GameState was correctly loaded.
     * Returns true if the load failed, and false otherwise.
     */

    public static boolean checkState() {
        return (quests == null || inventory == null || player == null
                || stats == null || minigame == null);
    }

    /**
     * Clears all the GameState contents and resets them to their empty state.
     */
    public static void clearState() {
        quests = new QuestSave();

        inventory = new InventorySave();

        player = new PlayerSave();

        stats = new StatSave();
    }
}
