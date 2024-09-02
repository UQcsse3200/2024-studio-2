package com.csse3200.game.components.minigame.snake;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SnakeGridTest {

    private SnakeGrid snakeGrid;

    @BeforeEach
    public void setUp() {
        snakeGrid = new SnakeGrid();
    }

    @Test
    public void testDefaultDimensions() {
        // the expected width and height for the grid class.
        int expectedWidth = 20;
        int expectedHeight = 20;

        int actualWidth = snakeGrid.getWidth();
        int actualHeight = snakeGrid.getHeight();

        assertEquals(expectedWidth, actualWidth, "The width of the SnakeGrid should be 20.");
        assertEquals(expectedHeight, actualHeight, "The height of the SnakeGrid should be 20.");
    }
}

