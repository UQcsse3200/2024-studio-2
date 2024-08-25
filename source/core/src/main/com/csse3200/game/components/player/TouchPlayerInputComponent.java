package com.csse3200.game.components.player;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.utils.math.Vector2Utils;
import com.badlogic.gdx.InputProcessor;

/**
 * Input handler for the player for keyboard and touch (mouse) input.
 * This input handler uses keyboard and touch input.
 */
public class TouchPlayerInputComponent extends InputComponent {
  private final Vector2 walkDirection = Vector2.Zero.cpy();
  public TouchPlayerInputComponent() {
    super(5);
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
      case Input.Keys.UP:
        walkDirection.add(Vector2Utils.UP);
        triggerWalkEvent();
        return true;
      case Input.Keys.LEFT:
        walkDirection.add(Vector2Utils.LEFT);
        triggerWalkEvent();
        return true;
      case Input.Keys.DOWN:
        walkDirection.add(Vector2Utils.DOWN);
        triggerWalkEvent();
        return true;
      case Input.Keys.RIGHT:
        walkDirection.add(Vector2Utils.RIGHT);
        triggerWalkEvent();
        return true;
      case Input.Keys.ESCAPE:
        entity.getEvents().trigger("restMenu");
        return true;
      case Input.Keys.Q:
        entity.getEvents().trigger("quest");
        return true;
      case Input.Keys.O:
        entity.getEvents().trigger("addMainGameScreen");
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
    if(!this.enabled){
      return false;
    }
    switch (keycode) {
      case Input.Keys.UP:
        walkDirection.sub(Vector2Utils.UP);
        triggerWalkEvent();
        return true;
      case Input.Keys.LEFT:
        walkDirection.sub(Vector2Utils.LEFT);
        triggerWalkEvent();
        return true;
      case Input.Keys.DOWN:
        walkDirection.sub(Vector2Utils.DOWN);
        triggerWalkEvent();
        return true;
      case Input.Keys.RIGHT:
        walkDirection.sub(Vector2Utils.RIGHT);
        triggerWalkEvent();
        return true;
      default:
        return false;
    }
  }

  /**
   * Triggers the player attack.
   * @return whether the input was processed
   * @see InputProcessor#touchDown(int, int, int, int)
   */
  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    if(!this.enabled){
      return false;
    }
    entity.getEvents().trigger("attack");
    return true;
  }

  private void triggerWalkEvent() {
    if (walkDirection.epsilonEquals(Vector2.Zero)) {
      entity.getEvents().trigger("walkStop");
    } else {
      entity.getEvents().trigger("walk", walkDirection);
    }
  }


}
