package com.csse3200.game.components.minigame.snake.rendering;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.csse3200.game.components.minigame.snake.SnakeGame;
import com.csse3200.game.ui.minigame.SnakeScoreBoard;


public class SnakeGameRenderer {

    private final GridRenderer gridRenderer;
    private final AppleRenderer appleRenderer;
    private final SnakeRenderer snakeRenderer;
    private final SpriteBatch spriteBatch;
    private final SnakeScoreBoard scoreBoard;

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
        this.scoreBoard = new SnakeScoreBoard(0);
    }

    /**
     * Renders the entire snake game including grid, apple, and snake.
     */
    public void render(int score) {
        spriteBatch.begin();
        gridRenderer.renderGrid();
        appleRenderer.renderApple();
        snakeRenderer.renderSnake();
        spriteBatch.end();
        scoreBoard.updateScore(score);
    }

    public void dispose() {
        spriteBatch.dispose();
    }
}
