package com.csse3200.game.minigames.snake;

import com.csse3200.game.minigames.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SnakeGameTest {
    private SnakeGame game;

    @BeforeEach
    void setUp() {
        game = new SnakeGame();
    }

    @Test
    void testInitialGameState() {
        assertNotNull(game.getSnake());
        assertNotNull(game.getApple());
        assertNotNull(game.getGrid());
        assertEquals(0, game.getScore());
        assertFalse(game.getIsGameOver());
    }

    @Test
    void testAppleEatingIncreasesScore() {
        // set apple location
        Apple apple = game.getApple();
        apple.setAppleLocation(1, -1);

        // Ensure snake is positioned correctly to eat the apple
        game.handleSnakeInput(Direction.RIGHT); // Move right
        game.snakeMove(1); // Move the snake
        game.handleSnakeInput(Direction.DOWN); // Move down
        game.snakeMove(1); // Move the snake
        game.attemptEatFruit(); // Attempt to eat the apple

        assertEquals(1, game.getScore(), "The score should increase by 1.");
    }

    @Test
    void testSnakeCollisionDetection() {
        // Grow the snake to be long enough to collide with itself
        for (int i = 0; i < 5; i++) {
            game.getSnake().grow();
        }

        // Move snake in a circle to collide with itself
        Snake snake = game.getSnake();
        snake.setDirection(Direction.RIGHT);
        snake.update(1f);
        snake.setDirection(Direction.UP);
        snake.update(1f);
        snake.setDirection(Direction.LEFT);
        snake.update(1f);
        snake.setDirection(Direction.DOWN);
        snake.update(1f);

        assertTrue(game.snakeCollisionDetection(), "The snake should collide with itself.");
    }

    @Test
    void testBoundaryYDetection() {
        // Move the snake outside the boundaries of the grid
        game.getSnake().updateDirectionOnInput(Direction.UP); // Move up
        for (int i = 0; i <= 20; i++) {
            game.snakeMove(1);
        }
        assertTrue(game.boundaryDetection(), "The snake should be detected as out of bounds.");
    }

    @Test
    void testBoundaryXDetection() {
        // Move the snake outside the boundaries of the grid

        game.getSnake().updateDirectionOnInput(Direction.RIGHT);
        for (int i = 0; i <= 20; i++) {
            game.snakeMove(1);
        }

        assertTrue(game.boundaryDetection(), "The snake should be detected as out of bounds.");

    }



    @Test
    void testGameOverOnBoundaryHit() {
        game.getSnake().updateDirectionOnInput(Direction.DOWN); // Move left out of bounds
        game.snakeMove(1); // Update the game state
        assertTrue(game.getIsGameOver(), "The game should be over when hitting the boundary.");
    }

    @Test
    void testGameOverOnSelfCollision() {
        game.handleSnakeInput(Direction.RIGHT); // Move to the right
        game.snakeMove(1); // Move the snake
        game.handleSnakeInput(Direction.DOWN); // Move down
        game.snakeMove(1); // Move the snake
        game.handleSnakeInput(Direction.LEFT); // Move left to eat the apple
        game.snakeMove(1); // Move the snake

        game.attemptEatFruit(); // Eat the apple to grow

        // Move the snake head on top of its body
        game.handleSnakeInput(Direction.UP); // Move back to collide
        game.snakeMove(1); // Update the game state
        assertTrue(game.getIsGameOver(), "The game should be over when the snake collides with itself.");
    }

    @Test
    void testIncreaseSnakeSpeed() {

        Apple apple = game.getApple();
        Float originalPeriod = game.getSnake().getMovePeriod();

        // Move the snake to the apple's position to increase score
        for (int i = 0; i < 5; i++) {
            apple.setAppleLocation(i+1, 0);
            game.handleSnakeInput(Direction.RIGHT); // Move towards the apple
            game.snakeMove(1); // Move the snake
            assertTrue(game.attemptEatFruit(), "The snake should eat the apple."); // Ensure apple is eaten
        }

        // Verify that the speed increased after 5 points
        assertEquals(originalPeriod - 1f/ 60, game.getSnake().getMovePeriod(), "The snake's speed should increase after 5 points.");
    }
}