package com.csse3200.game.components.minigames.birdieDash.entities;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;

/**
 * Class for the bird in the mini-game birdie dash
 */
public class Bird {
    private final Vector2 position;
    private final Vector2 velocity;
    private final Rectangle boundingBox;
    private static final float GRAVITY = -2600f;
    private static final float FLAP_STRENGTH = 1200;
    private static final float BIRD_WIDTH = 60f;
    private static final float BIRD_HEIGHT = 45f;
    private static final int GAME_HEIGHT = 1200;

    private boolean collidingPipe;
    private boolean collideTopOfPipe;
    private boolean isFlapping;

    public Bird(float x, float y) {
        position = new Vector2(x, y);
        velocity = new Vector2(0, 0);
        boundingBox = new Rectangle(x, y, BIRD_WIDTH, BIRD_HEIGHT);
        collidingPipe = false;
        collideTopOfPipe = false;
        isFlapping = false;
    }

    /**
     * Updates the birds position
     * @param deltaTime the time from the last position update
     * @param multiplier Used to speed up the game
     */
    public void update(float deltaTime, float multiplier) {

        if(isFlapping) {
            isFlapping = false;
            velocity.y = FLAP_STRENGTH;
        }

        if (position.y > 0) {
            if(collideTopOfPipe) {
                if (velocity.y != 0) {
                    velocity.y = 0;
                }
            } else {
                velocity.add(0, GRAVITY * deltaTime);
            }
        } else { // Ensure bird doesn't fly below screen
            position.y = position.x = 0;
        }
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
        return !(position.y > 0);
    }
}