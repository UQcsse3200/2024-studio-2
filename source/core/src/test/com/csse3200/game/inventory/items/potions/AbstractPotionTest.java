package com.csse3200.game.inventory.items.potions;


import com.csse3200.game.inventory.items.potions.healingpotion.HealingPotion;



import static org.junit.jupiter.api.Assertions.assertEquals;

import com.csse3200.game.inventory.items.AbstractItemTest;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
@ExtendWith(GameExtension.class)
public class AbstractPotionTest  {
    private HealingPotion healingPotion;

    @BeforeEach
    public void setUp() {
        healingPotion = new HealingPotion(3);
    }

    @Test
    public void testApplyEffect() {
        healingPotion.useItem(null);
        assertEquals(2, healingPotion.getUses(), "The potion should have 2 uses left after one use.");

        healingPotion.useItem(null);
        assertEquals(1, healingPotion.getUses(), "The potion should have 1 use left after two uses.");
    }

    @Test
    public void testPotionIsConsumed() {
        healingPotion.useItem(null);
        healingPotion.useItem(null);
        healingPotion.useItem(null);

        Assertions.assertTrue(healingPotion.isEmpty(), "The potion should be empty after 3 uses.");
    }



}

