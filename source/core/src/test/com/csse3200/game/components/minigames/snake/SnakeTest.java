package com.csse3200.game.components.minigames.snake;

import com.csse3200.game.components.minigames.Direction;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class SnakeTest {

    Snake snake;
    SnakeGrid grid;

    @Before
    public void setUp() {
        this.grid = new SnakeGrid();
        this.snake = new Snake(grid, 0, 0, Direction.RIGHT, 2, 1);
    }

    @Test
    public void testMove() {
        assertEquals(snake.getX(), 0);
        assertEquals(snake.getY(), 0);
        this.snake.move(Direction.RIGHT);
        assertEquals(snake.getX(), 1);
        assertEquals(snake.getY(), 0);
        this.snake.move(Direction.UP);
        assertEquals(snake.getX(), 1);
        assertEquals(snake.getY(), 1);
        this.snake.move(Direction.LEFT);
        assertEquals(snake.getX(), 0);
        assertEquals(snake.getY(), 1);
        this.snake.move(Direction.DOWN);
        assertEquals(snake.getX(), 0);
        assertEquals(snake.getY(), 0);
    }

    @Test
    public void testUpdateDirectionOnInput() {
        this.snake.setDirection(Direction.UP);
        assertEquals(snake.getDirection(), Direction.UP);

        this.snake.updateDirectionOnInput(Direction.DOWN);
        this.snake.update(1);
        assertEquals(snake.getDirection(), Direction.UP);
        this.snake.updateDirectionOnInput(Direction.RIGHT);
        this.snake.update(1);
        assertEquals(snake.getDirection(), Direction.RIGHT);

        this.snake.updateDirectionOnInput(Direction.LEFT);
        this.snake.update(1);
        assertEquals(snake.getDirection(), Direction.RIGHT);
        this.snake.updateDirectionOnInput(Direction.DOWN);
        this.snake.update(1);
        assertEquals(snake.getDirection(), Direction.DOWN);

        this.snake.updateDirectionOnInput(Direction.UP);
        this.snake.update(1);
        assertEquals(snake.getDirection(), Direction.DOWN);
        this.snake.updateDirectionOnInput(Direction.LEFT);
        this.snake.update(1);
        assertEquals(snake.getDirection(), Direction.LEFT);

        this.snake.updateDirectionOnInput(Direction.RIGHT);
        this.snake.update(1);
        assertEquals(snake.getDirection(), Direction.LEFT);
        this.snake.updateDirectionOnInput(Direction.UP);
        this.snake.update(1);
        assertEquals(snake.getDirection(), Direction.UP);
    }

    @Test
    public void testUpdate() {
        this.snake.updateDirectionOnInput(Direction.UP);
        assertEquals(snake.getDirection(), Direction.RIGHT);
        assertEquals(0, snake.getX());
        assertEquals(0, snake.getY());
        snake.update(0.5f);
        assertEquals(snake.getDirection(), Direction.RIGHT);
        assertEquals(0, snake.getX());
        assertEquals(0, snake.getY());
        snake.update(0.5f);
        assertEquals(snake.getDirection(), Direction.UP);
        assertEquals(0, snake.getX());
        assertEquals(1, snake.getY());
    }

    @Test
    public void testGrow() {
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

