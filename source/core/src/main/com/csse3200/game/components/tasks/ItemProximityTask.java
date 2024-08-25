package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;
import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.ValueExp;
import java.util.Objects;

public class ItemProximityTask extends DefaultTask implements PriorityTask {
    private Entity target = null;
    private final int priority;
    private final float proximityThreshold;
    private static final Logger logger = LoggerFactory.getLogger(ItemProximityTask.class);

    public ItemProximityTask(Entity target, int priority, float proximityThreshold) {
        this.target = target;
        this.priority = priority;
        this.proximityThreshold = proximityThreshold;
    }

    protected Entity item = owner.getEntity();
    protected Vector2 targetPosition = this.target.getPosition();
    protected Vector2 itemPosition = item.getPosition();

    @Override
    public void start() {
        super.start();

        this.owner.getEntity().getEvents().trigger("pickUp");
    }

    public void update() {
        if (isPlayerNearItem()) {
            logger.info("Player is within pickup range of the item.");
        }
        if (hasPlayerPickedUpNearItem()) {
            logger.info("Item in range of player has been picked up.");
        }
    }

    /**
     * returns the item vector from player
     *
     * @return itemPosition the position of the item from player
     */
    public Vector2 getItemPositionVector() {
        return itemPosition;
    }

    /**
     * Returns the item entity
     * @return item the item entity
     */
    public Entity getItemEntity() {
        return item;
    }

    @Override
    public int getPriority() {
        if (isPlayerNearItem()) {
            return priority;
        }
        if (hasPlayerPickedUpNearItem()) {
            return priority;
        }
        return 0;
    }

    private boolean isPlayerNearItem() {
        float distance = targetPosition.dst(itemPosition);
        return distance <= proximityThreshold;
    }

    /**
     * Checks if nearby item is picked up
     *
     * @return true if item has been picked up
     */
    private boolean hasPlayerPickedUpNearItem() {
        EventHandler eventHandler = new EventHandler();
        return Objects.equals(eventHandler.getLastTriggeredEvent(), "itemPickedUp");
        // TO DO uncomment this when removal of spawn items on map will be implemented
//        Entity item = owner.getEntity();
//        Vector2 targetPosition = target.getPosition();
//        Vector2 itemPosition = item.getPosition();
//        float distance = targetPosition.dst(itemPosition);
//        return distance > proximityThreshold;
    }

    /**
     * Retrieves the item associated with this task.
     *
     * @return the entity representing the item
     */
    public Entity getTargetItem() {
        return owner.getEntity();
    }
}
