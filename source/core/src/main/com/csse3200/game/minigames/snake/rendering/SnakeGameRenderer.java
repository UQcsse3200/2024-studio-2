package com.csse3200.game.minigames.snake.rendering;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.minigames.MinigameRenderer;
import com.csse3200.game.minigames.snake.AssetPaths;
import com.csse3200.game.minigames.snake.SnakeGame;
import com.csse3200.game.minigames.MiniGameNames;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.minigames.ScoreBoard;

import static com.csse3200.game.minigames.snake.AssetPaths.IMAGES;

/**
 * Renders all elements of the Snake mini-game, including the grid, apple, snake, and scoreboard.
 */
public class SnakeGameRenderer {

    private final ScoreBoard scoreBoard;
    private Texture appleTexture, snakeTexture, snakeBodyHorizontalTexture,
            snakeBodyVerticalTexture, snakeBodyBentTexture, grassTexture;

    private final MinigameRenderer renderer;

    /**
     * Initialises the SnakeGameRenderer and its sub-renderers.
     *
     * @param game The SnakeGame instance containing game state and logic.
     */
    public SnakeGameRenderer(SnakeGame game) {
        // Initialise the individual renderers
        this.renderer = new MinigameRenderer();
        ServiceLocator.registerResourceService(new ResourceService());
        loadAssets();
        renderer.addRenderable(new GridRenderer(game.getGrid(), grassTexture, renderer));
        renderer.addRenderable(new AppleRenderer(game.getApple(), game.getGrid(), appleTexture,
                renderer));
        renderer.addRenderable(new SnakeRenderer(game.getSnake(), game.getGrid(), snakeTexture,
                snakeBodyHorizontalTexture,
                snakeBodyVerticalTexture, snakeBodyBentTexture, renderer));
        this.scoreBoard = new ScoreBoard(0, MiniGameNames.SNAKE);
    }

    /**
     * Renders the entire snake game including grid, apple, snake, and scoreboard.
     *
     * @param score The current score to be displayed on the scoreboard.
     */
    public void render(int score) {
        renderer.render();
        scoreBoard.updateScore(score);
    }

    /**
     * Resizes the screen
     * @param width width of the screen
     * @param height height of the screen
     */
    public void resize(int width, int height) {
            renderer.resize(width, height);
            scoreBoard.resize();
    }

    /**
     * Loads the textures and other assets required for rendering.
     */
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
     * Unloads the assets used in the game to free resources.
     */
    private void unloadAssets() {

        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(IMAGES);
    }

    /**
     * Disposes of resources used by the renderer, including textures and the SpriteBatch.
     */
    public void dispose() {
        renderer.dispose();
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
