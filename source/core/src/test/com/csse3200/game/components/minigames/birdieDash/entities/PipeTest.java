package com.csse3200.game.components.minigames.birdieDash.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PipeTest {

    private Pipe pipe;

    // Method Set Up to initialise the Pipe instance
    @BeforeEach
    void setUp() {
        pipe = new Pipe(1920, 200.0f);
    }

    // Testing to check that the pipe's position changes correctly
    @Test
    void testChangePosition() {
        // getting the initial position of the pipe
        float initialPosition = pipe.getPositionBottom().x;
        // updating the pipe's position
        pipe.changePosition(1.0f);
        // the pipe's X position should decrease after the position changes
        assertTrue(pipe.getPositionBottom().x < initialPosition, "Pipe should move left after changePosition.");
    }

    // Testing the pipe respawns correctly
    @Test
    void testRespawnPipe() {
        pipe.changePosition(2000.0f);
        pipe.respawnPipe();
        assertEquals(1920 + 960 - pipe.getWidth(), pipe.getPositionBottom().x, "Respawned pipe X position should be GAME_WIDTH + 960 - width.");
    }

    // Testing the set rectangles
    @Test
    void testSetRectangles() throws Exception {
        // Using the method of reflection to access the private methods
        Method setRectanglesMethod = Pipe.class.getDeclaredMethod("setRectangles");
        setRectanglesMethod.setAccessible(true);

        setRectanglesMethod.invoke(pipe);

        assertEquals(pipe.getPositionBottom().x, pipe.getBottomPipe().x, "Bottom pipe rectangle X position should match bottom position.");
        assertEquals(pipe.getPositionBottom().y, pipe.getBottomPipe().y, "Bottom pipe rectangle Y position should match bottom position.");
        assertEquals(pipe.getHeightBottom(), pipe.getBottomPipe().height, "Bottom pipe rectangle height should match pipe height.");
        assertEquals(pipe.getHeightTop(), pipe.getTopPipe().height, "Top pipe rectangle height should match height calculated from game dimensions.");
    }
}
