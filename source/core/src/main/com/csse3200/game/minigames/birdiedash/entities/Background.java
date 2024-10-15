package com.csse3200.game.minigames.birdiedash.entities;

/**
 * This claass makes a moving background for the Birdie Dash Mini-Game
 */
public class Background {
    private final float startSpeed;
    private final float screenWidth;
    public static float xBG1;
    public static float xBG2;

    public Background(float startSpeed, float screenWidth) {
        this.startSpeed = startSpeed;
        this.screenWidth = screenWidth;
    }

    /**
     * Updates the background to be moving
     * @param dt time since last update
     */
    public void update(float dt) {
        xBG1 -= startSpeed * dt;
        xBG2 = xBG1 + screenWidth;
        if (xBG1 <= -screenWidth) {
            xBG1 = 0; xBG2 = screenWidth;
        }
    }
}
