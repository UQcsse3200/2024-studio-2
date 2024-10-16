package com.csse3200.game.minigames.birdiedash.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Class for the spikes in the birdie dash game
 */
public class Spike {
    private final static float width = 100;
    private final static float height = 1200;
    private final Vector2 position;
    private final Rectangle spikeBoundary;

    public Spike(float position) {
        this.position = new Vector2(position, 0);
        this.spikeBoundary = new Rectangle(0,
                0,
                width,
                height);
    }

    /**
     * Get the spikes position
     * @return the spikes position
     */
    public Vector2 getPosition() {
        return this.position;
    }

    /**
     * Get the spikes width
     * @return the spikes width
     */
    public float getWidth() {
        return width;
    }

    /**
     * Get the spikes height
     * @return the spikes height
     */
    public float getHeight() {
        return height;
    }

    /**
     * Ge the spikes boundary
     * @return the spikes boundary
     */
    public Rectangle getSpikeBoundary() {
        return this.spikeBoundary;
    }
}
