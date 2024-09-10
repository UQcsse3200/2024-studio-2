package com.csse3200.game.gamestate;

import com.csse3200.game.gamestate.data.InventorySave;
import com.csse3200.game.gamestate.data.QuestSave;

/**
 * A data struct that contains other data structs to be saved to JSONs by SaveHandler.
 * Fields MUST be static.
 * Not to be kept between playthroughs.
 */
public class GameState {
    private GameState() {}

    public static QuestSave quests = new QuestSave();

    public static InventorySave inventory = new InventorySave();
}
