package com.csse3200.game.components.minigames.birdieDash.controller;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.csse3200.game.components.minigames.Direction;
import com.csse3200.game.input.InputComponent;


public class KeyboardBirdInputComponent extends InputComponent {


    public KeyboardBirdInputComponent() {
        super(5);
    }

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