package com.csse3200.game.minigames.snake.rendering;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.minigames.MinigameRenderable;
import com.csse3200.game.minigames.MinigameRenderer;
import com.csse3200.game.minigames.snake.Apple;
import com.csse3200.game.minigames.snake.SnakeGrid;

/**
 * Renders the apple on the grid in the Snake mini-game.
 */
public class AppleRenderer implements MinigameRenderable {

    private static final int CELL_SIZE = 20;
    private final Apple apple;
    private final SnakeGrid grid;
    private final Texture appleTexture;
    private final MinigameRenderer renderer;

    /**
     * Creates a new AppleRenderer.
     *
     * @param apple The apple to render.
     * @param grid The grid the apple is on.
     * @param appleTexture The texture to use for rendering the apple.
     * @param renderer The renderer used for drawing.
     */
    public AppleRenderer(Apple apple, SnakeGrid grid, Texture appleTexture,
                         MinigameRenderer renderer) {
        this.apple = apple;
        this.grid = grid;
        this.appleTexture = appleTexture;
        this.renderer = renderer;
    }

    /**
     * Renders the apple on the grid.
     */
    public void render() {

        // Calculate the top-left corner of the grid, centered in the camera's view
        float gridWidthInWorldUnits = grid.getWidth() * CELL_SIZE;
        float gridHeightInWorldUnits = grid.getHeight() * CELL_SIZE;

        // Determine where the grid starts in the camera's world coordinates
        float startX = renderer.getCam().position.x - gridWidthInWorldUnits / 2f;
        float startY = renderer.getCam().position.y - gridHeightInWorldUnits / 2f;

        // Render the apple relative to the grid's starting position
        renderer.getSb().draw(appleTexture,
                startX + apple.getX() * CELL_SIZE,
                startY + apple.getY() * CELL_SIZE,
                CELL_SIZE,
                CELL_SIZE);
    }

}
