package com.csse3200.game.components.player;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.math.Vector2Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Input handler for the player for keyboard and touch (mouse) input.
 * This input handler only uses keyboard input.
 */
public class KeyboardPlayerInputComponent extends InputComponent {
  private final Vector2 walkDirection = Vector2.Zero.cpy();
  private final Map<Integer, Boolean> buttonPressed = new HashMap<>();

  public KeyboardPlayerInputComponent() {
    super(5);
    ServiceLocator.getEventService().getGlobalEventHandler().addListener("resetVelocity",this::resetVelocity);
    buttonPressed.put(Keys.W, false);
    buttonPressed.put(Keys.A, false);
    buttonPressed.put(Keys.S, false);
    buttonPressed.put(Keys.D, false);
  }

  private void resetVelocity () {
    walkDirection.set(Vector2.Zero);
    buttonPressed.put(Keys.W, false);
    buttonPressed.put(Keys.A, false);
    buttonPressed.put(Keys.S, false);
    buttonPressed.put(Keys.D, false);
  }

  /**
   * Triggers player events on specific keycodes.
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
        walkDirection.add(Vector2Utils.UP);
        triggerWalkEvent();
        return true;
      case Keys.A:
        buttonPressed.put(Keys.A, true);
        walkDirection.add(Vector2Utils.LEFT);
        triggerWalkEvent();
        return true;
      case Keys.S:
        buttonPressed.put(Keys.S, true);
        walkDirection.add(Vector2Utils.DOWN);
        triggerWalkEvent();
        return true;
      case Keys.D:
        buttonPressed.put(Keys.D, true);
        walkDirection.add(Vector2Utils.RIGHT);
        triggerWalkEvent();
        return true;
      case Keys.SPACE:
        entity.getEvents().trigger("attack");
        return true;
      case Keys.ESCAPE:
        entity.getEvents().trigger("restMenu");
        return true;
      case Keys.Q:
        entity.getEvents().trigger("quest");
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
    if(!this.enabled) {
      return false;
    }
      if (buttonPressed.containsKey(keycode) && buttonPressed.get(keycode).equals(false)) {
      return true;
    }
    switch (keycode) {
      case Keys.W:
        walkDirection.sub(Vector2Utils.UP);
        triggerWalkEvent();
        return true;
      case Keys.A:
        walkDirection.sub(Vector2Utils.LEFT);
        triggerWalkEvent();
        return true;
      case Keys.S:
        walkDirection.sub(Vector2Utils.DOWN);
        triggerWalkEvent();
        return true;
      case Keys.D:
        walkDirection.sub(Vector2Utils.RIGHT);
        triggerWalkEvent();
        return true;
      case Keys.E:
        entity.getEvents().trigger("toggleInventory");
        return true;
      case Keys.P:
        entity.getEvents().trigger("pickUpItem");
        return true;
      default:
        return false;
    }
  }

  private void triggerWalkEvent() {
    if (walkDirection.epsilonEquals(Vector2.Zero)) {
      entity.getEvents().trigger("walkStop");
    } else {
      entity.getEvents().trigger("walk", walkDirection);
    }
  }
}