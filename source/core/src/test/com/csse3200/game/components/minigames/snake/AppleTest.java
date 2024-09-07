package com.csse3200.game.components.minigames.snake;

import com.csse3200.game.components.minigames.Grid;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AppleTest {

    private Grid grid;

    @Before
    public void setUp() {
        grid = new Grid(10, 10);
    }

    @Test
    public void testAppleSpawn() {
        Apple apple = new Apple(grid);

        int x = apple.getX();
        int y = apple.getY();

        assertTrue(x >= 0 && x < grid.getWidth());
        assertTrue(y >= 0 && y < grid.getHeight());
    }

    @Test
    public void testAppleRespawn() {
        Apple apple = new Apple(grid);

        apple.spawn();
        int newX = apple.getX();
        int newY = apple.getY();

        // The apple can theoretically respawn in the same spot, so we're checking if it's
        // at least valid
        assertTrue(newX >= 0 && newX < grid.getWidth());
        assertTrue(newY >= 0 && newY < grid.getHeight());
    }
}
