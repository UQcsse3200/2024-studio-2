package com.csse3200.game.minigames.snake.rendering;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.minigames.MinigameRenderable;
import com.csse3200.game.minigames.MinigameRenderer;
import com.csse3200.game.minigames.snake.SnakeGrid;

/**
 * Renders the grid for the Snake mini-game.
 */
public class GridRenderer implements MinigameRenderable {

    private static final int CELL_SIZE = 20;
    private final SnakeGrid grid;
    private final Texture grassLightTexture;
    private final Texture grassDarkTexture;
    private final MinigameRenderer renderer;

    /**
     * Creates a new GridRenderer.
     *
     * @param grid The grid to render.
     * @param renderer The renderer used for drawing.
     */
    public GridRenderer(SnakeGrid grid, Texture grassLightTexture, Texture grassDarkTexture, MinigameRenderer renderer) {
        this.grid = grid;
        this.grassDarkTexture = grassDarkTexture;
        this.grassLightTexture = grassLightTexture;
        this.renderer = renderer;

    }

    /**
     * Renders the grid background.
     */
    public void render() {
        // Calculate the total size of the grid in world units
        float gridWidthInWorldUnits = grid.getWidth() * CELL_SIZE;
        float gridHeightInWorldUnits = grid.getHeight() * CELL_SIZE;

        // Calculate the top-left corner of the grid so that it's centered in the camera's view
        float startX = renderer.getCam().position.x - gridWidthInWorldUnits / 2f;
        float startY = renderer.getCam().position.y - gridHeightInWorldUnits / 2f;

        // Render the grid based on the camera's position
        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                if (x % 2 == 1){
                    if (y % 2 ==1){
                        renderer.getSb().draw(grassLightTexture, //light
                                startX + x * CELL_SIZE,
                                startY + y * CELL_SIZE,
                                CELL_SIZE,
                                CELL_SIZE);
                    }
                    if (y % 2 ==0){
                        renderer.getSb().draw(grassDarkTexture,
                                startX + x * CELL_SIZE,
                                startY + y * CELL_SIZE,
                                CELL_SIZE,
                                CELL_SIZE);
                    }
                }
                if (x % 2 == 0){
                    if (y % 2 ==1){
                        renderer.getSb().draw(grassDarkTexture,
                                startX + x * CELL_SIZE,
                                startY + y * CELL_SIZE,
                                CELL_SIZE,
                                CELL_SIZE);
                    }
                    if (y % 2 ==0){
                        renderer.getSb().draw(grassLightTexture, //Light
                                startX + x * CELL_SIZE,
                                startY + y * CELL_SIZE,
                                CELL_SIZE,
                                CELL_SIZE);
                    }
                }
            }
        }
    }
}
