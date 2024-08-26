package com.csse3200.game.inventory.items.potions;


import com.csse3200.game.inventory.items.potions.healingpotion.HealingPotion;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
public class AbstractPotionTest  {
    private HealingPotion healingPotion;

    @BeforeEach
    public void setUp() {
        healingPotion = new HealingPotion( 3);
    }

    @Test
    public void testApplyEffect() {
        healingPotion.useItem(null);
        assertEquals(2, healingPotion.getQuantity(), "The potion should have 2 uses left after one use.");

        healingPotion.useItem(null);
        assertEquals(1, healingPotion.getQuantity(), "The potion should have 1 use left after two uses.");

        healingPotion.useItem(null);
        assertTrue(healingPotion.isEmpty(), "The potion should be empty after 3 uses.");
    }
}

