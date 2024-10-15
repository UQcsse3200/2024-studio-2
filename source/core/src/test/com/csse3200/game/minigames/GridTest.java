package com.csse3200.game.minigames;

import org.junit.Test;
import static org.junit.Assert.*;

public class GridTest {

    @Test
    public void testGridCreation() {
        Grid grid = new Grid(10, 15);

        assertEquals(10, grid.getWidth());
        assertEquals(15, grid.getHeight());

        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                assertNotNull(grid.getCell(x, y));
                assertFalse(grid.isOccupied(x, y));
            }
        }
    }

    @Test
    public void testSetOccupied() {
        Grid grid = new Grid(10, 10);
        grid.setOccupied(5, 5, true);

        assertTrue(grid.isOccupied(5, 5));

        grid.setOccupied(5, 5, false);
        assertFalse(grid.isOccupied(5, 5));
    }

    @Test
    public void testGetCellOutOfBounds() {
        Grid grid = new Grid(10, 10);

        assertNull(grid.getCell(-1, 0));
        assertNull(grid.getCell(0, -1));
        assertNull(grid.getCell(10, 0));
        assertNull(grid.getCell(0, 10));
    }
}
