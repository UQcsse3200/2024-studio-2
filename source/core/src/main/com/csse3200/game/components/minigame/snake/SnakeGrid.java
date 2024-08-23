package com.csse3200.game.components.minigame.snake;

import com.csse3200.game.components.minigame.Grid;

/**
 * Represents a specialised grid for the Snake game.
 * The SnakeGrid has default dimensions and includes methods for snake-specific functionality.
 */
public class SnakeGrid extends Grid {

    private static final int DEFAULT_WIDTH = 20;
    private static final int DEFAULT_HEIGHT = 20;

    /**
     * Creates a SnakeGrid with default dimensions.
     * The grid is initialized to a size of 20x20 cells.
     */
    public SnakeGrid() {
        super(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    // We can add snake-specific methods here if needed.
    // No thar should be in snake Game
    public boolean isCollision(int x, int y) {
        // Check if a cell is occupied (could represent the snake's body)
        return isOccupied(x, y);
    }

}
