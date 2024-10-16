package com.csse3200.game.minigames;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.overlays.Overlay;

/**
 * Input handler for the snake mini-game.
 */
public class KeyboardMiniGameInputComponent extends InputComponent {

    /**
     * Construct new snake input component with priority 5.
     */
    public KeyboardMiniGameInputComponent() {
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
            case Keys.R -> {
                entity.getEvents().trigger("restart");
                yield true;
            }
            case Keys.ESCAPE -> {
                entity.getEvents().trigger("addOverlay", Overlay.OverlayType.PAUSE_OVERLAY);
                yield true;
            }
            default -> false;
        };
    }

    @Override
    public boolean keyUp(int keycode) {
        if(!this.enabled){
            return false;
        }
        if (keycode == Keys.BACKSPACE) {
            entity.getEvents().trigger("exit");
            return true;
        }
        return false;
    }
}