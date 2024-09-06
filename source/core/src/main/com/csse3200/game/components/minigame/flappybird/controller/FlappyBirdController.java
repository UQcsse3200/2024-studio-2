package com.csse3200.game.components.minigame.flappybird.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.csse3200.game.components.minigame.flappybird.entities.Bird;

public class FlappyBirdController {

    private Bird bird;

    public FlappyBirdController(Bird bird) {
        this.bird = bird;
    }

    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            bird.flap();
        }
    }
}
