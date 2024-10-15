package com.csse3200.game.minigames.birdiedash.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import java.util.Random;

/**
 * Class for the coin in the birdie dash mini-game
 */
public class Coin {
    // x and y coordinates for the coin
    private Vector2 position;
    private final Rectangle boundary;
    private final static float minY = 100;
    private final static float maxY = 1100;
    private final static float width = 80;
    private final static float height = 80;
    private final static float gameWidth = 1920;
    private final float speed;
    private final Random random;

    /**
     * Constructors to create a coin.
     * Coin will spawn randomly between pipes, both horizontally and vertically.
     *
     */
    public Coin(float start, float speed) {
        this.random = new Random();
        this.position = new Vector2(start, random.nextFloat(minY, maxY));
        this.speed = speed;
        this.boundary = new Rectangle(position.x, position.y, width, height);
    }

    /**
     * Get the coins position
     * @return the coins position
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * returns the coins width
     * @return the coins width
     */
    public float getWidth() {
        return width;
    }

    /**
     * returns the coins height
     * @return the coins height
     */
    public float getHeight() {
        return height;
    }

    /**
     * Changed the coins position (as the screen moves)
     * @param dt the time since the last update
     */
    public void changePosition(float dt){
        dt = dt * speed;
        this.position.sub(dt,0);
        setBoundary();
        if(coinOffScreen()) {
            respawnCoin();
        }
    }

    /**
     * spawns new coin
     */
    public void respawnCoin() {
        position = new Vector2(this.position.x + gameWidth + 960, random.nextFloat(minY, maxY));
        setBoundary();
    }

    /**
     * Determines if the coin is off the screen
     * @return true if coin off-screen, false otherwise.
     */
    private boolean coinOffScreen() {
        return this.position.x + width / 2 < 0;
    }

    /**
     * set coin boundary
     */
    private void setBoundary() {
        boundary.setCenter(position.x, position.y);
    }

    /**
     * get the coin boundary
     * @return the coin boundary
     */
    public Rectangle getBoundary() {
        return boundary;
    }

    /**
     * Used for testing
     */
    public void setPosition(float x, float y) {
        position.set(x, y);
        setBoundary();
    }
}