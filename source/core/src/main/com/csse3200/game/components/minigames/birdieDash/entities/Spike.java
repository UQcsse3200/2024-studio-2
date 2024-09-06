package com.csse3200.game.components.minigames.birdieDash.entities;

import com.badlogic.gdx.math.Vector2;

public class Spike {
    private final float WIDTH = 100;
    private final float HEIGHT = 1200;
    private final Vector2 position;

    public Spike(float position) {
        this.position = new Vector2(position, 0);
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public float getWidth() {
        return WIDTH;
    }

    public float getHeight() {
        return HEIGHT;
    }


}
