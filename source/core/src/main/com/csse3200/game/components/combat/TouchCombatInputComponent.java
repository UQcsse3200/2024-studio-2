package com.csse3200.game.components.combat;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.csse3200.game.input.InputComponent;

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
      case Keys.UP:
       triggerQuickTimeBtnPress(Keys.UP);
        return true;
      case Keys.LEFT:
//        triggerQuickTimeBtnPress(Keys.LEFT);
        return true;
      case Keys.RIGHT:
//        triggerQuickTimeBtnPress(Keys.RIGHT);
      case Keys.DOWN:
//        triggerQuickTimeBtnPress(Keys.DOWN);
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

  /**
   * Responsible for triggering a button-pressed event
   * during a quick-time event
   *
   * @param keycode the key pressed
   */
  private void triggerQuickTimeBtnPress(int keycode) {
//    entity.getEvents().trigger("quickTimeBtnPress", keycode);

      entity.getEvents().trigger("quickTimeBtnPress", "UP");

  }
}
