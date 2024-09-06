package com.csse3200.game.components.minigames.birdieDash.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.csse3200.game.components.minigames.birdieDash.entities.Bird;

public class BirdieDashController {
    private Bird bird;
    public void FlappyBirdController(Bird bird) {
        this.bird = bird;
    }
    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            bird.flap();
        }
    }
}