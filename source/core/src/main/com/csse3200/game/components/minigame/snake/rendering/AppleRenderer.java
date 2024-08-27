package com.csse3200.game.components.minigame.snake.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.csse3200.game.components.minigame.snake.Apple;
import com.csse3200.game.components.minigame.snake.SnakeGrid;

public class AppleRenderer {

    private static final int CELL_SIZE = 55;
    private final Apple apple;
    private final SnakeGrid grid;
    private final Texture appleTexture;
    private final SpriteBatch spriteBatch;

    public AppleRenderer(Apple apple, SnakeGrid grid, Texture appleTexture, SpriteBatch spriteBatch) {
        this.apple = apple;
        this.grid = grid;
        this.appleTexture = appleTexture;
        this.spriteBatch = spriteBatch;
    }

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
