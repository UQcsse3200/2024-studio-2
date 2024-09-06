package com.csse3200.game.components.minigames.birdieDash.entities;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Pipe{
    private final int GAME_WIDTH = 800;
    private final int GAME_HEIGHT = 600;

    private Vector2 topPosition;
    private Vector2 bottomPosition;

    // Set width of pipe
    private final float width = 150;
    private float height;
    private final float PIPE_GAP = 150;
    private final float MIN_HEIGHT = 50;
    private final float MAX_HEIGHT = 300;

    // Game physics
    private final float start_speed;
    private Random random;

    public Pipe(float start, float start_speed){
        this.random =new Random();
        this.height = random.nextFloat(MIN_HEIGHT,MAX_HEIGHT);
        this.bottomPosition = new Vector2(start,0);
        this.topPosition=new Vector2(start, height + PIPE_GAP);
        this.start_speed = start_speed;
    }

    public void changePosition(float dt){
        dt = dt * start_speed;
        this.bottomPosition.sub(dt,0);
        this.topPosition.sub(dt,0);
    }

    public Vector2 getPositionBottom(){
        return this.bottomPosition;
    }

    public Vector2 getPositionTop(){
        return this.topPosition;
    }

    public float getWidth(){
        return this.width;
    }


    public float getHeightBottom(){
        return this.height;
    }

    public float getHeightTop(){
        return GAME_HEIGHT - height -PIPE_GAP;
    }
}
