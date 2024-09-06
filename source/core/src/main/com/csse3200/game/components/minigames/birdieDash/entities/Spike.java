package com.csse3200.game.components.minigames.birdieDash.entities;

import com.badlogic.gdx.math.Vector2;

public class Spike {
    private final float WIDTH = 400;
    private final float HEIGHT = 1200;
    private final Vector2 position;

    public Spike() {
        this.position = new Vector2(0, 0);
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
