package com.csse3200.game.inventory.items.lootbox;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.player.PlayerInventoryDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.inventory.items.ConsumableItem;
import com.csse3200.game.inventory.items.ItemUsageContext;
import com.csse3200.game.inventory.items.exceptions.ConsumedException;
import com.csse3200.game.inventory.items.lootbox.*;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.inventory.items.lootbox.configs.BaseLootTable;
import com.csse3200.game.entities.factories.ItemFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The LootBox class represents a consumable item that generates items based on a specified loot table.
 * It allows the player to open the loot box, triggering a random selection of items according to the
 * configured number of rolls.
 */
public class LootBox extends ConsumableItem {
    private final BaseLootTable lootTable;
    private final int rolls;
    private final Entity player;
    private static final Logger logger = LoggerFactory.getLogger(LootBox.class);

    /**
     * Constructs a LootBox with the specified loot table, number of rolls, and the player entity.
     *
     * @param lootTable The BaseLootTable object representing the loot table used for generating items.
     * @param rolls     The number of rolls to perform when opening the loot box, determining the quantity of items received.
     * @param player    The player Entity to which the loot box items will be added.
     */
    public LootBox(BaseLootTable lootTable, int rolls, Entity player) {
        super("LootBox", 1001, 1, 1);
        this.lootTable = lootTable;
        this.rolls = rolls;
        this.player = player;

    }


    /**
     * Opens the loot box and returns a list of randomly selected items based on the specified number of rolls.
     * This method utilizes the loot table to determine which items are included.
     *
     * @return A List of randomly selected AbstractItem objects generated from the loot table.
     */
    public List<AbstractItem> open() {
        return lootTable.getRandomItems(rolls);
    }

    /**
     * Consumes one instance of the loot box and adds the generated items to the player's inventory.
     * This method triggers the addition of new items to the player's inventory and updates the player's UI
     * to show the newly obtained items.
     *
     * @param context The context in which the item is being used, typically includes information about the entity using the item.
     * @throws ConsumedException If the loot box has already been fully consumed and has no remaining uses.
     */
    @Override
    public void useItem(ItemUsageContext context) {
        super.useItem(context);
        List<AbstractItem> newItems = this.open(); // Rolls the loot box and gets new items

        PlayerInventoryDisplay display = this.player.getComponent(PlayerInventoryDisplay.class);

        // Check if the inventory has space for items
        for (AbstractItem item : newItems) {
            if (display.hasSpaceFor()) { // Check if the inventory is full
                Entity itemEntity = ItemFactory.createItem(player, item); // Create entity for the item
                player.getEvents().trigger("dropItems", itemEntity, 3); // Drop item near player
                logger.info("Dropping item: {}", item.getName());
            } else {
                display.getEntity().getEvents().trigger("addItem", item); // Add item to inventory
                logger.info("Item added to inventory: {}", item.getName());
            }
        }

        // Trigger inventory UI update and show loot
        if(display.getToggle()) {
            player.getEvents().trigger("toggleInventory");
        }
        player.getEvents().trigger("showLoot", newItems);
    }

}