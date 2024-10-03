package com.csse3200.game.inventory.items.lootbox;

import com.csse3200.game.components.inventory.InventoryComponent;
import com.csse3200.game.components.inventory.PlayerInventoryDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.inventory.items.ConsumableItem;
import com.csse3200.game.inventory.items.ItemUsageContext;
import com.csse3200.game.inventory.items.exceptions.ConsumedException;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.entities.factories.ItemFactory;
import com.csse3200.game.inventory.items.lootbox.configs.BaseLootTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UniversalLootBox extends ConsumableItem {

    private final BaseLootTable lootTable;
    private final int rolls;
    private final String description;
    private final String imagePath;
    private final Entity player;
    private static final Logger logger = LoggerFactory.getLogger(UniversalLootBox.class);

    public UniversalLootBox(BaseLootTable lootTable, int rolls, Entity player, String description, String imagePath,
                            int code) {
        super("LootBox", code, 999, 1);  // Assuming LootBox ID is 1001, can be changed
        this.lootTable = lootTable;
        this.rolls = rolls;
        this.player = player;
        this.description = description;
        this.imagePath = imagePath;
        this.setTexturePath(imagePath);
        this.setDescription(description);
    }

    /**
     * Opens the loot box and returns a list of randomly selected items based on the loot table and number of rolls.
     * @return List of randomly selected AbstractItem objects.
     */
    public List<AbstractItem> open() {
        return lootTable.getRandomItems(rolls);  // Use lootTable to generate items
    }

    /**
     * Consumes one instance of the loot box and adds the generated items to the player's inventory.
     * This method triggers the addition of new items to the player's inventory and updates the player's UI.
     *
     * @param context The context in which the item is being used, typically includes information about the entity using the item.
     * @throws ConsumedException If the loot box has already been fully consumed and has no remaining uses.
     */
    @Override
    public void useItem(ItemUsageContext context) throws ConsumedException {
        super.useItem(context);

        // Roll the loot box and get new items
        List<AbstractItem> newItems = this.open();

        // Get player's inventory component
        InventoryComponent inventory = this.player.getComponent(InventoryComponent.class);

        inventory.getInventory().deleteItem(this.getItemCode());



        // Add items to the player's inventory or drop them if the inventory is full
        for (AbstractItem item : newItems) {
            if (inventory.getInventory().isFull()) {
                // Drop item near the player if the inventory is full
                Entity itemEntity = ItemFactory.createItem(player, item);
                player.getEvents().trigger("dropItems", itemEntity, 3);  // Drop the item with a 3-unit radius
                logger.debug("Dropping item: {}", item.getName());
            } else {
                // Add item to inventory if there is space
                inventory.getEntity().getEvents().trigger("addItem", item);
                logger.debug("Item added to inventory: {}", item.getName());
            }
        }

        // Update UI and show the loot items
        PlayerInventoryDisplay display = this.player.getComponent(PlayerInventoryDisplay.class);
        if (display.getToggle()) {
            player.getEvents().trigger("toggleInventory");
        }
        player.getEvents().trigger("showLoot", newItems);
    }

    // Getters for texture path and description
    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Entity getPlayer() {
        return player;
    }

}