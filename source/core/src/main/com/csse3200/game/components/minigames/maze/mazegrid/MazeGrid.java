package com.csse3200.game.components.minigames.maze.mazegrid;

import com.csse3200.game.components.minigames.Grid;

public class MazeGrid{
    /**
     * Creates a new Grid with the specified dimensions.
     * Specialised Grid takes in a file path and converts it into a maze
     *
     * @param width  The width of the grid (number of cells in the x-direction).
     * @param height The height of the grid (number of cells in the y-direction).
     */

    private final String file;
    private final int width;
    private final int height;
    public MazeGrid(int width, int height, String file) {
        this.width = width;
        this.height = height;
        this.file = file;
    }
}
