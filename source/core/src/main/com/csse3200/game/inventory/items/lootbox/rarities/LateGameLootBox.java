package com.csse3200.game.inventory.items.lootbox.rarities;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.inventory.items.lootbox.LootBox;
import com.csse3200.game.inventory.items.lootbox.configs.LateGameLootTable;

/**
 * Represents a Late Game Loot Box, a specialized type of loot box designed for late-game content.
 * This loot box uses a late-game loot table to generate items, providing valuable rewards suitable
 * for players in the advanced stages of the game.
 */
public class LateGameLootBox extends LootBox {
    private final static String path = "images/chests/fancy-smancy-chest.png";

    /**
     * Constructs a LateGameLootBox with the specified loot table, number of rolls, and player entity.
     *
     * @param lootTable The LateGameLootTable object representing the loot table used to generate items for the late game.
     * @param rolls     The number of rolls to perform when opening the loot box, determining the quantity of items generated.
     * @param player    The player Entity to which the loot box items will be added.
     */
    public LateGameLootBox(LateGameLootTable lootTable, int rolls, Entity player) {
        super(lootTable, rolls, player);
        this.setTexturePath(path);
        this.setDescription("This is an LateGameLootBox, Open it Up for Goodies!!!!");
    }

}

