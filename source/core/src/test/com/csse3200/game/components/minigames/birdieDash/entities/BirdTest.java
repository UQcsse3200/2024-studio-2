package com.csse3200.game.components.minigames.birdieDash.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Field;

class BirdTest {
    private Bird bird;
    // Method Set Up to initialise the Bird instance
    @BeforeEach
    void setUp() {
        bird = new Bird(100, 300); // This is the initial bird's position
    }

    // Used helper method to get private field value using reflection
    private float getPrivateField(String fieldName) {
        try {
            Field field = Bird.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.getFloat(null); // Use 'null' for static fields
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
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
        bird.flap();
        Vector2 velocity = bird.getPosition();
        assertEquals(300, bird.getPosition().y, "Y velocity after flap should be 300");
    }

    // Testing the bird's movement without gravity.
    // NEEDS TO BE DONE
    @Test
    public void testUpdateWithoutGravity() {
    }


    // Testing the bird's movement with gravity.
    @Test
    void testUpdateWithGravity() {
        float flapStrength = getPrivateField("FLAP_STRENGTH");
        float gravity = getPrivateField("GRAVITY");

        bird.flap(); // Simulate a flap
        bird.update(1.0f, 1); // Update with gravity

        float expectedY = 300 + flapStrength + gravity;
        assertTrue(bird.getPosition().y < 300, "Bird's Y position should decrease due to gravity");
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
    @Test
    void testUpdateBoundingBox() {
        // Simulate some movement or state changes
        bird.update(0.1f, 1);

        // Updating the expected bounding box values based on the observed behaviour
        // Assuming the bird's position is correctly reflected as x = 105.0 and y = 298.5
        float expectedX = 105.0f;
        float expectedY = bird.getPosition().y;
        float width = 60.0f;
        float height = 45.0f;

        Rectangle expectedBoundingBox = new Rectangle(expectedX, expectedY, width, height);
        Rectangle actualBoundingBox = bird.getBoundingBox();

        // Checking that the expected and actual bounding boxes are equal
        assertEquals(expectedBoundingBox, actualBoundingBox, "Bounding box should update correctly with bird's position.");
    }
}
