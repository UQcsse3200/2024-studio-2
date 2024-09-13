package com.csse3200.game.inventory.items.lootbox;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.inventory.items.lootbox.configs.EarlyGameLootTable;
import com.csse3200.game.inventory.items.lootbox.configs.LateGameLootTable;
import com.csse3200.game.inventory.items.lootbox.configs.MediumGameLootTable;
import com.csse3200.game.inventory.items.lootbox.rarities.EarlyGameLootBox;
import com.csse3200.game.inventory.items.lootbox.rarities.LateGameLootBox;
import com.csse3200.game.inventory.items.lootbox.rarities.MediumGameLootBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
public class LootBoxTest {
    private Entity player;

    @BeforeEach
    void setUp() {
        player = new Entity();
    }

    @Test
    void testInitialisation() {
        // For now this simply tests that initialisation doesn't crash, this can be extended to
        // more proper testing using Mockito in sprint 3.
        new EarlyGameLootBox(new EarlyGameLootTable(), 1, player);
        new MediumGameLootBox(new MediumGameLootTable(), 1, player);
        new LateGameLootBox(new LateGameLootTable(), 1, player);
    }
}
