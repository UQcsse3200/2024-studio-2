package com.csse3200.game.components.minigames.birdieDash.entities;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;

public class Bird {
    private Vector2 position;
    private Vector2 velocity;
    private Rectangle boundingBox;
    private static final float GRAVITY = -15f;
    private static final float FLAP_STRENGTH = 500;
    private static final float BIRD_WIDTH = 60f;
    private static final float BIRD_HEIGHT = 45f;
    private static final int GAME_HEIGHT = 1200;
    // testing
    private boolean collidingPipe;
    private boolean top;
    private boolean isFlapping;
    public Bird(float x, float y) {
        position = new Vector2(x, y);
        velocity = new Vector2(0, 0);
        boundingBox = new Rectangle(x, y, BIRD_WIDTH, BIRD_HEIGHT);
        collidingPipe = false;
        isFlapping = false;
    }
    public void update(float deltaTime) {
        if (position.y > 0) {
            if(top) {
                if(velocity.y != 0) {
                    velocity.y = 0;
                }
            } else {
                velocity.add(0, GRAVITY);
            }
        } else {
            position.y = 0;
            velocity.y = 0;
        }
        if(isFlapping) {
            isFlapping = false;
            velocity.y = FLAP_STRENGTH;
        }
        if(position.x < 960 && !collidingPipe) {
            velocity.x = 50;
        } else {
            velocity.x = 0;
        }
        velocity.scl(deltaTime);
        position.add(velocity);
        if(collidingPipe) {
            position.sub(deltaTime * 200, 0);
        }
        velocity.scl(1 / deltaTime);
        if (position.y + BIRD_HEIGHT > GAME_HEIGHT) {
            position.y = GAME_HEIGHT - BIRD_HEIGHT;
        }
        updateBoundingBox();

    }

    public void setCollidingTopPipe() {
        top = true;
    }

    public void unsetCollidingTopPipe() {
        top = false;
    }
    /**
     * For collisions
     */
    public void setCollidingPipe() {
        collidingPipe = true;
    }

    /**
     * For collisions.
     */
    public void unsetCollidingPipe() {
        collidingPipe = false;
    }
    public void flapp() {
        isFlapping = true;
    }
    public Vector2 getPosition() {
        return position;
    }
    public Rectangle getBoundingBox() {
        return boundingBox;
    }
    private void updateBoundingBox() {
        boundingBox.setPosition(position);
    }
}