package com.csse3200.game.components.minigame.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
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

    public Vector2 tileToWorldPosition(Vector2 gridPosition, int cellSize) {
        int gridWidthInPixels = getWidth() * cellSize;
        int gridHeightInPixels = getHeight() * cellSize;

        // Calculate the offset to center the grid
        float offsetX = (Gdx.graphics.getWidth() - gridWidthInPixels) / 2f;
        float offsetY = (Gdx.graphics.getHeight() - gridHeightInPixels) / 2f;

        // Calculate world position
        float worldX = offsetX + gridPosition.x * cellSize;
        float worldY = offsetY + gridPosition.y * cellSize;

        return new Vector2(worldX, worldY);
    }


}
