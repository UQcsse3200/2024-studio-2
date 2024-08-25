package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.ConfigComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.BaseEntityConfig;
import com.csse3200.game.entities.configs.BaseEntityItemConfig;
import com.csse3200.game.ui.ChatOverlay;
import com.csse3200.game.ui.ItemOverlay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ItemPickupTask extends DefaultTask implements PriorityTask {
    private final Entity target;
    private final int priority;
    private final float viewDistance;
    private final float maxItemDistance;
    private boolean hasApproached;
    private MovementTask movementTask;
    private static final Logger logger = LoggerFactory.getLogger(ItemPickupTask.class);
    private ItemOverlay hint;
    private Entity entity;
    private Object config;

    public ItemPickupTask(Entity target, int priority, float viewDistance, float maxItemDistance) {
        this.target = target;
        this.priority = priority;
        this.viewDistance = viewDistance;
        this.maxItemDistance = maxItemDistance;
        this.hasApproached = false;
        this.hint = null;
        this.config = null;
    }

    @Override
    public void start() {
        super.start();
        movementTask = new MovementTask(target.getPosition());
        movementTask.create(owner);
        movementTask.start();
        triggerItemEvent();
    }

    /**
     * Triggers the event when encountering an item
     */
    private void triggerItemEvent() {
        this.entity = this.owner.getEntity();
        ConfigComponent<?> configComponent = (ConfigComponent<?>) entity.getComponent(ConfigComponent.class);
        if (configComponent != null) {
            this.config = configComponent.getConfig();
            String itemName = ((BaseEntityItemConfig) config).getItemName();
            String eventName = String.format("Encountered%s", itemName);
            entity.getEvents().trigger(eventName);

        } else {
            entity.getEvents().trigger("encounterStart");
        }
    }

    /**
     * Updates the events with encountering with items
     */
    @Override
    public void update() {
        float distanceToTarget = getDistanceToTarget();
        if (distanceToTarget<= maxItemDistance && !hasApproached) {
            logger.info("Medium");
            hasApproached = true;

            createItemOverlay();

        }   else if (hasApproached && distanceToTarget > 1.5f) {

            // If the player moves out of viewDistance, the dialog will disappear
            this.hasApproached = false;
            logger.info("end");

            if (this.hint != null) {
                hint.dispose();
                hint = null;
            }
        }
    }


    private void createItemOverlay() {
        if (this.hint == null) {
            String[] itemText = ((BaseEntityItemConfig) this.config).getItemDescription();
            BaseEntityItemConfig config = ((BaseEntityItemConfig) this.config);
            String name = ((BaseEntityItemConfig) this.config).getItemName();
            hint = new ItemOverlay(itemText);
        }
    }

    @Override
    public int getPriority() {
        if (status == Status.ACTIVE) {
            return getActivePriority();
        }
        return 0;
        //return getInactivePriority();
    }

    private int getActivePriority() {
        float distance = getDistanceToTarget();
        if (distance > viewDistance || !isTargetVisible()) {
            return -1; // Too far or not visible, stop the task
        }
        return priority;
    }

    private boolean isTargetVisible() {
        return true;
    }

    /**
     * Return the distance between the target and player
     * @return float - the distance between the two
     */
    private float getDistanceToTarget () {
        return target.getPosition().dst(owner.getEntity().getPosition());
    }
}