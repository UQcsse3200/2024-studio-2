package com.csse3200.game.components.minigame.snake;

import com.csse3200.game.components.minigame.*;
import com.csse3200.game.screens.SnakeScreen;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

public class SnakeGameTest {

    Snake snake;
    Apple apple;
    SnakeGrid grid;
    SnakeGame snakeGame;
    // private static final Logger logger = LoggerFactory.getLogger(SnakeGameTest.class);

    @Before
    public void setUp() {
        this.grid = new SnakeGrid();
        this.apple = new Apple(grid);
        this.snake = new Snake(grid, 0, 0, Direction.RIGHT, 2, 1f / 10);
    }

    @Test
    public void testInitialScoreSnakeLength() {
        this.snakeGame = new SnakeGame(snake, apple, grid);
        assertTrue(snakeGame.getScore() == 0);
        assertTrue(snake.getBodySegments().size() == 0);
        assertTrue(snake.getLength() == 2);
        assertTrue(snake.getBodySegments().size() == snakeGame.getScore());
    }

    @Test
    public void testScoreSnakeLengthIncrease() {
        this.snakeGame = new SnakeGame(snake, apple, grid);
        apple.setAppleLocation(snake.getX(), snake.getY());
        apple.setAppleLocation(snake.getX(), snake.getY());
        snakeGame.attemptEatFruit();
        assertTrue(snakeGame.getScore() == 1); // score increases
        assertTrue(snake.getLength() == 3);
        snake.grow();
        assertTrue(snake.getLength() == 4);
        assertTrue(snakeGame.getScore() == 1); // score should not have changed
    }
    
}

