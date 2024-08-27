package com.csse3200.game.components.minigame.snake.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.csse3200.game.components.minigame.Direction;


/**
 * Handles player input for controlling the snake and managing game events.
 */
public class SnakeController {

    public SnakeController() {}

    /**
     * Gets the direction based on player input.
     *
     * @return The direction the snake should move based on the current input.
     */
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

    /**
     * Handles specific game event inputs such as restarting or exiting.
     *
     * @return The event triggered by the player's input, or NONE if no event is triggered.
     */
    public Events handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {  // Restart game
            return Events.RESTART;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {  // Go to minigames menu
            Gdx.gl.glClearColor(248f / 255f, 249f / 255f, 178f / 255f, 1f);
            return Events.EXIT_TO_MENU;
        }
        return Events.NONE;
    }
}
