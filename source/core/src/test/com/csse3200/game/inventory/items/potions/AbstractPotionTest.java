package com.csse3200.game.inventory.items.potions;


import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.inventory.items.potions.healingpotion.HealingPotion;

import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
class AbstractPotionTest  {
    private HealingPotion healingPotion;
    private CombatStatsComponent playerStat;

    @BeforeEach
    void setUp() {
        playerStat = new CombatStatsComponent(50, 50,10,10,10,10);
        healingPotion = new HealingPotion( 3, playerStat);
    }

    @Test
    void testApplyEffect() {
        healingPotion.useItem(null);
        assertEquals(2, healingPotion.getQuantity(), "The potion should have 2 uses left after one use.");

        healingPotion.useItem(null);
        assertEquals(1, healingPotion.getQuantity(), "The potion should have 1 use left after two uses.");

        healingPotion.useItem(null);
        assertTrue(healingPotion.isEmpty(), "The potion should be empty after 3 uses.");

        assertEquals(125, playerStat.getHealth(), "The potion should have 125 health.");
    }
}

