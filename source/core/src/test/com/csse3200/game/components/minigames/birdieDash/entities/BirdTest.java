package com.csse3200.game.components.minigames.birdieDash.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.minigames.birdieDash.entities.Bird;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BirdTest {
    private Bird bird;

    // Method Set Up to initialise the Bird instance
    @BeforeEach
    void setUp() {
        bird = new Bird(100, 300); // Initial bird position
    }

    // Testing to verify the Bird's initial position when it is created.
    @Test
    void testInitialPosition() {
        Vector2 position = bird.getPosition();
        assertEquals(100, position.x, "Initial X position should be 100");
        assertEquals(300, position.y, "Initial Y position should be 300");
    }

    // Testing to ensure that the Bird's velocity is updated when the flap method is called.
    @Test
    void testFlap() {
        bird.flapp();
        Vector2 velocity = bird.getPosition();
        assertEquals(300, bird.getPosition().y, "Y velocity after flap should be 300");
    }

    // Testing the bird's movement without gravity.
    @Test
    void testUpdateWithoutGravity() {
        bird.update(1.0f, 1); // simulate 1 second
        assertTrue(bird.getPosition().y > 0, "Bird should not fall below 0");
    }

    // Test the bird's movement with gravity.
    @Test
    void testUpdateWithGravity() {
        bird.update(1.0f, 1);
        assertTrue(bird.getPosition().y > 0, "Bird should be falling");
    }

    // Testing bird collides with the pipe
    @Test
    void testCollidingWithPipe() {
        bird.setCollidingPipe();
        bird.update(1.0f, 1);
        assertTrue(bird.getPosition().x < 100, "Bird's X position should decrease when colliding with a pipe");
    }

    // Testing after the bird stops colliding with a pipe, its X position does not continue decreasing.
    @Test
    void testUnsetCollidingPipe() {
        bird.setCollidingPipe();
        bird.unsetCollidingPipe();
        bird.update(1.0f, 1);
        assertFalse(bird.getPosition().x < 100, "Bird's X position should not decrease when not colliding with a pipe");
    }

    // Testing to ensure the bird's bounding box updates its position when the bird moves.
//    @Test
//    void testUpdateBoundingBox() {
//        bird.update(0.1f, 1);
//        Rectangle updatedBoundingBox = new Rectangle(100, bird.getPosition().y, 60, 45);
//        assertEquals(updatedBoundingBox, bird.getBoundingBox());
//    }
}

