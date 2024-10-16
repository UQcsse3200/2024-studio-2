package com.csse3200.game.minigames;

import com.csse3200.game.extensions.GameExtension;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.Assert.*;

@ExtendWith(GameExtension.class)
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
