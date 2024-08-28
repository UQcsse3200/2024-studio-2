package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.player.PlayerInventoryDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.ui.DialogueBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * A task that monitors when the proximity of a player to an item and handles item pick-up interactions.
 * When the player is within a specified distance of the item, an overlay is displayed, allowing
 * the player to pick up the item and add it to their inventory.
 */
public class ItemProximityTask extends DefaultTask implements PriorityTask {
    private final Entity target;
    private final int priority;
    private final float proximityThreshold;
    private static final Logger logger = LoggerFactory.getLogger(ItemProximityTask.class);
    private final AbstractItem item;
    private boolean itemPickedUp = false;
    private boolean hasApproached;
    DialogueBox itemOverlay;

    /**
     * Constructs a new ItemProximityTask
     *
     * @param target The entity that the task will monitor for Proximity (e.g., The Player)
     * @param priority The priority levels of this task, used in the AI task system.
     * @param proximityThreshold The distance within which the item will be considered "near" the player.
     * @param item The item that will be picked up by the player
     */
    public ItemProximityTask(Entity target, int priority, float proximityThreshold, AbstractItem item) {
        this.target = target;
        this.priority = priority;
        this.proximityThreshold = proximityThreshold;
        this.item = item;
        this.hasApproached = false;
    }

    /**
     * Starts the task by adding an event listener for the "pickupItem" event.
     * The event is triggered when the Player presses P.
     */
    @Override
    public void start() {
        super.start();
        this.target.getEvents().addListener("pickUpItem", this::addToInventory);
    }

    /**
     * Updates the task each frame. It checks the players proximity to the item and displays
     * an overlay if the player is near. If the play moves away, the overlay is removed.
     */
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

    /**
     * Creates an overlay that displays item information and prompts the player to pick up the item.
     * This method is called when the player is near the item.
     */
    private void createItemOverlay() {
        if (this.itemOverlay == null) {
            String[] itemText = {item.getDescription() + " - press P to pick it up."};
            itemOverlay = new DialogueBox(itemText);
        }
    }

    /**
     *Adds the item to the players' inventory if they are near the item and haven't picked it up
     * Disposes of the item entity once it has been picked up and logs the event
     */
    public void addToInventory() {
        if (!itemPickedUp && isPlayerNearItem()) { // Check if the item hasn't been picked up and player is near
            PlayerInventoryDisplay display = this.target.getComponent(PlayerInventoryDisplay.class);
            if (display != null) {
                display.getEntity().getEvents().trigger("addItem", item);
                logger.debug("Item added to inventory.");
                itemPickedUp = true; // Set flag to prevent further triggering
                owner.getEntity().dispose();
                logger.debug("I WAS DISPOSED OF!");
                itemOverlay.dispose();
                itemOverlay = null;

            } else {
                logger.error("PlayerInventoryDisplay component not found on target entity.");
            }
        } else if (!isPlayerNearItem()) {
            logger.info("Player is not close enough to pick up the item.");
        }
    }

    /**
     * Returns the priority of this task, which is based on whether the player is near the item or has just
     * picked it up
     * @return The priority level of this task
     */
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

    /**
     * Checks if the player is near the item using the distance between the item and the player
     * @return true if the player is within the proximity threshold, false otherwise.
     */
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
    }
}
