package com.csse3200.game.components.minigames;

/**
 * Represents a single cell on the grid.
 * A cell has a fixed position (x, y) and can be either occupied or unoccupied.
 */
public class GridCell {
    private final int x;
    private final int y;
    private boolean occupied;

    /**
     * Creates a new GridCell at the specified coordinates.
     * The cell is initially unoccupied.
     *
     * @param x The x-coordinate of the cell.
     * @param y The y-coordinate of the cell.
     */
    public GridCell(int x, int y) {
        this.x = x;
        this.y = y;
        this.occupied = false;
    }

    /**
     * get the cells x coordinate
     * @return x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * get the cells y coordinate
     * @return y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Returns a booleean if the cell is occupied
     * @return true if occupied, otherwise false
     */
    public boolean isOccupied() {
        return occupied;
    }

    /**
     * Sets a cells occupied status
     * @param occupied: the status to set occupied to
     */
    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
}
