package com.csse3200.game.inventory.items.potions;


import com.csse3200.game.components.CombatStatsComponent;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.inventory.items.AbstractFoodTest;
import com.csse3200.game.inventory.items.ItemUsageContext;
import com.csse3200.game.inventory.items.food.AbstractFood;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
class AbstractPotionTest  {
    private HealingPotion healingPotion;
    private DefensePotion defensePotion;
    private AttackPotion attackPotion;
    private CombatStatsComponent stat;
    private TestablePLayer player1;


    private static class TestablePLayer extends ItemUsageContext {
        public TestablePLayer(Entity entity) {
            super(entity);
        }
    }

    @BeforeEach
    void setUp() {
        stat = new CombatStatsComponent(0, 0,0,0,0,0);
        player1 = new TestablePLayer(new Entity().addComponent(stat));
        healingPotion = new HealingPotion( 3);
        defensePotion = new DefensePotion( 3);
        attackPotion = new AttackPotion(3);

    }

    @Test
    void testHealingApplyEffect() {
        int originalHealth = player1.player.getComponent(CombatStatsComponent.class).getHealth();
        healingPotion.useItem(player1);
        assertEquals(2, healingPotion.getQuantity(), "The potion should have 2 uses left after one use.");
        assertEquals(25, healingPotion.getEffectAmount());
        assertEquals(originalHealth + 25, player1.player.getComponent(CombatStatsComponent.class).getHealth(), "Should be 25");


        healingPotion.useItem(player1);
        assertEquals(1, healingPotion.getQuantity(), "The potion should have 1 use left after two uses.");

        healingPotion.useItem(player1);
        assertTrue(healingPotion.isEmpty(), "The potion should be empty after 3 uses.");

        assertEquals(100, player1.player.getComponent(CombatStatsComponent.class).getHealth(), "The potion should have 100 health.");
    }

    @Test
    void testDefenseApplyEffect() throws InterruptedException {
        int originalDefense = player1.player.getComponent(CombatStatsComponent.class).getDefense();
        defensePotion.useItem(player1);
        assertEquals(2, defensePotion.getQuantity(), "The potion should have 2 uses left after one use.");
        assertEquals(originalDefense + 25, player1.player.getComponent(CombatStatsComponent.class).getDefense(), "The defense should have 25.");

        Thread.sleep(120000);

        assertEquals(originalDefense, player1.player.getComponent(CombatStatsComponent.class).getDefense(), "The potion has finished and is back to 0");
    }

    @Test
    void testAttackApplyEffect() throws InterruptedException {
        int originalAttack = player1.player.getComponent(CombatStatsComponent.class).getStrength();
        attackPotion.useItem(player1);
        assertEquals(2, attackPotion.getQuantity(), "The potion should have 2 uses left after one use.");
        assertEquals(originalAttack + 25, player1.player.getComponent(CombatStatsComponent.class).getStrength(), "The strength should have 25.");

        Thread.sleep(120000);

        assertEquals(originalAttack, player1.player.getComponent(CombatStatsComponent.class).getStrength(), "The potion has finished and is back to 0");
    }
}

