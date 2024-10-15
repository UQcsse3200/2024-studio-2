package com.csse3200.game.minigames.snake;

import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.minigames.Grid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class AppleTest {

    private Grid mockGrid;
    private Snake mockSnake;
    private Apple apple;

    @BeforeEach
    void setUp() {
        // Creating a mock grid
        mockGrid = Mockito.mock(Grid.class);

        // Creating a mock snake
        mockSnake = Mockito.mock(Snake.class);

        // Setting up the grid to have a width and height of 10 (or any size you want)
        when(mockGrid.getWidth()).thenReturn(10);
        when(mockGrid.getHeight()).thenReturn(10);

        // Initialising set all cells to unoccupied
        when(mockGrid.isOccupied(Mockito.anyInt(), Mockito.anyInt())).thenReturn(false);

        // Creating the apple with the mock grid
        apple = new Apple(mockGrid);
    }

    @Test
    void testAppleSpawnsInUnoccupiedCell() {
        // Checking that the apple spawned at a valid position within the grid bounds
        assertTrue(apple.getX() >= 0 && apple.getX() < mockGrid.getWidth());
        assertTrue(apple.getY() >= 0 && apple.getY() < mockGrid.getHeight());

        // Verifying that the apple does not spawn in an occupied cell
        when(mockGrid.isOccupied(apple.getX(), apple.getY())).thenReturn(true);
        apple.spawn();
        assertFalse(mockGrid.isOccupied(apple.getX(), apple.getY()));
    }

    @Test
    void testAppleTouchesSnakeHead() {
        // Setting snake's position to the apple's location
        when(mockSnake.getX()).thenReturn(apple.getX());
        when(mockSnake.getY()).thenReturn(apple.getY());

        // Checking that the apple is touching the snake's head
        assertTrue(apple.isTouchingSnakeHead(mockSnake));
    }

    @Test
    void testAppleNotTouchingSnakeHead() {
        // Setting snake's position to a different location than the apple
        when(mockSnake.getX()).thenReturn(5);
        when(mockSnake.getY()).thenReturn(5);

        // Checking that the apple is not touching the snake's head
        assertFalse(apple.isTouchingSnakeHead(mockSnake));
    }

    @Test
    void testSetAppleLocation() {
        // Setting the apple's position to a specific location
        apple.setAppleLocation(3, 4);

        // Checking that the apple's position was updated correctly
        assertEquals(3, apple.getX());
        assertEquals(4, apple.getY());
    }
}

