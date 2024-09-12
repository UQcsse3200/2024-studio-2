package com.csse3200.game.components.combat;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.utils.math.Vector2Utils;

/**
 * Input handler for combat for keyboard and touch (mouse) input.
 * This input handler uses keyboard and touch input.
 */
public class TouchCombatInputComponent extends InputComponent {

  public TouchCombatInputComponent() {
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
      case Input.Keys.UP:
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
      return false;
  }

  private void triggerQuickTimeBtnPress() {
    entity.getEvents().trigger("quickTimeBtnPress");
  }
}
