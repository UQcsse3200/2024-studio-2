package com.csse3200.game.minigames.snake;

import com.csse3200.game.minigames.Direction;
import com.csse3200.game.services.AudioManager;

import java.util.List;

/**
 * Manages the logic and state of the Snake mini-game, including snake movement, scoring,  and
 * game-over conditions.
 */
public class SnakeGame {
    private final Snake snake;
    private final Apple apple;
    private final SnakeGrid grid;
    private int score;
    private Boolean isGameOver;

    /**
     * Initialises a new SnakeGame with a grid, snake, apple, and controller.
     */
    public SnakeGame() {
        this.grid = new SnakeGrid();
        this.snake = new Snake(this.grid, 0, 0, Direction.RIGHT, 2, 1f / 6);
        this.apple = new Apple(this.grid);
        this.score = 0;
        this.isGameOver = false;
    }

    /**
     * methods to return snake object
     * @return the snake
     */
    public Snake getSnake() {
        return this.snake;
    }

    /**
     * Method to return apple object
     * @return the apple
     */
    public Apple getApple() {
        return this.apple;
    }

    /**
     * Methods to return grid object
     * @return the grid
     */
    public SnakeGrid getGrid() {
        return this.grid;
    }

    public boolean getIsGameOver() {
        return isGameOver;
    }

    public void setIsGameOver() {
        this.isGameOver = true;
    }

    public int getScore() {
        return score;
    }

    /**
     * Attempts to eat the apple in the snake game if the snake has reached it.
     * If the snake can eat the apple then a new apple will spawn, the snake will grow
     * and the score will increase.
     */
    public boolean attemptEatFruit() {
        if (apple.isTouchingSnakeHead(snake)) {
            apple.spawn();
            snake.grow();
            score += 1;

            increaseSnakeSpeed();
            return true;
        }
        return false;
    }

    /**
     * Update snake direction based on an input direction.
     * If the input direction is reversed 180 degrees from the current
     * direction of the snake, it will not change.
     *
     * @param direction the direction to update to
     */
    public void handleSnakeInput(Direction direction) {
        snake.updateDirectionOnInput(direction);
    }

    /**
     * Moves the snake and checks for game-over conditions.
     *
     * @param delta The time elapsed since the last update.
     */
    public void snakeMove(float delta) {
        if (attemptEatFruit()) {
            AudioManager.playSound("sounds/minigames/snake-apple.mp3");
        }
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

    public void increaseSnakeSpeed() {
        if (this.score > 0 && (this.score % 5 == 0)) {
            snake.updateMovePeriod(snake.getMovePeriod() - 1f/60);
        }
    }
}
