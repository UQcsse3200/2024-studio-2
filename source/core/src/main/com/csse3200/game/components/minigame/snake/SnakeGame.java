package com.csse3200.game.components.minigame.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.csse3200.game.components.minigame.Direction;
import com.csse3200.game.components.minigame.snake.Snake;
import com.csse3200.game.components.minigame.snake.Apple;

public class SnakeGame {
    private final Snake snake;
    private final Apple apple;

    private SnakeGrid grid;

        // TODO: add grid
    public SnakeGame(Snake snake, Apple apple, SnakeGrid snakeGrid) {
        this.snake = snake;
        this.apple = apple;
        this.grid = snakeGrid;
    }
    
    public void attemptEatFruit() {
        if (apple.isTouchingSnakeHead(snake)) {
            apple.spawn();
            snake.grow();
        }
    }

    /**
     * Detects if the snake it at the boundary of the grid
     *
     * @return 1 if it is, 0 otherwise
     */
    public boolean boundaryDetection() {
        int snakeX = snake.getX();
        int snakeY = snake.getY();
        int grid_height = this.grid.getHeight();
        int grid_width = this.grid.getWidth();
        if (snakeY < 0 || snakeY > (grid_height -1)) {

            return true;
        }
        if (snakeX < 0 || snakeX > (grid_width -1)) {
            return true;
        }
        return false;
    }
}
