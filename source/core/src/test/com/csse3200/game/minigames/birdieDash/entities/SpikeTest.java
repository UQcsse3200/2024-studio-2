package com.csse3200.game.minigames.birdieDash.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

class SpikeTest {

    private Spike spike;
    private final float POSITION_X = 150; // Sample position value

    // Method Set Up to initialise the Spike instance
    @BeforeEach
    void setUp() {
        spike = new Spike(POSITION_X);
    }

    // Testing the initial position of the spike
    @Test
    void testInitialPosition() {
        Vector2 position = spike.getPosition();
        assertEquals(POSITION_X, position.x, "Spike X position should be correctly initialized.");
        assertEquals(0, position.y, "Spike Y position should be zero.");
    }

    // Testing the Width of the spike
    @Test
    void testWidth() {
        assertEquals(100, spike.getWidth(), "Spike width should be 100.");
    }

    // Testing the height of the spike
    @Test
    void testHeight() {
        assertEquals(1200, spike.getHeight(), "Spike height should be 1200.");
    }

    // Testing the Spike boundary
    @Test
    void testSpikeBoundary() {
        Rectangle boundary = spike.getSpikeBoundary();
        // checking X
        assertEquals(0, boundary.x, "Spike boundary X position = 0.");
        // checking Y
        assertEquals(0, boundary.y, "Spike boundary Y position = 0.");
        // checking with
        assertEquals(100, boundary.width, "Spike boundary width = 100.");
        // checking height
        assertEquals(1200, boundary.height, "Spike boundary height = 1200.");
    }
}

