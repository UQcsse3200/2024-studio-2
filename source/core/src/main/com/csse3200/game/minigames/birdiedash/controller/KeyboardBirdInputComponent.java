package com.csse3200.game.minigames.birdiedash.controller;

import com.badlogic.gdx.Input.Keys;
import com.csse3200.game.input.InputComponent;

/**
 * CLass for keyboard inputs for birdie dash
 */
public class KeyboardBirdInputComponent extends InputComponent {


    public KeyboardBirdInputComponent() {
        super(5);
    }

    /**
     * Define keys for events
     * @param keycode one of the constants in Input.Keys
     * @return true if key pressed
     */
    @Override
    public boolean keyDown(int keycode) {
        if(!this.enabled){
            return false;
        }
        return switch (keycode) {
            case Keys.SPACE, Keys.W, Keys.UP -> {
                entity.getEvents().trigger("flap");
                yield true;
            }
            default -> false;
        };
    }
}