package com.csse3200.game.components.combat;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.utils.math.Vector2Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Input handler for combat for keyboard and touch (mouse) input.
 * This input handler only uses keyboard input.
 */
public class KeyboardCombatInputComponent extends InputComponent {
  private final Map<Integer, Boolean> buttonPressed = new HashMap<>();

  public KeyboardCombatInputComponent() {
    super(5);
  }

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
      case Keys.W:
        buttonPressed.put(Keys.W, true);
        triggerQuickTimeBtnPress();
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
      return buttonPressed.containsKey(keycode) && buttonPressed.get(keycode).equals(false);
  }

  private void triggerQuickTimeBtnPress() {
    entity.getEvents().trigger("quickTimeBtnPress");
  }
}