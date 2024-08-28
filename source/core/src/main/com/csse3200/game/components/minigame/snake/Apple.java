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
    private final Random random; 

    /**
     * Creates an apple associated with a specific grid.
     * The apple is initially spawned at a random unoccupied position on the grid.
     *
     * @param grid The grid on which the apple is placed.
     */
    public Apple(Grid grid) {
        this.grid = grid;
        this.random = new Random();
        spawn();
    }

    /**
     * Randomly spawns the apple at an unoccupied position on the grid.
     * The apple will not be placed in a cell that is already occupied.
     */
    public void spawn() {
        do {
            x = random.nextInt(grid.getWidth());
            y = random.nextInt(grid.getHeight());
        } while (grid.isOccupied(x, y)); // Ensure the apple doesn't spawn on top of the snake
    }

    /**
     * Checks to see if the apple is touching the snakes head
     * @param snake: the game snake
     * @return True if head is touching else false
     */
    public boolean isTouchingSnakeHead(Snake snake) {
        return snake.getX() == x && snake.getY() == y;
    }

    /**
     * Get the apples x position
     * @return x position
     */
    public int getX() {
        return x;
    }

    /**
     * Get the apples y position
     * @return y position
     */
    public int getY() {
        return y;
    }

    /**
     * Set apple location. Used for testing and debugging only.
     */
    public void setAppleLocation(int newX, int newY) {
        x = newX;
        y = newY;
    }
}
