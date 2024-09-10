package com.csse3200.game.components.minigames.birdieDash.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Class for the spikes in the birdie dash game
 */
public class Spike {
    private final float WIDTH = 100;
    private final float HEIGHT = 1200;
    private final Vector2 position;
    private final Rectangle spikeBoundary;

    public Spike(float position) {
        this.position = new Vector2(position, 0);
        this.spikeBoundary = new Rectangle(0,
                0,
                WIDTH,
                HEIGHT);
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
        return WIDTH;
    }

    /**
     * Get the spikes height
     * @return the spikes height
     */
    public float getHeight() {
        return HEIGHT;
    }

    /**
     * Ge the spikes boundary
     * @return the spikes boundary
     */
    public Rectangle getSpikeBoundary() {
        return this.spikeBoundary;
    }
}
