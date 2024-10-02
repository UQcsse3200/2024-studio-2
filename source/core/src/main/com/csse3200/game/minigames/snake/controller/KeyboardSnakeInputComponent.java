package com.csse3200.game.minigames.snake.controller;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.csse3200.game.minigames.Direction;
import com.csse3200.game.input.InputComponent;

/**
 * Input handler for the snake mini-game.
 */
public class KeyboardSnakeInputComponent extends InputComponent {

    /**
     * Construct new snake input component with priority 5.
     */
    public KeyboardSnakeInputComponent() {
        super(5);
    }

    /**
     * Triggers snake move events on specific keycodes.
     *
     * @return whether the input was processed
     * @see InputProcessor#keyDown(int)
     */
    @Override
    public boolean keyDown(int keycode) {
        if(!this.enabled){
            return false;
        }
        return switch (keycode) {
            case Keys.UP, Keys.W -> {
                entity.getEvents().trigger("move", Direction.UP);
                yield true;
            }
            case Keys.LEFT, Keys.A -> {
                entity.getEvents().trigger("move", Direction.LEFT);
                yield true;
            }
            case Keys.DOWN, Keys.S -> {
                entity.getEvents().trigger("move", Direction.DOWN);
                yield true;
            }
            case Keys.RIGHT, Keys.D -> {
                entity.getEvents().trigger("move", Direction.RIGHT);
                yield true;
            }
            default -> false;
        };
    }
}