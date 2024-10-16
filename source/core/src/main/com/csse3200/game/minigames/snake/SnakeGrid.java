package com.csse3200.game.minigames.snake;

import com.csse3200.game.minigames.Grid;

/**
 * Represents a specialised grid for the Snake game.
 * The SnakeGrid has default dimensions and includes methods for snake-specific functionality.
 */
public class SnakeGrid extends Grid {

    private static final int DEFAULT_WIDTH = 20;
    private static final int DEFAULT_HEIGHT = 20;

    /**
     * Creates a SnakeGrid with default dimensions.
     * The grid is initialised to a size of 20x20 cells.
     */
    public SnakeGrid() {
        super(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
}
