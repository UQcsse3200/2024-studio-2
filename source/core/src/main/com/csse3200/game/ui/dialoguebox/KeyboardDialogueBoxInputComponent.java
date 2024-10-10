package com.csse3200.game.ui.dialoguebox;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class KeyboardDialogueBoxInputComponent extends InputComponent {
    private final Map<Integer, Boolean> buttonPressed = new HashMap<>();

    public KeyboardDialogueBoxInputComponent() {
        super(5);
    }
    private static final Logger logger = LoggerFactory.getLogger(KeyboardDialogueBoxInputComponent.class);


    /**
     * Triggers combat events on specific keycodes.
     *
     * @return whether the input was processed
     * @see InputProcessor#keyDown(int)
     */
    @Override
    public boolean keyDown(int keycode) {
        if(!this.enabled){
            return false;
        }
        switch (keycode) {
            case Input.Keys.RIGHT:
                buttonPressed.put(Input.Keys.RIGHT, true);
                triggerRightArrowPress();
                return true;
            case Input.Keys.LEFT:
                buttonPressed.put(Input.Keys.LEFT, true);
                triggerLeftArrowPress();
                return true;
            default:
                return false;
        }
    }

    /**
     * Triggers player events on specific keycodes.
     *
     * @return whether the input was processed
     * @see InputProcessor#keyUp(int)
     */
    @Override
    public boolean keyUp(int keycode) {
        if (!this.enabled) {
            return false;
        }

        return buttonPressed.computeIfPresent(keycode, (k, v) -> false) != null;
    }

    private void triggerRightArrowPress() {
        if (Boolean.TRUE.equals(ServiceLocator.getDialogueBoxService().getIsVisible())) {
            logger.info("Pressed Right Arrow");
            ServiceLocator.getDialogueBoxService().getCurrentOverlay().handleForwardButtonClick();
        }
    }

    private void triggerLeftArrowPress() {
        if (Boolean.TRUE.equals(ServiceLocator.getDialogueBoxService().getIsVisible())) {
            logger.info("Pressed Left Arrow");
            ServiceLocator.getDialogueBoxService().getCurrentOverlay().handleBackwardButtonClick();
        }
    }
}
