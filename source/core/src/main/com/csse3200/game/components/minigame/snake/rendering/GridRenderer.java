package com.csse3200.game.components.minigame.snake.rendering;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.csse3200.game.components.minigame.snake.SnakeGrid;

/**
 * Renders the grid for the Snake mini-game.
 */
public class GridRenderer {

    private static final int CELL_SIZE = 55;
    private final SnakeGrid grid;
    private final Texture grassTexture;
    private final SpriteBatch spriteBatch;

    /**
     * Creates a new GridRenderer.
     *
     * @param grid The grid to render.
     * @param grassTexture The texture to use for rendering the grid cells.
     * @param spriteBatch The SpriteBatch used for drawing.
     */
    public GridRenderer(SnakeGrid grid, Texture grassTexture, SpriteBatch spriteBatch) {
        this.grid = grid;
        this.grassTexture = grassTexture;
        this.spriteBatch = spriteBatch;
    }

    /**
     * Renders the grid background.
     */
    public void renderGrid() {
        int gridWidthInPixels = grid.getWidth() * CELL_SIZE;
        int gridHeightInPixels = grid.getHeight() * CELL_SIZE;

        float offsetX = (Gdx.graphics.getWidth() - gridWidthInPixels) / 2f;
        float offsetY = (Gdx.graphics.getHeight() - gridHeightInPixels) / 2f;

        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                spriteBatch.draw(grassTexture,
                        offsetX + x * CELL_SIZE,
                        offsetY + y * CELL_SIZE,
                        CELL_SIZE,
                        CELL_SIZE);
            }
        }
    }
}
