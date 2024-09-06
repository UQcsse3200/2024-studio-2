package com.csse3200.game.components.minigame.flappybird.entities;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;

public class Bird {
    private Vector2 position;
    private Vector2 velocity;
    private Rectangle boundingBox;

    private static final float GRAVITY = -15f;
    private static final float FLAP_STRENGTH = 300f;
    private static final float BIRD_WIDTH = 60f;
    private static final float BIRD_HEIGHT = 45f;
    private static final int GAME_HEIGHT = 600;

    public Bird(float x, float y) {
        position = new Vector2(x, y);
        velocity = new Vector2(0, 0);
        boundingBox = new Rectangle(x, y, BIRD_WIDTH, BIRD_HEIGHT);
    }

    public void update(float deltaTime) {
        if (position.y > 0) {
            velocity.add(0, GRAVITY);
        } else {
            position.y = 0;
            velocity.y = 0;
        }

        velocity.scl(deltaTime);
        position.add(0, velocity.y);
        velocity.scl(1 / deltaTime);


        if (position.y + BIRD_HEIGHT > GAME_HEIGHT) {
            position.y = GAME_HEIGHT - BIRD_HEIGHT;
        }

        updateBoundingBox();
    }

    public void flap() {
        velocity.y = FLAP_STRENGTH;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    private void updateBoundingBox() {
        boundingBox.setPosition(position.x, position.y);
    }
}
