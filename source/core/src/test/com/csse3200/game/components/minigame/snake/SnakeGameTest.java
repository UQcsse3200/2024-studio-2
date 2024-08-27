package com.csse3200.game.components.minigame.snake;

import com.csse3200.game.components.minigame.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/*
Test SnakeGame class functionality
 */
public class SnakeGameTest {

//    Snake snake;
//    Apple apple;
//    SnakeGrid grid;
    SnakeGame snakeGame;
    @Before
    public void setUp() {
//        this.grid = new SnakeGrid();
//        this.apple = new Apple(grid);
//        this.snake = new Snake(grid, 0, 0, Direction.RIGHT, 2, 1f / 10);
        // becuase of refacroting have new setUp
        this.snakeGame = new SnakeGame();
    }

    /*
    Test the Initial Snake and Score
     */
    @Test
    public void testInitialScoreSnakeLength() {


        Snake snake = snakeGame.getSnake();
        assertEquals(0, snakeGame.getScore());
        assertEquals(0, snake.getBodySegments().size());
        assertEquals(2, snake.getLength());
        assertEquals(snake.getBodySegments().size(), snakeGame.getScore());
    }

    /*
    Test the snake length increase and score
     */
    @Test
    public void testScoreSnakeLengthIncrease() {

        Snake snake = snakeGame.getSnake();
        Apple apple = snakeGame.getApple();

        // Move apple to snakes head
        apple.setAppleLocation(snake.getX(), snake.getY());
        snakeGame.attemptEatFruit();
        assertEquals(1, snakeGame.getScore()); // score increases
        assertEquals(3, snake.getLength());
        snake.grow();
        assertEquals(4, snake.getLength());
        assertEquals(1, snakeGame.getScore()); // score should not have changed
    }

    /*
    Test the boundary collision detection
     */
    @Test
    public void testSnakeBoundaryDetection() {

        Snake snake = snakeGame.getSnake();

        // Move the snake off the grid
        snake.setDirection(Direction.DOWN);
        snake.update(1f);
        assertTrue(snakeGame.boundaryDetection());

    }

    /*
    Test the snake collision detection
     */
    @Test
    public void testSnakeCollisionDetection() {
        SnakeGame snakeGame = new SnakeGame();
        Snake snake = snakeGame.getSnake();
        //grow the snake to be long enough
        snake.grow();
        snake.grow();
        snake.grow();
        snake.grow();
        snake.grow();

        // Move snake in circle to collide with itself
        snake.setDirection(Direction.RIGHT);
        snake.update(1f);
        snake.setDirection(Direction.UP);
        snake.update(1f);
        snake.setDirection(Direction.LEFT);
        snake.update(1f);
        snake.setDirection(Direction.DOWN);
        snake.update(1f);

        assertTrue(snakeGame.snakeCollisionDetection());
    }
}

