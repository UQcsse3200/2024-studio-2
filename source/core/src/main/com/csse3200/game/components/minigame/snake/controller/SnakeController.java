package com.csse3200.game.components.minigame.snake.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.csse3200.game.components.minigame.Direction;
import com.csse3200.game.screens.MiniGameMenuScreen;

public class SnakeController {

    public SnakeController() {}
    public Direction getInputDirection() {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            return Direction.RIGHT;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            return Direction.LEFT;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            return Direction.UP;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            return Direction.DOWN;
        }
        return Direction.ZERO;
    }

    public int handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {  // Restart game
            return 1;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {  // Go to minigames menu
            Gdx.gl.glClearColor(248f / 255f, 249f / 255f, 178f / 255f, 1f);
            return 2;
        }
        return 0;
    }
}
