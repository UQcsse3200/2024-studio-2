package com.csse3200.game.minigames;

import com.csse3200.game.minigames.maze.areas.MazeGameArea;

/**
 * Class to hold all medal constants for all Mini-games. Used for scoring and rewards.
 */
public final class MiniGameConstants {

    private MiniGameConstants() {

        throw new AssertionError("Do not initiate MiniGameConstants Class");
    }

    /**
     * Snake game GOLD, SILVER and BRONZE score thresholds
     */
    public static final int SNAKE_BRONZE_THRESHOLD = 15;
    public static final int SNAKE_SILVER_THRESHOLD = 30;
    public static final int SNAKE_GOLD_THRESHOLD = 45;

    /**
     * Birdie Dash game GOLD, SILVER and BRONZE score thresholds
     */
    public static final int BIRDIE_DASH_GOLD_THRESHOLD = 45;
    public static final int BIRDIE_DASH_SILVER_THRESHOLD = 30;
    public static final int BIRDIE_DASH_BRONZE_THRESHOLD = 15;

    /**
     * Maze game GOLD, SILVER and BRONZE score thresholds
     */
    public static final int MAZE_GOLD_THRESHOLD = MazeGameArea.NUM_EGGS;
    public static final int MAZE_SILVER_THRESHOLD = MazeGameArea.NUM_EGGS / 4 * 3;
    public static final int MAZE_BRONZE_THRESHOLD = MazeGameArea.NUM_EGGS / 2;
}
