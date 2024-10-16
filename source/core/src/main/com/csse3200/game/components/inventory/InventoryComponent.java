package com.csse3200.game.components.inventory;

import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.forest.ForestGameArea;
import com.csse3200.game.components.Component;
import com.csse3200.game.inventory.Inventory;

import com.badlogic.gdx.math.Vector2;

import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.areas.MapHandler;
import com.csse3200.game.entities.factories.ItemFactory;
import com.csse3200.game.inventory.items.lootbox.UniversalLootBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data class to store the player's inventory separately from the display components.
 * All other objects which do not explicitly need to use the display but need to access the
 * inventory should access this component only.
 */
public class InventoryComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(InventoryComponent.class);
    private final Inventory inventory;

    public InventoryComponent(int capacity) {
        this.inventory = new Inventory(capacity);
    }

    @Override
    public void create() {
        super.create();
        // Register the event listener for removing items.
        entity.getEvents().addListener("removeItemFromInventory", this::onRemoveItemFromInventory);
    }

    /**
     * Event listener that handles removing an item from the inventory and dropping it onto the game map.
     *
     * @param index        The index of the item to remove.
     * @param worldPosition The position where the item should be dropped on the map.
     */
    private void onRemoveItemFromInventory(int index, Vector2 worldPosition) {
        logger.info("Attempting to remove item from inventory at index: {} with target world position: ({}, {})", index, worldPosition.x, worldPosition.y);

        AbstractItem removedItem = inventory.getAt(index);
        if (removedItem != null) {
            logger.info("Item found at index {}: {}. Proceeding to remove it from inventory.", index, removedItem.getName());

            // Check if the item is a loot box. If it is, don't proceed with dropping it.
            if (removedItem instanceof UniversalLootBox) {
                logger.info("Item {} is a loot box and will not be dropped.", removedItem.getName());
                return;
            }

            // Remove the item from the inventory.
            inventory.deleteItemAt(index);
            logger.info("Item {} successfully removed from inventory at index {}.", removedItem.getName(), index);

            // Drop the item near the player using the spawnEntityNearPlayer method.
            dropItemNearPlayer(removedItem, 2); // Adjust the radius as needed.
        } else {
            logger.info("No item found at index {}. No removal action taken.", index);
        }
    }

    /**
     * Drops the item near the player within a specified radius.
     *
     * @param item     The item to drop onto the map.
     * @param radius   The radius within which the item should be dropped near the player.
     */
    private void dropItemNearPlayer(AbstractItem item, int radius) {
        logger.info("Attempting to drop item {} near the player within a radius of {} units.", item.getName(), radius);

        // Get the player entity using MapHandler.
        Entity playerEntity = MapHandler.getCurrentMap().getPlayer();
        if (playerEntity != null) {
            logger.info("Player entity found. Proceeding to create item entity.");

            // Create the item entity using the ItemFactory.
            Entity itemEntity = ItemFactory.createItem(playerEntity, item);

            // Use the ForestGameArea to spawn the entity near the player.
            GameArea gameArea = MapHandler.getCurrentMap();
            if (gameArea instanceof ForestGameArea forestGameArea) {
                forestGameArea.spawnEntityNearPlayer(itemEntity, radius);
                logger.info("Item {} dropped near the player using ForestGameArea at a random position within radius {}.", item.getName(), radius);
            } else {
                logger.warn("Current game area is not of type ForestGameArea. Cannot drop item {} using the specified method.", item.getName());
            }
        } else {
            logger.warn("Player entity is null. Cannot drop item {} on the map.", item.getName());
        }
    }

    /**
     * Returns the player's current inventory from this attached InventoryComponent.
     * @return The player's current inventory.
     */
    public Inventory getInventory() {
        return this.inventory;
    }

    /**
     * Loads the inventory attached to the player from a save.
     */
    public void loadInventoryFromSave() {
        inventory.loadInventoryFromSave();
    }
}

