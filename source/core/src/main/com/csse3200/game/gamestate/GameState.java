package com.csse3200.game.gamestate;

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
     * Clears the contents of the GameState
     */
    public static void resetState() {
        SaveHandler.load(GameState.class, "defaultsaves");
    }

    public static boolean checkState() {
        return (quests == null || inventory == null || player == null
                || stats == null || minigame == null);
    }
}
