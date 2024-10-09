package com.csse3200.game.minigames.birdieDash.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
class PipeTest {

    private Pipe pipe;

    // Method Set Up to initialise the Pipe instance
    @BeforeEach
    public void setUp() {
        pipe = new Pipe(1920, 200); // start position and speed
    }

    // Testing to check that the initial pipe's position
    @Test
    void testInitialPipePosition() {
        // Ensuring that the initial positions of top and bottom pipes are set correctly
        Vector2 bottomPos = pipe.getPositionBottom();
        Vector2 topPos = pipe.getPositionTop();

        assertEquals(1920, bottomPos.x, "Bottom pipe should start at x=1920");
        assertEquals(0, bottomPos.y, "Bottom pipe should start at y=0");
        assertTrue(topPos.y > bottomPos.y, "Top pipe should be above the bottom pipe");
    }

    @Test
    void testChangePosition() {
        // Capturing the initial positions
        Vector2 initialBottomPos = pipe.getPositionBottom().cpy();
        Vector2 initialTopPos = pipe.getPositionTop().cpy();

        // Changing the position using a small delta time
        pipe.changePosition(0.1f);

        // Ensuring that the pipes moved left by the appropriate amount
        assertTrue(pipe.getPositionBottom().x < initialBottomPos.x, "Bottom pipe should have moved left");
        assertTrue(pipe.getPositionTop().x < initialTopPos.x, "Top pipe should have moved left");
    }

    @Test
    void testPipeRespawn() {
        // Moving the pipe off-screen
        pipe.changePosition(50f); // Change position enough times to move off screen

        // Testing that the pipe has respawned
        assertTrue(pipe.getPositionBottom().x > 0, "Pipe should respawn on the right side of the screen");
    }

    @Test
    void testBottomPipeRectangle() {
        // Getting the bottom pipe's rectangle and ensuring it is positioned correctly
        Rectangle bottomRect = pipe.getBottomPipe();

        assertEquals(pipe.getPositionBottom().x, bottomRect.x, "Bottom pipe's rectangle x position should match bottom position");
        assertEquals(pipe.getPositionBottom().y, bottomRect.y, "Bottom pipe's rectangle y position should match bottom position");
        assertEquals(pipe.getWidth(), bottomRect.width, "Bottom pipe's rectangle width should match pipe width");
        assertEquals(pipe.getHeightBottom(), bottomRect.height, "Bottom pipe's rectangle height should match bottom pipe height");
    }

    @Test
    void testTopPipeRectangle() {
        // Getting the top pipe's rectangle and ensuring it is positioned correctly
        Rectangle topRect = pipe.getTopPipe();

        assertEquals(pipe.getPositionTop().x, topRect.x, "Top pipe's rectangle x position should match top position");
        assertEquals(pipe.getPositionTop().y, topRect.y, "Top pipe's rectangle y position should match top position");
        assertEquals(pipe.getWidth(), topRect.width, "Top pipe's rectangle width should match pipe width");
        assertEquals(pipe.getHeightTop(), topRect.height, "Top pipe's rectangle height should match top pipe height");
    }
}

