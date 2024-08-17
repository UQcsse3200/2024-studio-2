package com.csse3200.game.components.minigame.snake;

import com.csse3200.game.components.minigame.Grid;

import java.util.Random;

/**
 * Represents an apple in the Snake game.
 * The apple is placed on a grid and can be randomly spawned in an unoccupied cell.
 */
public class Apple {
    private int x;
    private int y;
    private final Grid grid;

    /**
     * Creates an apple associated with a specific grid.
     * The apple is initially spawned at a random unoccupied position on the grid.
     *
     * @param grid The grid on which the apple is placed.
     */
    public Apple(Grid grid) {
        this.grid = grid;
        spawn();
    }

    /**
     * Randomly spawns the apple at an unoccupied position on the grid.
     * The apple will not be placed in a cell that is already occupied.
     */
    public void spawn() {
        Random random = new Random();
        do {
            x = random.nextInt(grid.getWidth());
            y = random.nextInt(grid.getHeight());
        } while (grid.isOccupied(x, y)); // Ensure the apple doesn't spawn on top of the snake

        grid.setOccupied(x, y, true);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
