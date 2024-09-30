package com.csse3200.game.gamestate;

import com.csse3200.game.gamestate.data.InventorySave;
import com.csse3200.game.gamestate.data.PlayerSave;
import com.csse3200.game.gamestate.data.QuestSave;
import com.csse3200.game.gamestate.data.StatSave;
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

    /**
     * Clears the contents of the GameState
     */
    public static void resetState() {
        Logger logger = LoggerFactory.getLogger(GameState.class);
        logger.info("state is reset");
        SaveHandler.load(GameState.class, "defaultsaves");
    }

    public static boolean checkState() {
        Logger logger = LoggerFactory.getLogger(GameState.class);
        logger.info("states: {} {} {} {}", quests, inventory, player, stats);
        return (quests == null || inventory == null || player == null || stats == null);
    }

    /**
     * Clears all the GameState contents and resets them to their original state.
     */
    public static void clearState() {
        quests = new QuestSave();

        inventory = new InventorySave();

        player = new PlayerSave();

        stats = new StatSave();
    }
}
