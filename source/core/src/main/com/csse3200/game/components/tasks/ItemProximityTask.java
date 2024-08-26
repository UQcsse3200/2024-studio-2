package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.player.PlayerInventoryDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.ui.ChatOverlay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class ItemProximityTask extends DefaultTask implements PriorityTask {
    private final Entity target;
    private final int priority;
    private final float proximityThreshold;
    private static final Logger logger = LoggerFactory.getLogger(ItemProximityTask.class);
    private final AbstractItem item;
    private boolean itemPickedUp = false;
    private boolean hasApproached;
    private ChatOverlay itemOverlay;

    public ItemProximityTask(Entity target, int priority, float proximityThreshold, AbstractItem item) {
        this.target = target;
        this.priority = priority;
        this.proximityThreshold = proximityThreshold;
        this.item = item;
        this.hasApproached = false;
    }

    @Override
    public void start() {
        super.start();
        this.target.getEvents().addListener("pickUpItem", this::addToInventory);
    }

    @Override
    public void update() {
        super.update();
        if(isPlayerNearItem() && !this.hasApproached) {
            this.hasApproached = true;
            createItemOverlay();
        }
        else if (!isPlayerNearItem() && this.hasApproached) {
            this.hasApproached = false;
            if (this.itemOverlay != null) {
                itemOverlay.dispose();
                itemOverlay = null;
            }

        }
    }

    private void createItemOverlay() {
        if (this.itemOverlay == null) {
            String[] itemText = {item.getDescription() + "- press P to pick it up."};
            itemOverlay = new ChatOverlay(itemText);
        }
    }

    public void addToInventory() {
        if (!itemPickedUp && isPlayerNearItem()) { // Check if the item hasn't been picked up and player is near
            PlayerInventoryDisplay display = this.target.getComponent(PlayerInventoryDisplay.class);
            if (display != null) {
                display.getEntity().getEvents().trigger("addItem", item);
                logger.info("Item added to inventory.");
                itemPickedUp = true; // Set flag to prevent further triggering
                owner.getEntity().dispose();
                itemOverlay.dispose();
                itemOverlay = null;

            } else {
                logger.error("PlayerInventoryDisplay component not found on target entity.");
            }
        } else if (!isPlayerNearItem()) {
            logger.info("Player is not close enough to pick up the item.");
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
