package com.csse3200.game.minigames.snake;

import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.minigames.Direction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/*
Test SnakeGame class functionality
 */
@ExtendWith(GameExtension.class)
class SnakeGameTest {
    static SnakeGame snakeGame;

    @BeforeAll
    static void setUp() {
        snakeGame = new SnakeGame();
    }

    /*
    Test the Initial Snake and Score
     */
    @Test
    void testInitialScoreSnakeLength() {
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
    void testScoreSnakeLengthIncrease() {
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
    void testSnakeBoundaryDetection() {

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
    void testSnakeCollisionDetection() {
        SnakeGame game = new SnakeGame();
        Snake snake = game.getSnake();
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

        assertTrue(game.snakeCollisionDetection());
    }
}

