package com.csse3200.game.components.minigames.birdieDash.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Coin {
    // x and y coordinates for the coin
    private Vector2 position;
    private Rectangle boundary;
    private final float MIN_Y = 100;
    private final float MAX_Y = 1100;
    private final float WIDTH = 100;
    private final float HEIGHT = 100;
    private final float GAME_WIDTH = 1920;
    private float speed;
    private final Random random;


    /**
     * Constructors to create a coin.
     * Coin will spawn randomly between pipes, both horizontally and vertically.
     *
     */

    public Coin(float start, float speed) {
        this.random = new Random();
        this.position = new Vector2(start, random.nextFloat(MIN_Y, MAX_Y));
        this.speed = speed;
        this.boundary = new Rectangle(position.x, position.y, WIDTH, HEIGHT);
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getWidth() {
        return WIDTH;
    }

    public float getHeight() {
        return HEIGHT;
    }

    public void changePosition(float dt){
        dt = dt * speed;
        this.position.sub(dt,0);
        setBoundary();
        if(coinOffScreen()) {
            respawnCoin();
        }
    }

    public void respawnCoin() {
        position = new Vector2(GAME_WIDTH + 960, random.nextFloat(MIN_Y, MAX_Y));
        setBoundary();
    }

    private boolean coinOffScreen() {
        if(this.position.x + 100 < 0) {
            return true;
        }
        return false;
    }

    private void setBoundary() {
        boundary.setPosition(position.x, position.y);
    }

}