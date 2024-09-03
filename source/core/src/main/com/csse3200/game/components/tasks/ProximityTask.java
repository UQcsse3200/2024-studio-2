package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;

/**
 * An abstract task that monitors the proximity of a specified target entity.
 * The task performs specific actions when the target entity moves within a
 * defined proximity threshold and when it moves away. This task is designed
 * to be used within an AI task system.
 */
public abstract class ProximityTask extends DefaultTask  implements PriorityTask {
    private final Entity target;
    private final int priority;
    private final float proximityThreshold;
    private boolean hasApproached;

    /**
     * Constructs a new ProximityTask.
     *
     * @param target              The entity that the task will monitor for proximity
     * @param priority            The priority level of this task, used in the AI task system.
     * @param proximityThreshold  The distance within which the task will consider the target entity "near".
     */
    public ProximityTask(Entity target, int priority, float proximityThreshold) {
        this.target = target;
        this.priority = priority;
        this.proximityThreshold = proximityThreshold;
        this.hasApproached = false;
    }

    /**
     * Starts the task by initializing any required state or event listeners.
     * This method is called when the task is activated.
     */
    @Override
    public void start() {
        super.start();
    }

    /**
     * Updates the task each frame. It checks the proximity of the target entity
     * to the owner entity and triggers the appropriate handler methods when
     * the target moves close or moves away.
     */
    @Override
    public void update() {
        super.update();
        if(targetInProximity() && !this.hasApproached) {
            this.hasApproached = true;
            handleTargetMovedClose();
        }
        else if (!targetInProximity() && this.hasApproached) {
            this.hasApproached = false;
            handleTargetMovedAway();
        }
    }

    /**
     * Abstract method to handle the case when the target entity moves away
     * from the owner entity. Subclasses must implement this method to define
     * the specific behavior when the target moves away.
     */
    public abstract void handleTargetMovedAway();

    /**
     * Abstract method to handle the case when the target entity moves away
     * from the owner entity. Subclasses must implement this method to define
     * the specific behavior when the target moves away.
     */
    public abstract void handleTargetMovedClose();

    /**
     * Returns the priority of this task, which is based on whether the target
     * entity is near the owner entity.
     *
     * @return The priority level of this task. Returns the configured priority
     *         if the target is within the proximity threshold, otherwise returns 0.
     */
    @Override
    public int getPriority() {
        if (targetInProximity()) {
            return priority;
        }
        return 0;
    }

    /**
     * Checks if the target entity is near the owner entity by calculating the
     * distance between them and comparing it to the proximity threshold.
     *
     * @return true if the target entity is within the proximity threshold,
     *         false otherwise.
     */
    private boolean targetInProximity() {
        return target.getPosition().dst(owner.getEntity().getPosition()) <= proximityThreshold;
    }
}
