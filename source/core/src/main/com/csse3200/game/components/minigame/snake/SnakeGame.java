package com.csse3200.game.components.minigame.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.csse3200.game.components.minigame.Direction;
import com.csse3200.game.components.minigame.snake.Snake;
import com.csse3200.game.components.minigame.snake.Apple;

import java.util.List;

public class SnakeGame {
    private final Snake snake;
    private final Apple apple;
    private SnakeGrid grid;
    private int score;
        // to-do add grid
    public SnakeGame(Snake snake, Apple apple, SnakeGrid snakeGrid) {
        this.snake = snake;
        this.apple = apple;
        this.grid = snakeGrid;
        this.score = 0;
    }

    public int getScore() {
        return score;
    }

    public void attemptEatFruit() {
        if (apple.isTouchingSnakeHead(snake)) {
            apple.spawn();
            snake.grow();
            score += calculateScore();
        }
    }
    private int calculateScore() {
        return 1;
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


    /**
     * Determines if the snake has run into itself
     * @return true if the head of the snake has run into the body, otherwise false
     */
    public boolean snakeCollisionDetection() {

        int snakeHeadX = snake.getX();
        int snakeHeadY = snake.getY();
        System.out.println("got here");
        List<Snake.Segment> snakeSegs = snake.getBodySegments();
        for (int i = 0; i <= snakeSegs.size() - 1; i += 1) {
             if (snakeSegs.get(i).getX() == snakeHeadX && snakeSegs.get(i).getY() == snakeHeadY) {
                return true;
             }
        }
        return false;
    }
}
