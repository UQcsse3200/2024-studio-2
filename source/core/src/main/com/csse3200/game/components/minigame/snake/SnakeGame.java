package com.csse3200.game.components.minigame.snake;

import com.csse3200.game.components.minigame.Direction;
import com.csse3200.game.components.minigame.snake.controller.Events;
import com.csse3200.game.components.minigame.snake.controller.SnakeController;
import com.csse3200.game.components.minigame.snake.rendering.SnakeGameRenderer;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.badlogic.gdx.graphics.Texture;

import java.util.List;

/**
 * A controller class for the logic of the Snake mini-game.
 */
public class SnakeGame {
    private final Snake snake;
    private final Apple apple;
    private final SnakeGrid grid;
    private final SnakeController snakeController;
    private final SnakeGameRenderer renderer;
    private int score;
    private Boolean isGameOver;
    private Texture appleTexture, snakeTexture, snakeBodyHorizontalTexture,
            snakeBodyVerticalTexture, snakeBodyBentTexture, grassTexture;

    public SnakeGame() {
        this.grid = new SnakeGrid();
        this.snakeController = new SnakeController();
        this.snake = new Snake(this.grid, 0, 0, Direction.RIGHT, 2, 1f / 6);
        this.apple = new Apple(this.grid);
        this.score = 0;
        this.isGameOver = false;

        ResourceService resourceService = ServiceLocator.getResourceService();
        grassTexture = resourceService.getAsset(AssetPaths.GRASS_IMAGE, Texture.class);
        appleTexture = resourceService.getAsset(AssetPaths.APPLE_IMAGE, Texture.class);
        snakeTexture = resourceService.getAsset(AssetPaths.SNAKE_HEAD_IMAGE, Texture.class);
        snakeBodyHorizontalTexture = resourceService.getAsset(AssetPaths.SNAKE_BODY_HORIZONTAL_IMAGE, Texture.class);
        snakeBodyVerticalTexture = resourceService.getAsset(AssetPaths.SNAKE_BODY_VERTICAL_IMAGE, Texture.class);
        snakeBodyBentTexture = resourceService.getAsset(AssetPaths.SNAKE_BODY_BENT_IMAGE, Texture.class);

        this.renderer = new SnakeGameRenderer(this, grassTexture,
                appleTexture,
                snakeTexture,
                snakeBodyHorizontalTexture,
                snakeBodyVerticalTexture,
                snakeBodyBentTexture);
    }

    public Snake getSnake() {
        return this.snake;
    }

    public Apple getApple() {
        return this.apple;
    }

    public SnakeGrid getGrid() {
        return this.grid;
    }

    /**
     * Getter for the isGameOver variable
     * @return isGameOver, is true if over, else false
     */
    public boolean getIsGameOver() {
        return isGameOver;
    }

    /**
     * Set the isGameOver function. Is true if over, else false
     */
    public void setIsGameOver() {
        this.isGameOver = true;
    }

    /**
     * Returns the current score of the game.
     * @return the current score of the game.
     */
    public int getScore() {
        return score;
    }

    /**
     * Attempts to eat the apple in the snake game if the snake has reached it.
     * If the snake can eat the apple then a new apple will spawn, the snake will grow
     * and the score will increase.
     */
    public void attemptEatFruit() {
        if (apple.isTouchingSnakeHead(snake)) {
            apple.spawn();
            snake.grow();
            score += 1;
        }
    }

    public void snakeMove(float delta) {
        snake.updateDirectionOnInput(snakeController.getInputDirection());
        attemptEatFruit();
        this.snake.update(delta);
        if(boundaryDetection() || snakeCollisionDetection()) {
            setIsGameOver();
        }
    }

    /**
     * Detects if the snake it at the boundary of the grid
     * @return true if it is, false otherwise
     */
    public boolean boundaryDetection() {
        int snakeX = snake.getX();
        int snakeY = snake.getY();
        int gridHeight = this.grid.getHeight();
        int gridWidth = this.grid.getWidth();

        return (snakeY < 0 || snakeY > (gridHeight -1) || (snakeX < 0 || snakeX > (gridWidth -1)));
    }

    /**
     * Determines if the snake has run into itself
     * @return true if the head of the snake has run into the body, otherwise false
     */
    public boolean snakeCollisionDetection() {

        int snakeHeadX = snake.getX();
        int snakeHeadY = snake.getY();
        List<Snake.Segment> snakeSegs = snake.getBodySegments();
        for (int i = 0; i <= snakeSegs.size() - 1; i += 1) {
             if (snakeSegs.get(i).x() == snakeHeadX && snakeSegs.get(i).y() == snakeHeadY) {
                return true;
             }
        }
        return false;
    }

    public Events handleInput() {
        return snakeController.handleInput();
    }

    public void render(int score) {
        renderer.render(score);
    }

    public void dispose() {
        renderer.dispose();
        appleTexture.dispose();
        snakeTexture.dispose();
        snakeBodyHorizontalTexture.dispose();
        snakeBodyVerticalTexture.dispose();
        snakeBodyBentTexture.dispose();
        grassTexture.dispose();
    }
}
