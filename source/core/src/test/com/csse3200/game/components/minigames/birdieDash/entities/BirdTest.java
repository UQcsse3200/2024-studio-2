package com.csse3200.game.components.minigames.birdieDash.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BirdTest {

    private Bird bird;
    // Method Set Up to initialise the Bird instance
    @BeforeEach
    void setUp() {
        bird = new Bird(100, 300);
    }

    // Testing the initial position of the Bird
    @Test
    void testInitialPosition() {
        assertEquals(new Vector2(100, 300), bird.getPosition());
    }

    // Testing the Initial Bounding Box
    @Test
    void testInitialBoundingBox() {
        Rectangle expectedBoundingBox = new Rectangle(100, 300, 60, 45);
        assertEquals(expectedBoundingBox, bird.getBoundingBox());
    }

    // Testing to ensure that the bird's flapping is changing so does the bird's velocity
    @Test
    void testFlap() {
        bird.flap();
        assertEquals(300f, bird.getPosition().y, "Velocity should be set to FLAP_STRENGTH.");
    }

    // Testing to ensure that gravity affects the bird's position over time
    @Test
    void testUpdateWithGravity() {
        bird.update(1.0f); // Updating with deltaTime of 1 second
        assertTrue(bird.getPosition().y < 300, "Bird should fall due to gravity.");
    }

    // Testing to ensure that gravity affects the bird's position over time
    @Test
    void testCollisionPipe() {
        bird.setCollidingPipe();
        bird.update(0.1f); // Small deltaTime
        assertTrue(bird.getPosition().x < 100, "Bird should move left when colliding with pipe.");
    }

    // Testing to ensure that bounding box is updated correctly after the bird's movement
    // The bounding box should move along with the bird
    @Test
    void testUpdateBoundingBox() {
        bird.update(0.1f);
        Rectangle updatedBoundingBox = new Rectangle(100, bird.getPosition().y, 60, 45);
        assertEquals(updatedBoundingBox, bird.getBoundingBox());
    }
}
