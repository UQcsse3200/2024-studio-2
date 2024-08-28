package com.csse3200.game.components.minigame.snake.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.csse3200.game.components.minigame.snake.Apple;
import com.csse3200.game.components.minigame.snake.SnakeGrid;

/**
 * Renders the apple on the grid in the Snake mini-game.
 */
public class AppleRenderer {

    private static final int CELL_SIZE = 55;
    private final Apple apple;
    private final SnakeGrid grid;
    private final Texture appleTexture;
    private final SpriteBatch spriteBatch;

    /**
     * Creates a new AppleRenderer.
     *
     * @param apple The apple to render.
     * @param grid The grid the apple is on.
     * @param appleTexture The texture to use for rendering the apple.
     * @param spriteBatch The SpriteBatch used for drawing.
     */
    public AppleRenderer(Apple apple, SnakeGrid grid, Texture appleTexture, SpriteBatch spriteBatch) {
        this.apple = apple;
        this.grid = grid;
        this.appleTexture = appleTexture;
        this.spriteBatch = spriteBatch;
    }

    /**
     * Renders the apple on the grid.
     */
    public void renderApple() {
        int gridWidthInPixels = grid.getWidth() * CELL_SIZE;
        int gridHeightInPixels = grid.getHeight() * CELL_SIZE;

        float offsetX = (Gdx.graphics.getWidth() - gridWidthInPixels) / 2f;
        float offsetY = (Gdx.graphics.getHeight() - gridHeightInPixels) / 2f;

        spriteBatch.draw(appleTexture,
                offsetX + apple.getX() * CELL_SIZE,
                offsetY + apple.getY() * CELL_SIZE,
                CELL_SIZE,
                CELL_SIZE);
    }
}
