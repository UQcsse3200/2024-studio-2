package com.csse3200.game.minigames.birdiedash.entities;

public class Background {
    private final float start_speed;
    private final float screen_width;
    public float xBG1;
    public float xBG2;

    public Background(float start_speed, float screen_width) {
        this.start_speed = start_speed;
        this.screen_width = screen_width;
    }

    public void update(float dt) {
        xBG1 -= start_speed * dt;
        xBG2 = xBG1 + screen_width;
        if (xBG1 <= -screen_width) {
            xBG1 = 0; xBG2 = screen_width;
        }
    }
}
