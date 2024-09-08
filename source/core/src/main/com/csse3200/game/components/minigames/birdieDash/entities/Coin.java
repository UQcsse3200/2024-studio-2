package com.csse3200.game.components.minigames.birdieDash.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public class Coin {
    // x and y coordinates for the coin
    private float x;
    private float y;

    // image for the coin
    private final Texture texture;

    /**
     * Constructors to create a coin.
     * Coin will spawn randomly between pipes, both horizontally and vertically.
     *
     * @param pipeWidth  width of the pipes, used to keep the coin between the pipes
     * @param minX       minimum x-coordinate for the coin (left side of the pipe)
     * @param maxX       maximum x-coordinate for the coin (right side of the pipe)
     * @param gapY       y-coordinate for the gap between pipes where the coin can spawn vertically
     * @param gapHeight  height of the gap between pipes to ensure the coin appears within the gap
     */
    public Coin(float pipeWidth, float minX, float maxX, float gapY, float gapHeight) {
        // Randomise x-coordinate to place the coin between the pipes
        this.x = MathUtils.random(minX + pipeWidth, maxX - pipeWidth);

        // Randomise y-coordinate to place the coin within the vertical gap between the pipes
        this.y = MathUtils.random(gapY, gapY + gapHeight);

        this.texture = new Texture("images/minigames/coin.png");
    }

    /**
     * Gets x-coordinate of the coin.
     * @return x-coordinate
     */
    public float getX() {
        return x;
    }

    /**
     * Sets x-coordinate for the coin.
     * @param x the new x-coordinate
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Gets y-coordinate for the coin.
     * @return y-coordinate
     */
    public float getY() {
        return y;
    }

    /**
     * Sets y-coordinate for the coin.
     * @param y the new y-coordinate
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Gets texture used for the coin.
     * @return the coin's texture
     */
    public Texture getTexture() {
        return texture;
    }

    /**
     * Dispose the coin's texture to free up the resources. It should be called when the coin is no longer needed.
     */
    public void dispose() {
        texture.dispose();
    }
}