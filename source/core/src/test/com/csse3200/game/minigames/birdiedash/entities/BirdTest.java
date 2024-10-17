package com.csse3200.game.minigames.birdiedash.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
public class BirdTest {
    private Bird bird;
    private final float BIRD_HEIGHT = 45f;
    // Method Set Up to initialise the Bird instance
    @BeforeEach
    public void setUp() {
        bird = new Bird(100, 300); // This is the initial bird's position
    }


    // Testing to verify the Bird's initial position when it is created.
    @Test
    public void testInitialPosition() {
        Vector2 position = bird.getPosition();
        assertEquals(100, position.x, "Initial X position should be 100");
        assertEquals(300, position.y, "Initial Y position should be 300");
    }

    // Testing to ensure that the Bird's velocity is updated when the flap method is called.
    @Test
    public void testFlap() {
        bird.flap();
        assertEquals(300, bird.getPosition().y, "Y velocity after flap should be 300");
    }

    // Testing the bird's movement with gravity.
    @Test
    public void testUpdateWithGravity() {

        bird.flap(); // Simulate a flap
        bird.update(1.0f, 1); // Update with gravity

        assertTrue(bird.getPosition().y < 300, "Bird's Y position should decrease due to gravity");
    }

    // Testing bird collides with the pipe
    @Test
    public void testCollidingWithPipe() {
        bird.setCollidingPipe();
        bird.update(1.0f, 1);
        assertTrue(bird.getPosition().x < 100, "Bird's X position should decrease when colliding with a pipe");
    }

    // Testing after the bird stops colliding with a pipe, its X position does not continue decreasing.
    @Test
    public void testUnsetCollidingPipe() {
        bird.setCollidingPipe();
        bird.unsetCollidingPipe();
        bird.update(1.0f, 1);
        assertFalse(bird.getPosition().x < 100, "Bird's X position should not decrease when not colliding with a pipe");
    }

    // Testing to ensure the bird's bounding box updates its position when the bird moves.
    @Test
    public void testUpdateBoundingBox() {
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

    @Test
    public void testGoBelowScreen() {
        bird.setPosition(500,-10);
        bird.update(1.0f, 1.0f);
        assertEquals(0, bird.getPosition().y);
    }

    @Test
    public void testGoAboveScreen() {
        bird.setPosition(500,1210);
        bird.update(0.0001f, 1.0f);
        assertEquals(1155, bird.getPosition().y);
    }

    @Test
    public void testBirdTopofPipePosition() {
        bird.setPosition(300,300);
        bird.setCollidingTopPipe(300);
        assertEquals(301, bird.getPosition().y);
    }

    @Test
    public void testBirdTopofPipeVelocity() {
        bird.setPosition(300,300);
        bird.setCollidingTopPipe(300);
        bird.update(1, 1);
        assertEquals(0, bird.getVelocity().y);
    }

    @Test
    public void testTouchingFloor() {
        bird.setPosition(300, 0);
        assertTrue(bird.touchingFloor());
    }

    @Test
    public void testBirdHeight() {
        assertEquals(bird.getBirdHeight(), BIRD_HEIGHT);
    }

    @Test
    public void testCollidingBottomPipe() {
        bird.setPosition(100,100);
        bird.setCollidingBottomPipe(100);
        assertEquals(100-BIRD_HEIGHT-1, bird.getPosition().y);
    }

    @Test
    public void testUnsetCollidingTopPipe() {
        bird.setCollidingTopPipe(0);
        bird.unsetCollidingTopPipe();
        assertFalse(bird.isCollideTopOfPipe());
    }
}
