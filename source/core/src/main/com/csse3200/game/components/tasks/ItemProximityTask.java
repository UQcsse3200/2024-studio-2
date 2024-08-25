package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class ItemProximityTask extends DefaultTask implements PriorityTask {
    private final Entity target;
    private final int priority;
    private final float proximityThreshold;
    private static final Logger logger = LoggerFactory.getLogger(ItemProximityTask.class);

    public ItemProximityTask(Entity target, int priority, float proximityThreshold) {
        this.target = target;
        this.priority = priority;
        this.proximityThreshold = proximityThreshold;
    }

    @Override
    public void start() {
        super.start();
    }

    public void update() {
        if (isPlayerNearItem()) {
            logger.debug("Player is within pickup range of the item.");
            this.owner.getEntity().getEvents().trigger("PlayerNearItem");
        }
        if (hasPlayerPickedUpNearItem()) {
            logger.debug("Item in range of player has been picked up.");
        }
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
        return target.getPosition().dst(owner.getEntity().getPosition()) <= proximityThreshold;
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
}
