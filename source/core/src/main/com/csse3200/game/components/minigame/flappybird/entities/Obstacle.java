package com.csse3200.game.components.minigame.flappybird.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Obstacle {
    private Vector2 topPosition;
    private Vector2 bottomPosition;
    private Rectangle top;
    private Rectangle bottom;
    private final int GAME_WIDTH = 800;
    private final int GAME_HEIGHT = 600;
    private final float WIDTH = 150;
    private float height;
    private final float GAP = 150;
    private final float MIN = 50;
    private final float TALLEST = 300;
    private Random rand;

    public Obstacle(float start) {
        this.rand = new Random();
        this.height = rand.nextFloat(MIN, TALLEST);
        this.bottomPosition = new Vector2(start,0);
        this.topPosition = new Vector2(start, height + GAP);
    }

//    public void setPosition(int x, int y) {
//        this.postion = new Vector2(x, y);
//    }

    public Vector2 getPositionBottom() {
        return this.bottomPosition;
    }

    public Vector2 getPositionTop() {return this.topPosition;}

    public float getWidth() {
        return this.WIDTH;
    }


    public float getHeightBottom() {
        return this.height;
    }

    public float getHeightTop() {return GAME_HEIGHT - height-GAP;}


}
