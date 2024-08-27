package com.csse3200.game.components.minigame.snake.rendering;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.csse3200.game.components.minigame.snake.AssetPaths;
import com.csse3200.game.components.minigame.snake.SnakeGame;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.minigame.SnakeScoreBoard;

import static com.csse3200.game.components.minigame.snake.AssetPaths.IMAGES;


public class SnakeGameRenderer {

    private final GridRenderer gridRenderer;
    private final AppleRenderer appleRenderer;
    private final SnakeRenderer snakeRenderer;
    private final SpriteBatch spriteBatch;
    private final SnakeScoreBoard scoreBoard;
    private Texture appleTexture, snakeTexture, snakeBodyHorizontalTexture,
            snakeBodyVerticalTexture, snakeBodyBentTexture, grassTexture;


    public SnakeGameRenderer(SnakeGame game) {
        // Initialise the individual renderers
        this.spriteBatch = new SpriteBatch();
        ServiceLocator.registerResourceService(new ResourceService());
        loadAssets();
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

    private void loadAssets() {

        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(AssetPaths.IMAGES);
        ServiceLocator.getResourceService().loadAll();
        grassTexture = resourceService.getAsset(AssetPaths.GRASS_IMAGE, Texture.class);
        appleTexture = resourceService.getAsset(AssetPaths.APPLE_IMAGE, Texture.class);
        snakeTexture = resourceService.getAsset(AssetPaths.SNAKE_HEAD_IMAGE, Texture.class);
        snakeBodyHorizontalTexture = resourceService.getAsset(AssetPaths.SNAKE_BODY_HORIZONTAL_IMAGE, Texture.class);
        snakeBodyVerticalTexture = resourceService.getAsset(AssetPaths.SNAKE_BODY_VERTICAL_IMAGE, Texture.class);
        snakeBodyBentTexture = resourceService.getAsset(AssetPaths.SNAKE_BODY_BENT_IMAGE, Texture.class);

    }

    /**
     * Unloads assests for the game
     */
    private void unloadAssets() {

        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(IMAGES);
    }

    public void dispose() {
        spriteBatch.dispose();
        appleTexture.dispose();
        snakeTexture.dispose();
        snakeBodyHorizontalTexture.dispose();
        snakeBodyVerticalTexture.dispose();
        snakeBodyBentTexture.dispose();
        grassTexture.dispose();
        unloadAssets();
        ServiceLocator.getResourceService().dispose();
    }
}
