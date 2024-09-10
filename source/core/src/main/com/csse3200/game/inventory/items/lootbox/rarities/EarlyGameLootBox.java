package com.csse3200.game.inventory.items.lootbox.rarities;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.inventory.items.ItemUsageContext;
import com.csse3200.game.inventory.items.lootbox.LootBox;
import com.csse3200.game.inventory.items.lootbox.configs.BaseLootTable;
import com.csse3200.game.inventory.items.lootbox.configs.EarlyGameLootTable;

/**
 * Represents an Early Game Loot Box, a type of loot box specifically configured for early game content.
 * It uses an early game loot table to generate items when opened, and sets a specific texture and description
 * to visually represent the loot box in the game.
 */
public class EarlyGameLootBox extends LootBox {

    private final static String path = "images/chests/ordinary-chest.png";

    /**
     * Constructs an EarlyGameLootBox with the specified loot table, number of rolls, and player entity.
     *
     * @param lootTable The EarlyGameLootTable object representing the loot table used to generate items for the early game.
     * @param rolls     The number of rolls to perform when opening the loot box, determining how many items are generated.
     * @param player    The player Entity to which the loot box items will be added.
     */
    public EarlyGameLootBox(EarlyGameLootTable lootTable, int rolls, Entity player) {
        super(lootTable, rolls, player );
        this.setTexturePath(path);
        this.setDescription("This is an EarlyGameLootBox, Open it Up for Goodies!!!!");
    }


}
