package com.csse3200.game.minigames.snake;

import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.minigames.Direction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.Assert.assertEquals;

@ExtendWith(GameExtension.class)
class SnakeTest {
    Snake snake;
    SnakeGrid grid;

    @BeforeEach
    void setUp() {
        grid = new SnakeGrid();
        snake = new Snake(grid, 0, 0, Direction.RIGHT, 2, 1);
    }

    @Test
    void testMove() {
        assertEquals(0, snake.getX());
        assertEquals(0, snake.getY());
        this.snake.move(Direction.RIGHT);
        assertEquals(1, snake.getX());
        assertEquals(0, snake.getY());
        this.snake.move(Direction.UP);
        assertEquals(1, snake.getX());
        assertEquals(1, snake.getY());
        this.snake.move(Direction.LEFT);
        assertEquals(0, snake.getX());
        assertEquals(1, snake.getY());
        this.snake.move(Direction.DOWN);
        assertEquals(0, snake.getX());
        assertEquals(0, snake.getY());
    }

    @Test
    void testUpdateDirectionOnInput() {
        this.snake.setDirection(Direction.UP);
        assertEquals(Direction.UP, snake.getDirection());

        this.snake.updateDirectionOnInput(Direction.DOWN);
        this.snake.update(1);
        assertEquals(Direction.UP, snake.getDirection());
        this.snake.updateDirectionOnInput(Direction.RIGHT);
        this.snake.update(1);
        assertEquals(Direction.RIGHT, snake.getDirection());

        this.snake.updateDirectionOnInput(Direction.LEFT);
        this.snake.update(1);
        assertEquals(Direction.RIGHT, snake.getDirection());
        this.snake.updateDirectionOnInput(Direction.DOWN);
        this.snake.update(1);
        assertEquals(Direction.DOWN, snake.getDirection());

        this.snake.updateDirectionOnInput(Direction.UP);
        this.snake.update(1);
        assertEquals(Direction.DOWN, snake.getDirection());
        this.snake.updateDirectionOnInput(Direction.LEFT);
        this.snake.update(1);
        assertEquals(Direction.LEFT, snake.getDirection());

        this.snake.updateDirectionOnInput(Direction.RIGHT);
        this.snake.update(1);
        assertEquals(Direction.LEFT, snake.getDirection());
        this.snake.updateDirectionOnInput(Direction.UP);
        this.snake.update(1);
        assertEquals(Direction.UP, snake.getDirection());
    }

    @Test
    void testUpdate() {
        this.snake.updateDirectionOnInput(Direction.UP);
        assertEquals(Direction.RIGHT, snake.getDirection());
        assertEquals(0, snake.getX());
        assertEquals(0, snake.getY());
        snake.update(0.5f);
        assertEquals(Direction.RIGHT, snake.getDirection());
        assertEquals(0, snake.getX());
        assertEquals(0, snake.getY());
        snake.update(0.5f);
        assertEquals(Direction.UP, snake.getDirection());
        assertEquals(0, snake.getX());
        assertEquals(1, snake.getY());
    }

    @Test
    void testGrow() {
        this.snake.setDirection(Direction.UP);
        this.snake.move(Direction.UP);
        assertEquals(1, this.snake.getBodySegments().size());
        this.snake.move(Direction.UP);
        assertEquals(1, this.snake.getBodySegments().size());
        this.snake.grow();
        this.snake.setDirection(Direction.RIGHT);
        this.snake.move(Direction.RIGHT);
        assertEquals(2, this.snake.getBodySegments().size());
        this.snake.grow();
        this.snake.move(Direction.RIGHT);
        assertEquals(3, this.snake.getBodySegments().size());
        List<Snake.Segment> segments = this.snake.getBodySegments();
        assertEquals(new Snake.Segment(0, 1, Direction.UP), segments.get(0));
        assertEquals(new Snake.Segment(0, 2, Direction.RIGHT), segments.get(1));
        assertEquals(new Snake.Segment(1, 2, Direction.RIGHT), segments.get(2));
    }
}

