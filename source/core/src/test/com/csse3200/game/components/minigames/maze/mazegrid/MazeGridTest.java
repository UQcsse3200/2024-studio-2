package com.csse3200.game.components.minigames.maze.mazegrid;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MazeGridTest {

    private MazeGrid mazeGrid;

    @Before
    public void setup() {
        this.mazeGrid = new MazeGrid(10, MazeFilePaths.TEST_MAZE);
    }

    /**
     * Tests if the first cell (0,0) in the grid is correctly parsed as a Wall.
     */
    @Test
    public void testFileParsing() {
        assertTrue(mazeGrid.getCell(0,0) instanceof Wall);
    }

    /**
     * Tests the entire grid parsing logic by comparing the parsed maze grid
     * to an expected grid layout. Verifies that each cell in the grid is
     * correctly parsed as either a Wall or a NotWall.
     */
    @Test
    public void testFileParsingFullGrid() {

        int[][] expectedGrid = {
                {1, 0, 0, 1, 0, 1, 0, 1, 0, 1},
                {1, 0, 1, 0, 1, 0, 1, 0, 1, 0},
                {0, 1, 0, 1, 0, 1, 0, 1, 0, 0},
                {1, 1, 1, 0, 0, 0, 0, 1, 0, 0},
                {1, 0, 1, 0, 1, 0, 1, 0, 0, 1},
                {1, 0, 1, 0, 1, 0, 1, 0, 1, 0},
                {1, 0, 1, 0, 1, 0, 1, 0, 0, 0},
                {0, 0, 1, 1, 1, 0, 1, 0, 1, 0},
                {0, 1, 0, 1, 0, 1, 0, 1, 0, 0},
                {0, 1, 0, 1, 0, 1, 0, 0, 0, 1}
        };
        // Iterate over the grid and compare each cell to the expected values
        for (int row = 0; row < expectedGrid.length; row++) {
            for (int col = 0; col < expectedGrid[row].length; col++) {
                if (expectedGrid[row][col] == 1) {
                    assertTrue("Expected Wall at (" + row + ", " + col + ") but found NotWall.",
                            mazeGrid.getCell(row, col) instanceof Wall);
                } else {
                    assertTrue("Expected NotWall at (" + row + ", " + col + ") but found Wall.",
                            mazeGrid.getCell(row, col) instanceof NotWall);
                }
            }
        }
    }
}
