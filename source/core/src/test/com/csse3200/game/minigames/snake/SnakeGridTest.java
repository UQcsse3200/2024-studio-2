package com.csse3200.game.minigames.snake;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
class SnakeGridTest {

    // it initialises the SnakeGrid instance before each test
    private SnakeGrid snakeGrid;

    @BeforeEach
    void setUp() {
        snakeGrid = new SnakeGrid();
    }

    @Test
    void testDefaultDimensions() {
        // the expected width and height for the grid class.
        int expectedWidth = 20;
        int expectedHeight = 20;

        // retrieves the actual width and height of the SnakeGrid
        int actualWidth = snakeGrid.getWidth();
        int actualHeight = snakeGrid.getHeight();

        // Assert that the actual width matches with the expected width
        assertEquals(expectedWidth, actualWidth, "The width of the SnakeGrid should be 20.");
        // Assert that the actual height matches with the expected height
        assertEquals(expectedHeight, actualHeight, "The height of the SnakeGrid should be 20.");
    }
}
