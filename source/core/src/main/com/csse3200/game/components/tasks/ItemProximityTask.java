package com.csse3200.game.components.tasks;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.components.inventory.InventoryComponent;
import com.csse3200.game.services.DialogueBoxService;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;


/**
 * A task that monitors when the proximity of a player to an item and handles item pick-up interactions.
 * When the player is within a specified distance of the item, an overlay is displayed, allowing
 * the player to pick up the item and add it to their inventory.
 */
public class ItemProximityTask extends ProximityTask {
    private static final Logger logger = LoggerFactory.getLogger(ItemProximityTask.class);
    private final AbstractItem item;
    private boolean itemPickedUp = false;

    public ItemProximityTask(Entity target,
                             int priority,
                             float proximityThreshold,
                             AbstractItem item) {
        super(target, priority, proximityThreshold);
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
     *Adds the item to the players' inventory if they are near the item and haven't picked it up
     * Disposes of the item entity once it has been picked up and logs the event
     */
    public void addToInventory() {
        if (!itemPickedUp && targetInProximity()) { // Check if the item hasn't been picked up and player is near
            InventoryComponent inventory = this.target.getComponent(InventoryComponent.class);
            if (inventory != null) {
                if (!inventory.getInventory().isFull()) {
                    this.target.getEvents().trigger("addItem", item);
                    this.target.getEvents().trigger("pickUp" + item.getName());
                    logger.info("Item added to inventory {}.", item.getName());
                    itemPickedUp = true; // Set flag to prevent further triggering
                    owner.getEntity().dispose();
                    ServiceLocator.getDialogueBoxService().hideCurrentOverlay();
                }
            } else {
                logger.error("Inventory not found on target entity.");
            }
        } else if (!targetInProximity()) {
            logger.debug("Player is not close enough to pick up the item.");
        }
    }

    /**
     * Returns the priority of this task, which is based on whether the player is near the item or has just
     * picked it up
     * @return The priority level of this task
     */
    @Override
    public int getPriority() {
        if (targetInProximity()) {
            return priority;
        }
        if (hasPlayerPickedUpNearItem()) {
            return priority;
        }
        return 0;
    }

    @Override
    public void handleTargetMovedAway() {
        // Needs new chatOverlayService when screen recovered from preserving screen (e.g. to play mini-game)
        DialogueBoxService dialogueBoxService = ServiceLocator.getDialogueBoxService();
        if (dialogueBoxService == null) {
            Stage stage = ServiceLocator.getRenderService().getStage();
            ServiceLocator.registerDialogueBoxService(new DialogueBoxService(stage));
        }
        ServiceLocator.getDialogueBoxService().hideCurrentOverlay();
    }

    @Override
    public void handleTargetMovedClose() {
        String[][] itemText = {{item.getDescription() + " - press P to pick it up."}};
        ServiceLocator.getDialogueBoxService().updateText(itemText, DialogueBoxService.DialoguePriority.ITEMINVENTORY);
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
