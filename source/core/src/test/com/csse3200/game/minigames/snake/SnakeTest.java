package com.csse3200.game.minigames.snake;

import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.minigames.Direction;
import com.csse3200.game.minigames.Grid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
class SnakeTest {
    private Grid grid;
    private Snake snake;

    @BeforeEach
    void setup() {
        grid = new Grid(10, 10); // Example grid size
        snake = new Snake(grid, 5, 5, Direction.RIGHT, 3, 1.0f);
    }

    @Test
    void testInitialSnakeState() {
        assertEquals(5, snake.getX());
        assertEquals(5, snake.getY());
        assertEquals(Direction.RIGHT, snake.getDirection());
        assertEquals(3, snake.getLength());
    }

    @Test
    void testSetDirection() {
        snake.setDirection(Direction.UP);
        assertEquals(Direction.UP, snake.getDirection());
    }

    @Test
    void testMoveSnake() {
        snake.move(Direction.RIGHT);
        assertEquals(6, snake.getX());
        assertEquals(5, snake.getY());

        snake.move(Direction.DOWN);
        assertEquals(6, snake.getX());
        assertEquals(4, snake.getY());
    }

    @Test
    void testUpdateDirectionOnInput() {
        snake.updateDirectionOnInput(Direction.LEFT);
        assertEquals(Direction.RIGHT, snake.getDirection()); // Shouldn't change to opposite direction

        snake.updateDirectionOnInput(Direction.UP);
        snake.update(1.1f); // Enough time for move
        assertEquals(Direction.UP, snake.getDirection()); // Should change to UP now
    }

    @Test
    void testGrowSnake() {
        int originalLength = snake.getLength();
        snake.grow();
        assertEquals(originalLength + 1, snake.getLength());
    }

    @Test
    public void testUpdate() {
        snake.update(1.1f);
        assertEquals(6, snake.getX());
        assertEquals(5, snake.getY());
    }

    @Test
    void testSnakeBodySegments() {
        snake.move(Direction.RIGHT);
        assertEquals(1, snake.getBodySegments().size());
        Snake.Segment lastSegment = snake.getBodySegments().get(0);
        assertEquals(new Snake.Segment(5, 5, Direction.RIGHT), lastSegment);
    }

    @Test
    void testMovePeriod() {
        assertEquals(1.0f, snake.getMovePeriod());
        snake.updateMovePeriod(2.0f);
        assertEquals(2.0f, snake.getMovePeriod());
    }
}