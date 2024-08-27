package com.csse3200.game.components.minigame.snake.rendering;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.csse3200.game.components.minigame.snake.SnakeGame;


public class SnakeGameRenderer {

    private final GridRenderer gridRenderer;
    private final AppleRenderer appleRenderer;
    private final SnakeRenderer snakeRenderer;

    public SnakeGameRenderer(SnakeGame game,
                             Texture grassTexture, Texture appleTexture, Texture snakeTexture,
                             Texture snakeBodyHorizontalTexture, Texture snakeBodyVerticalTexture,
                             Texture snakeBodyBentTexture, SpriteBatch spriteBatch) {
        // Initialise the individual renderers
        this.gridRenderer = new GridRenderer(game.getGrid(), grassTexture, spriteBatch);
        this.appleRenderer = new AppleRenderer(game.getApple(), game.getGrid(), appleTexture, spriteBatch);
        this.snakeRenderer = new SnakeRenderer(game.getSnake(), game.getGrid(), snakeTexture,
                snakeBodyHorizontalTexture,
                snakeBodyVerticalTexture, snakeBodyBentTexture, spriteBatch);
    }

    /**
     * Renders the entire snake game including grid, apple, and snake.
     */
    public void render() {
        gridRenderer.renderGrid();
        appleRenderer.renderApple();
        snakeRenderer.renderSnake();
    }
}
