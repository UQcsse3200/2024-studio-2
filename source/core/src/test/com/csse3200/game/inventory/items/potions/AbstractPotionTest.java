package com.csse3200.game.inventory.items.potions;


import com.csse3200.game.components.CombatStatsComponent;

import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
class AbstractPotionTest  {
    private HealingPotion healingPotion;
    private DefensePotion defensePotion;
    private AttackPotion attackPotion;

    @BeforeEach
    void setUp() {
        healingPotion = new HealingPotion( 3);
        defensePotion = new DefensePotion( 3);
        attackPotion = new AttackPotion(3);

    }

    @Test
    void testHealingApplyEffect() {
//        int originalHealth = playerStat.getHealth();
        healingPotion.useItem(null);
        assertEquals(2, healingPotion.getQuantity(), "The potion should have 2 uses left after one use.");
        assertEquals(25, healingPotion.getEffectAmount());
//        assertEquals(originalHealth + 25, playerStat.getHealth());


        healingPotion.useItem(null);
        assertEquals(1, healingPotion.getQuantity(), "The potion should have 1 use left after two uses.");

        healingPotion.useItem(null);
        assertTrue(healingPotion.isEmpty(), "The potion should be empty after 3 uses.");

//        assertEquals(100, playerStat.getHealth(), "The potion should have 100 health.");
    }

    @Test
    void testDefenseApplyEffect() throws InterruptedException {
//        int originalDefense = playerStat1.getDefense();
        defensePotion.useItem(null);
        assertEquals(2, defensePotion.getQuantity(), "The potion should have 2 uses left after one use.");
//        assertEquals(originalDefense + 25, playerStat1.getDefense(), "The defense should have 25.");

        Thread.sleep(120000);

//        assertEquals(originalDefense, playerStat1.getDefense(), "The potion has finished and is back to 0");
    }

    @Test
    void testAttackApplyEffect() throws InterruptedException {
//        int originalAttack = playerStat1.getStrength();
        attackPotion.useItem(null);
        assertEquals(2, attackPotion.getQuantity(), "The potion should have 2 uses left after one use.");
//        assertEquals(originalAttack + 25, playerStat1.getStrength(), "The strength should have 25.");

        Thread.sleep(120000);

//        assertEquals(originalAttack, playerStat1.getStrength(), "The potion has finished and is back to 0");
    }
}

