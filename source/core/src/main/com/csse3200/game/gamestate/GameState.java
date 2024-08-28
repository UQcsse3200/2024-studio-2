package com.csse3200.game.gamestate;

import com.csse3200.game.gamestate.data.Quests;

/**
 * A data struct that contains other data structs to be saved to JSONs by SaveHandler.
 * Fields MUST be static.
 * Not to be kept between playthroughs.
 */
public class GameState {
    private GameState() {}

    public static Quests quests = new Quests();
}
