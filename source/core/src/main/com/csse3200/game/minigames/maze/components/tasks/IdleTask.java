package com.csse3200.game.minigames.maze.components.tasks;

import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

/** A Priority task which listens to stun events and forces the ai to sleep for a duration */
public class IdleTask extends DefaultTask implements PriorityTask {
  private final int priority;

  /**
   * @param priority Task priority when task is active (0 when not active).
   */
  public IdleTask(int priority) {
    super();
    this.priority = priority;
  }

  public void setActive() {
    status = Status.ACTIVE;
  }

  public void setInactive() {
    status = Status.INACTIVE;
  }

  /**
   * Trigger idle event for animations.
   */
  @Override
  public void start() {
    owner.getEntity().getEvents().trigger("chaseStart");
  }

  @Override
  public int getPriority() {
    return status == Status.ACTIVE ? priority : 0;
  }
}
