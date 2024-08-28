package com.csse3200.game.components.minigame;

/**
 * Represents a grid composed of GridCells.
 * The grid has a fixed width and height and can be used to track the occupancy of cells.
 */
public class Grid {
    private final int width;
    private final int height;
    private final GridCell[][] cells;

    /**
     * Creates a new Grid with the specified dimensions.
     * The grid is composed of GridCells that are initially unoccupied.
     *
     * @param width  The width of the grid (number of cells in the x-direction).
     * @param height The height of the grid (number of cells in the y-direction).
     */
    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        this.cells = new GridCell[width][height];
        initialiseGrid();
    }

    /**
     * Initialises the grid by creating a GridCell at each position.
     */
    private void initialiseGrid() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y] = new GridCell(x, y);
            }
        }
    }

    /**
     * Gets the GridCell at the specified coordinates.
     *
     * @param x The x-coordinate of the cell.
     * @param y The y-coordinate of the cell.
     * @return The GridCell at the specified position, or null if the coordinates are out of bounds.
     */
    public GridCell getCell(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return cells[x][y];
        }
        return null;
    }

    /**
     * Checks if the cell at the specified coordinates is occupied.
     *
     * @param x The x-coordinate of the cell.
     * @param y The y-coordinate of the cell.
     * @return True if the cell is occupied, false otherwise.
     */
    public boolean isOccupied(int x, int y) {
        GridCell cell = getCell(x, y);
        return cell != null && cell.isOccupied();
    }

    /**
     * Sets the occupancy status of the cell at the specified coordinates.
     *
     * @param x        The x-coordinate of the cell.
     * @param y        The y-coordinate of the cell.
     * @param occupied True if the cell should be marked as occupied, false otherwise.
     */
    public void setOccupied(int x, int y, boolean occupied) {
        GridCell cell = getCell(x, y);
        if (cell != null) {
            cell.setOccupied(occupied);
        }
    }

    /**
     * Get the grids width
     * @return the grids width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the grids height
     * @return the grids height
     */
    public int getHeight() {
        return height;
    }
}
