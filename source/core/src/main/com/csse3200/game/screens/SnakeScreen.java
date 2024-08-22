package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.minigame.snake.SnakeGrid;
import com.csse3200.game.components.minigame.Direction;
import com.csse3200.game.components.minigame.snake.Apple;
import com.csse3200.game.components.minigame.snake.Snake;

/**
 * Represents the screen for the Snake game.
 * Handles the rendering of the game components.
 */
public class SnakeScreen extends ScreenAdapter {
    private final int CELL_SIZE = 55;
    private final GdxGame game;
    private final SnakeGrid grid;
    private final Apple apple;
    private final Snake snake;
    private ShapeRenderer shapeRenderer;

    /**
     * Initialises the SnakeScreen with the provided game instance.
     *
     * @param game The main game instance that controls the screen.
     */
    public SnakeScreen(GdxGame game) {
        this.game = game;
        this.grid = new SnakeGrid();
        this.apple = new Apple(grid);
        this.snake = new Snake(grid, 0, 0, Direction.RIGHT, 1, 0.5f);
        this.shapeRenderer = new ShapeRenderer();
    }

    /**
     * Renders the Snake game screen, including the grid and the apple.
     *
     * This method is made for future use, so it will have a lot to change probably
     *
     * @param delta Time in seconds since the last frame.
     */
    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render the grid and the apple
        snake.update(delta);
        renderGrid();
        renderApple();
        renderHead();
    }

    /**
     * Renders the game grid on the screen.
     * The grid is centered and each cell is drawn with a white outline.
     */
    private void renderGrid() {
        int gridWidthInPixels = grid.getWidth() * CELL_SIZE;
        int gridHeightInPixels = grid.getHeight() * CELL_SIZE;

        // Calculate the offset to center the grid
        float offsetX = (Gdx.graphics.getWidth() - gridWidthInPixels) / 2f;
        float offsetY = (Gdx.graphics.getHeight() - gridHeightInPixels) / 2f;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);

        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                shapeRenderer.rect(offsetX + x * CELL_SIZE, offsetY + y * CELL_SIZE, CELL_SIZE,
                        CELL_SIZE);
            }
        }

        shapeRenderer.end();
    }

    /**
     * Renders the apple on the grid.
     * The apple is displayed as a filled red square on the grid.
     */
    private void renderApple() {

        int gridWidthInPixels = grid.getWidth() * CELL_SIZE;
        int gridHeightInPixels = grid.getHeight() * CELL_SIZE;

        // Calculate the offset to center the grid
        float offsetX = (Gdx.graphics.getWidth() - gridWidthInPixels) / 2f;
        float offsetY = (Gdx.graphics.getHeight() - gridHeightInPixels) / 2f;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);

        shapeRenderer.rect(offsetX + apple.getX() * CELL_SIZE, offsetY + apple.getY() * CELL_SIZE, CELL_SIZE, CELL_SIZE);

        shapeRenderer.end();
    }

    /**
     * Renders the snake head on the grid.
     * The snake head is displayed as a filled green square on the grid.
     */
    private void renderHead() {

        int gridWidthInPixels = grid.getWidth() * CELL_SIZE;
        int gridHeightInPixels = grid.getHeight() * CELL_SIZE;

        // Calculate the offset to center the grid
        float offsetX = (Gdx.graphics.getWidth() - gridWidthInPixels) / 2f;
        float offsetY = (Gdx.graphics.getHeight() - gridHeightInPixels) / 2f;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GREEN);

        shapeRenderer.rect(offsetX + snake.getX() * CELL_SIZE, offsetY + snake.getY() * CELL_SIZE, CELL_SIZE, CELL_SIZE);

        shapeRenderer.end();
    }

    /**
     * Disposes of resources used by the SnakeScreen.
     * This includes the ShapeRenderer used for rendering the grid and apple.
     */
    @Override
    public void dispose() {
        shapeRenderer.dispose();
        // Dispose of other resources if necessary
    }
}
