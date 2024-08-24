package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.ConfigComponent;
import com.csse3200.game.entities.configs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.ui.ChatOverlay;
import com.csse3200.game.components.quests.QuestManager;
import com.csse3200.game.components.quests.AbstractQuest;

/** Pauses near a target entity until they move too far away or out of sight */
public class PauseTask extends ChaseTask {
    private final float maxPauseDistance;
    private boolean hasApproached;
    private static final Logger logger = LoggerFactory.getLogger(PauseTask.class);
    private ChatOverlay hint;
    private Entity entity;
    private QuestManager questManager;
    private BaseEntityConfig config;

    /**
     * @param target The entity to pause when seen.
     * @param priority Task priority when pausing (0 when not pausing).
     * @param viewDistance Maximum distance from the entity at which pausing can start.
     * @param maxPauseDistance Maximum distance from the entity to pause.
     */
    public PauseTask(Entity target, int priority, float viewDistance, float maxPauseDistance) {
        super(target, priority, viewDistance, maxPauseDistance);
        this.maxPauseDistance = maxPauseDistance;
        this.questManager = this.target.getComponent(QuestManager.class);
        this.hasApproached = false;
        this.hint = null;
        this.config = null;
    }

    @Override
    public void start() {
        super.start();
        triggerPauseEvent();
    }

    protected void triggerPauseEvent() {
        this.entity = this.owner.getEntity();
        ConfigComponent<BaseEntityConfig> configComponent = (ConfigComponent<BaseEntityConfig>) entity.getComponent(ConfigComponent.class);
        if (configComponent != null) {
            this.config = configComponent.getConfig();
            String animalName = ((BaseEntityConfig) config).getAnimalName();
            String eventName = String.format("Paused%s", animalName);
            entity.getEvents().trigger(eventName);
        } else {
            entity.getEvents().trigger("pauseStart");
        }
    }

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
            logger.info("Medium");

            createChatOverlay();

            movementTask.stop();

        } else if (hasApproached && distanceToTarget > 1.5f) {

            // If the player moves out of viewDistance, the NPC stops but does not follow the player
            this.hasApproached = false;
            logger.info("end");

            if (this.hint != null) {
                hint.dispose();
                hint = null;
            }

            //movementTask.start();
        }
    }

    protected void createChatOverlay() {
        if (this.hint == null) {
            AbstractQuest currentQuest = null;
            for (AbstractQuest quest : questManager.getAllQuests()) {
                if (quest.isActive()) {
                    currentQuest = quest;
                }
            }

            String[] hintText;
            if (currentQuest != null) {
                String hint = currentQuest.getCurrentTaskHint();
                hintText = new String[] { hint };
            } else {
                hintText = ((BaseEntityConfig) this.config).getStringHintLevel();
            }

            hint = new ChatOverlay(hintText);
        }
    }

    public int getPriority() {
        if (status == Status.ACTIVE) {
            return getActivePriority();
        }

        return getInactivePriority();
    }

    protected float getDistanceToTarget() {
        return owner.getEntity().getPosition().dst(target.getPosition());
    }

    @Override
    protected int getActivePriority() {
        float distance = getDistanceToTarget();
        if (distance > getViewDistance() || !isTargetVisible()) {
            return -1; // Too far or not visible, stop the task
        }
        return priority;
    }

    @Override
    protected int getInactivePriority() {
        float distance = getDistanceToTarget();
        if (distance < getViewDistance() && isTargetVisible()) {
            return priority;
        }
        return -1;
    }
}