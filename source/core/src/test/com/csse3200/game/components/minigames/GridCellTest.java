package com.csse3200.game.components.minigames;

import org.junit.Test;
import static org.junit.Assert.*;

public class GridCellTest {

    @Test
    public void testGridCellCreation() {
        GridCell cell = new GridCell(2, 3);

        assertEquals(2, cell.getX());
        assertEquals(3, cell.getY());
        assertFalse(cell.isOccupied());
    }

    @Test
    public void testSetOccupied() {
        GridCell cell = new GridCell(2, 3);
        cell.setOccupied(true);

        assertTrue(cell.isOccupied());

        cell.setOccupied(false);
        assertFalse(cell.isOccupied());
    }
}
