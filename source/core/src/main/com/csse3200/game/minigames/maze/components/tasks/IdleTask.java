package com.csse3200.game.minigames.maze.components.tasks;

import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;

/**
 * A Priority task which listens to stun events and forces the AI to sleep for a duration
 */
public class IdleTask extends DefaultTask implements PriorityTask {
    private final int priority;

    /**
     * @param priority Task priority when task is active (0 when not active).
     */
    public IdleTask(int priority) {
        super();
        this.priority = priority;
    }

    /**
     * Sets the idle task to be active (when AI is not moving)
     */
    public void setActive() {
        status = Status.ACTIVE;
    }

    /**
     * Sets the idle task to be active (when AI is moving)
     */
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

    /**
     * Get the tasks priority
     *
     * @return the tasks priority
     */
    @Override
    public int getPriority() {
        return status == Status.ACTIVE ? priority : 0;
    }
}
