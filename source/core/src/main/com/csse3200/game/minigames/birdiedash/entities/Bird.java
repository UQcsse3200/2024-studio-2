package com.csse3200.game.minigames.birdiedash.entities;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;

/**
 * Class for the bird in the mini-game birdie dash
 */
public class Bird {
    private final Vector2 position; // Bird position
    private final Vector2 velocity; // Bird velocity
    private final Rectangle boundingBox; // Bird boundary
    private static final float GRAVITY = -2600f; // Gravity for physics
    private static final float FLAP_STRENGTH = 1200; // Bird flap strength
    private static final float BIRD_WIDTH = 60f; // The bird's width
    private static final float BIRD_HEIGHT = 45f; // The birds height
    private static final int GAME_HEIGHT = 1200; // The game height

    private boolean collidingPipe; // Variable to track if bird is collided with pipe
    private boolean collideTopOfPipe; // Variable to detects if bird collided with top (or bottom) of pipe
    private boolean isFlapping; //Variable if the bird is flapping

    public Bird(float x, float y) {
        position = new Vector2(x, y);
        velocity = new Vector2(0, 0);
        boundingBox = new Rectangle(x, y, BIRD_WIDTH, BIRD_HEIGHT);
        collidingPipe = false;
        collideTopOfPipe = false;
        isFlapping = false;
    }

    /**
     * Updates the birds position and adjusts velocity as needed
     * @param deltaTime the time from the last position update
     * @param multiplier Used to speed up the game
     */
    public void update(float deltaTime, float multiplier) {

        if(isFlapping) {
            isFlapping = false;
            velocity.y = FLAP_STRENGTH;
        }

        if (position.y > 0) {

            // Ensure bird does not increase velocity if flown into pipe
            if(collideTopOfPipe) {
                if (velocity.y < 0) {
                    velocity.y = 0;
                }
            } else {
                velocity.add(0, GRAVITY * deltaTime);
            }
        } else { // Ensure bird doesn't fly below screen
            position.y = position.x = 0;
        }

        // Increase velocity if not pipe collision
        if(position.x < 960 && !collidingPipe) {
            velocity.x = 50;
        } else {
            velocity.x = 0;
        }
        velocity.scl(deltaTime);
        position.add(velocity);
        if(collidingPipe) {
            position.sub(deltaTime * multiplier * 200, 0);
        }
        velocity.scl(1 / deltaTime);
        if (position.y + BIRD_HEIGHT > GAME_HEIGHT) { // Ensure bird doesn't fly above screen
            position.y = GAME_HEIGHT - BIRD_HEIGHT;
            velocity.set(0,0);
        }
        updateBoundingBox();
    }

    /**
     * Method to set that the bird is colliding with the top of a pipe
     * @param y the y position
     */
    public void setCollidingTopPipe(float y) {
        collideTopOfPipe = true;
        position.set(position.x, y + 1);
    }

    /**
     * Used for testing
     */
    public boolean isCollideTopOfPipe() {
        return collideTopOfPipe;
    }

    /**
     * Method to unset the bird is colliding with a pipe
     */
    public void unsetCollidingTopPipe() {
        collideTopOfPipe = false;
    }

    /**
     * Method to set that the bird is colliding with the bottom of a pipe
     * @param y the y position
     */
    public void setCollidingBottomPipe(float y) {
        position.set(position.x, y - getBirdHeight() - 1);
        velocity.set(0,0);
    }

    /**
     * For collisions
     */
    public void setCollidingPipe() {
        collidingPipe = true;
    }

    /**
     * For testing
     */
    public boolean isCollidingPipe() {
        return collidingPipe;
    }

    /**
     * Gets the birds height
     * @return the birds height
     */
    public float getBirdHeight() {
        return BIRD_HEIGHT;
    }

    /**
     * For collisions.
     */
    public void unsetCollidingPipe() {
        collidingPipe = false;
    }

    /**
     * Flag to move bird
     */
    public void flap() {
        isFlapping = true;
    }

    /**
     * Returns the birds position
     * @return the birds position
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Returns the birds bounding box
     * @return the birds bounding box
     */
    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    /**
     * Update the birds boundary box position
     */
    private void updateBoundingBox() {
        boundingBox.setPosition(position);
    }

    /**
     * Determines if the bird is touching or past the ground
     * @return true if bird it touching the ground, otherwise false
     */
    public Boolean touchingFloor () {
        return position.y <= 0;
    }

    /**
     * Used for testing
     */
    public void setPosition(float x, float y) {
        position.set(x, y);
        updateBoundingBox();
    }

    /**
     * For testing
     */
    public void setVelocity(float x, float y) {
        velocity.set(x, y);
    }

    /**
     * For Testing
     * @return The velocity vector
     */
    public Vector2 getVelocity() {
        return velocity;
    }
}