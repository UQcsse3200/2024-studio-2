package com.csse3200.game.inventory.items.lootbox.rarities;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.inventory.items.lootbox.LootBox;
import com.csse3200.game.inventory.items.lootbox.configs.MediumGameLootTable;

/**
 * Represents a Medium Game Loot Box, a type of loot box designed for mid-game content.
 * This loot box uses a medium game loot table to generate items when opened, providing
 * a balanced reward suitable for players in the mid-game phase.
 */
public class MediumGameLootBox extends LootBox {

    private final static String path = "images/chests/fancy-chest.png";

    /**
     * Constructs a MediumGameLootBox with the specified loot table, number of rolls, and player entity.
     *
     * @param lootTable The MediumGameLootTable object representing the loot table used to generate items for the mid-game.
     * @param rolls     The number of rolls to perform when opening the loot box, determining the number of items generated.
     * @param player    The player Entity to which the loot box items will be added.
     */
    public MediumGameLootBox(MediumGameLootTable lootTable, int rolls, Entity player) {
        super(lootTable, rolls, player );
        this.setTexturePath(path);
        this.setDescription("This is an MediumGameLootBox, Open it Up for Goodies!!!!");
    }


}
