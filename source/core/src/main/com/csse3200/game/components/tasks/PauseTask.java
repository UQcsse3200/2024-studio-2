package com.csse3200.game.components.tasks;

import com.badlogic.gdx.utils.Logger;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.components.ConfigComponent;
import com.csse3200.game.entities.configs.*;
import com.csse3200.game.services.ServiceLocator;

import java.util.Objects;

/**
 * Pauses near a target entity until they move too far away or out of sight.
 * Extends the ChaseTask to include pausing behavior when in proximity to a target.
 */
public class PauseTask extends ChaseTask {
    private static final Logger logger = new Logger("PauseTask");

    private final float maxPauseDistance;
    private boolean hasApproached;
    private Entity entity;
    private BaseFriendlyEntityConfig config;
    private String animalName;

    /**
     * Constructs a new PauseTask that will pause near a target entity.
     *
     * @param target The entity to pause when seen.
     * @param priority Task priority when pausing (0 when not pausing).
     * @param viewDistance Maximum distance from the entity at which pausing can start.
     * @param maxPauseDistance Maximum distance from the entity to pause.
     */
    public PauseTask(Entity target, int priority, float viewDistance, float maxPauseDistance, boolean isBoss) {
        super(target, priority, viewDistance, maxPauseDistance, null, isBoss);
        this.maxPauseDistance = maxPauseDistance;
        this.hasApproached = false;
        this.config = null;
    }

    /**
     * Starts the pause behavior
     */
    @Override
    public void start() {
        super.start();
        triggerPauseEvent();
    }

    /**
     * Triggers an event to start the pause behavior.
     * If the entity has a config component, it fetches the dialogue or hint text
     * associated with the entity to provide context for the pause event.
     */
    protected void triggerPauseEvent() {
        this.entity = this.owner.getEntity();
        ConfigComponent<BaseFriendlyEntityConfig> configComponent = entity.getComponent(ConfigComponent.class);
        this.config = configComponent.getConfig();


        if (this.config != null) {
            String[][] hintText = this.config.getBaseHint();
            animalName = (config).getAnimalName();
            String eventName = String.format("PauseStart%s", animalName);
            entity.getEvents().trigger(eventName, hintText);
        } else {
            entity.getEvents().trigger("PauseStart");
        }
    }

    /**
     * Triggers an event to end the pause behavior.
     * If the entity has a config component, it fetches the dialogue or hint text
     * associated with the entity to provide context for the pause event.
     */
    protected void triggerPauseEventEnd() {
        if (this.config != null) {
            String animalName = (config).getAnimalName();
            String eventName = String.format("PauseEnd%s", animalName);
            entity.getEvents().trigger(eventName);
        } else {
            entity.getEvents().trigger("pauseEnd");
        }
    }


    /**
     * Updates the pause behavior by checking the distance to the target entity
     * and determining whether to approach, pause, or end the pause state.
     */
    @Override
    public void update() {
        float distanceToTarget = getDistanceToTarget();

        if (!hasApproached && distanceToTarget > maxPauseDistance && distanceToTarget <= viewDistance) {
            // Move towards the target until within maxPauseDistance
            movementTask.setTarget(target.getPosition());
            movementTask.update();

        } else if (!hasApproached && distanceToTarget <= maxPauseDistance) {
            // NPC pauses when close enough to the target
            hasApproached = true;
            movementTask.stop();
        }

        if (hasApproached && Boolean.FALSE.equals(ServiceLocator.getDialogueBoxService().getIsVisible())
            && !Objects.equals(entity.getEvents().getLastTriggeredEvent(), String.format("PauseStart%s", animalName))
        ) {
            triggerPauseEvent();
        }
    }

    /**
     * Stops the pause behavior
     */
    @Override
    public void stop() {
        super.stop();
        movementTask.start();

        // Ensure the chat box doesn't hang around when it's not supposed to
        this.hasApproached = false;
        triggerPauseEventEnd();
    }

    /**
     * Returns the priority level of the pause behavior.
     * Determines the priority based on whether the task is currently active or inactive.
     *
     * @return the priority level of the pause behavior.
     */
    @Override
    public int getPriority() {
        if (getDistanceToTarget() < viewDistance && isTargetVisible()) {
            return priority;
        }
        return -1;
    }
}
