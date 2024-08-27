package com.csse3200.game.components.minigame.snake;

import java.util.List;

/**
 * A controller class for the logic of the Snake mini-game.
 */
public class SnakeGame {
    private final Snake snake;
    private final Apple apple;
    private final SnakeGrid grid;
    private int score;
    private Boolean isGameOver;

    public SnakeGame(Snake snake, Apple apple, SnakeGrid snakeGrid) {
        this.snake = snake;
        this.apple = apple;
        this.grid = snakeGrid;
        this.score = 0;
        this.isGameOver = false;
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
     * @param state: the state to set the isGameOver to
     */
    public void setIsGameOver(boolean state) {
        this.isGameOver = state;
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
}
