package com.csse3200.game.components.tasks;

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
