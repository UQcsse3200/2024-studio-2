package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.tasks.MovementTask;
import com.csse3200.game.components.tasks.WaitTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

import static com.csse3200.game.minigames.maze.components.tasks.MazeMovementUtils.PADDING;

/** A Priority task which listens to stun events and forces the ai to sleep for a duration */
public class WanderIdleTask extends WaitTask {
  /**
   * @param duration How long to wait for, in seconds.
   */
  public WanderIdleTask(float duration) {
    super(duration);
  }
  @Override
  public void start() {
    super.start();
    owner.getEntity().getEvents().trigger("idleStart");
  }
}
