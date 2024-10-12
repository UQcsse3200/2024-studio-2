package com.csse3200.game.minigames.birdiedash.entities;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Class for the pipes in birdie dash mini-game
 */
public class Pipe{
    private final int GAME_WIDTH = 1920;
    private final int GAME_HEIGHT = 1200;

    private Vector2 topPosition;
    private Vector2 bottomPosition;

    // Set width of pipe
    private final float width = 150;
    private float height;
    private final float PIPE_GAP = 300;
    private final float MIN_HEIGHT = 50;
    private final float MAX_HEIGHT = 850;

    // Game physics
    private final float start_speed;

    // Collision rectangles
    private final Rectangle bottomPipe;
    private final Rectangle topPipe;
    private final Random random;

    public Pipe(float start, float start_speed){
        this.random = new Random();
        this.height = random.nextFloat(MIN_HEIGHT,MAX_HEIGHT);
        this.bottomPosition = new Vector2(start,0);
        this.topPosition=new Vector2(start, height + PIPE_GAP);
        this.start_speed = start_speed;
        this.bottomPipe = new Rectangle(bottomPosition.x, bottomPosition.y, width, height);
        this.topPipe = new Rectangle(topPosition.x, topPosition.y, width, getHeightTop());
    }

    /**
     * chane the pipes position (as the screen moves along)
     * @param dt the time since it last changed
     */
    public void changePosition(float dt){
        dt = dt * start_speed;
        this.bottomPosition.sub(dt,0);
        this.topPosition.sub(dt,0);
        setRectangles();
        if(pipeOffScreen()) {
            respawnPipe();
        }
    }

    /**
     * spawns a pipe
     */
    public void respawnPipe() {
        height = random.nextFloat(MIN_HEIGHT,MAX_HEIGHT);
        this.bottomPosition = new Vector2(GAME_WIDTH - width/2 + 960, 0);
        this.topPosition=new Vector2(GAME_WIDTH - width/2 + 960, height + PIPE_GAP);
        setRectangles();
    }

    /**
     * Determines if the pipe is off-screen
     * @return true if pipe os off the screen, false otherwise
     */
    private boolean pipeOffScreen() {
        return this.bottomPosition.x + width / 2 < 0;
    }

    /**
     * Get the bottom pipe position
     * @return the bottom pipe position
     */
    public Vector2 getPositionBottom(){
        return this.bottomPosition;
    }

    /**
     * Get the top pipe position
     * @return the top pipe position
     */
    public Vector2 getPositionTop(){
        return this.topPosition;
    }

    /**
     * Get the pipes width
     * @return the pipe width
     */
    public float getWidth(){
        return this.width;
    }

    /**
     * Get the bottom pipes height
     * @return the pipe height
     */
    public float getHeightBottom(){
        return this.height;
    }

    /**
     * Get the top pipes height
     * @return the pipe height
     */
    public float getHeightTop(){
        return GAME_HEIGHT - height -PIPE_GAP;
    }

    /**
     * Get the bottom pipe
     * @return the bottom pipe
     */
    public Rectangle getBottomPipe() {
        return bottomPipe;
    }

    /**
     * Get the top pipe
     * @return the top pipe
     */
    public Rectangle getTopPipe() {
        return topPipe;
    }

    /**
     * Set the boundary of the pipes
     */
    private void setRectangles() {
        this.bottomPipe.setPosition(bottomPosition.x -width/2, bottomPosition.y);
        this.bottomPipe.setSize(width, height);
        this.topPipe.setPosition(topPosition.x - width/2, topPosition.y);
        this.topPipe.setSize(width, getHeightTop());
    }

    /**
     * Method for testing
     */
    public void setPosition(float x) {
        this.bottomPosition.set(x, 0);
        this.topPosition.set(x, height + PIPE_GAP);
        setRectangles();
    }
}
