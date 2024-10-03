package com.csse3200.game.components.player;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.inventory.InventoryComponent;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.inventory.Inventory;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.math.Vector2Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Input handler for the player for keyboard and touch (mouse) input.
 * This input handler only uses keyboard input.
 */
public class KeyboardPlayerInputComponent extends InputComponent {
  private final Vector2 walkDirection = new Vector2();
  private final Map<Integer, Boolean> buttonPressed = new HashMap<>();
  private boolean paralyzed = false;
  private long startTime = 0;
  GameTime timeSource;

  /**
   * Constructor, adds values to the button pressed map to avoid player gaining
   * unintended velocity when entering and exiting screens while holding a wasd button
   */
  public KeyboardPlayerInputComponent() {
    super(5);
    buttonPressed.put(Keys.W, false);
    buttonPressed.put(Keys.A, false);
    buttonPressed.put(Keys.S, false);
    buttonPressed.put(Keys.D, false);
    timeSource = ServiceLocator.getTimeSource();
  }

  /**
   * Resets the player's velocity to 0, 'unpresses' all keys.
   */
  public void resetVelocity() {
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
    if (!this.enabled) {
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
      case Keys.I:
        entity.getEvents().trigger("statsInfo");
        return true;
      case Keys.NUM_1: // TEMPORARY: Press 1 to spawn the land boss
        entity.getEvents().trigger("defeatLandBoss");
        return true;
      case Keys.NUM_2: // TEMPORARY: Press 2 to spawn the water boss
        entity.getEvents().trigger("defeatWaterBoss");
        return true;
      case Keys.NUM_3: // TEMPORARY: Press 3 to spawn the air boss
        entity.getEvents().trigger("defeatAirBoss");
        return true;
      case Keys.U:
        entity.getEvents().trigger("unlockNextArea");
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
    if (buttonPressed.containsKey(keycode)) {
      buttonPressed.put(keycode, false);
      updateWalkDirection();
      return true;
    }
    switch (keycode) {
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

  /**
   * Triggers camera zoom for player, calling the associated
   * event listener
   *
   * @param amountX the horizontal zoom amount
   * @param amountY the vertical zoom amount
   * @return whether the input was processed
   */
  @Override
  public boolean scrolled(float amountX, float amountY) {
    entity.getEvents().trigger("cameraZoom", amountX, amountY);
    return true;
  }

  private void triggerWalkEvent() {
    if (paralyzed) {
      if (timeSource.getTimeSince(startTime) > 2000) {
        unParalyze();
      } else {
        resetVelocity();
        entity.getEvents().trigger("walkStop");
        return;
      }
    }
    if (walkDirection.len() > 0) {
      walkDirection.nor(); // Normalize direction vector
      entity.getEvents().trigger("walk", walkDirection);
    } else {
      entity.getEvents().trigger("walkStop");
    }
  }

  private void updateWalkDirection() {
    walkDirection.set(Vector2.Zero);
    if (buttonPressed.get(Keys.W)) {
      walkDirection.add(Vector2Utils.UP);
    }
    if (buttonPressed.get(Keys.A)) {
      walkDirection.add(Vector2Utils.LEFT);
    }
    if (buttonPressed.get(Keys.S)) {
      walkDirection.add(Vector2Utils.DOWN);
    }
    if (buttonPressed.get(Keys.D)) {
      walkDirection.add(Vector2Utils.RIGHT);
    }
    triggerWalkEvent();
  }
  
  public boolean isParalyzed() {
    return paralyzed;
  }
  
  public void paralyze() {
    paralyzed = true;
    startTime = timeSource.getTime();
    triggerWalkEvent();
  }
  
  public void unParalyze() {
    paralyzed = false;
    startTime = 0;
  }
}
