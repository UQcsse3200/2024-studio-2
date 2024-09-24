package com.csse3200.game.minigames.maze.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.tasks.MovementTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

import static com.csse3200.game.minigames.maze.components.tasks.MazeMovementUtils.PADDING;

/** A Priority task which listens to stun events and forces the ai to sleep for a duration */
public class TimedTask extends DefaultTask implements PriorityTask {
  private final int priority;
  private float remaining;

  protected GameTime gameTime = ServiceLocator.getTimeSource();

  /**
   * @param priority Task priority when task is active (0 when not active).
   */
  public TimedTask(int priority) {
    super();
    this.priority = priority;
    this.remaining = 0;
  }

  @Override
  public void update() {
    remaining = Math.max(0, remaining - gameTime.getDeltaTime());
  }

  public void setRemainingActiveTime(float duration) {
    remaining = duration;
  }

  public void setMinRemainingActiveTime(float duration) {
    remaining = Math.max(duration, remaining);
    System.out.println("Setting min remaining time " + remaining);
  }

  public void addRemainingActiveTime(float duration) {
    remaining += duration;
  }

  public float getRemainingActiveTime() {
    return remaining;
  }

  public boolean hasRemainingActiveTime() {
    return remaining > 0;
  }

  @Override
  public int getPriority() {
    return hasRemainingActiveTime() ? priority : 0;
  }
}
