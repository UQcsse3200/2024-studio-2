package com.csse3200.game.components.minigame.snake.rendering;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.csse3200.game.components.minigame.snake.SnakeGame;


public class SnakeGameRenderer {

    private final GridRenderer gridRenderer;
    private final AppleRenderer appleRenderer;
    private final SnakeRenderer snakeRenderer;
    private final SpriteBatch spriteBatch;

    public SnakeGameRenderer(SnakeGame game,
                             Texture grassTexture, Texture appleTexture, Texture snakeTexture,
                             Texture snakeBodyHorizontalTexture, Texture snakeBodyVerticalTexture,
                             Texture snakeBodyBentTexture) {
        // Initialise the individual renderers
        this.spriteBatch = new SpriteBatch();
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
        spriteBatch.begin();
        gridRenderer.renderGrid();
        appleRenderer.renderApple();
        snakeRenderer.renderSnake();
        spriteBatch.end();
    }

    public void dispose() {
        spriteBatch.dispose();
    }
}
