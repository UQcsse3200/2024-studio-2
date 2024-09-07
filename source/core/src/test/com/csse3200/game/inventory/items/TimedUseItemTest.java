package com.csse3200.game.inventory.items;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.inventory.items.potions.AttackPotion;
import com.csse3200.game.inventory.items.potions.DefensePotion;
import com.csse3200.game.inventory.items.potions.HealingPotion;
import com.csse3200.game.inventory.items.potions.SpeedPotion;
import com.csse3200.game.services.GameTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
class TimedUseItemTest  {
    private HealingPotion healingPotion;
    private DefensePotion defensePotion;
    private AttackPotion attackPotion;
    private SpeedPotion speedPotion;
    private CombatStatsComponent stat;
    private TestablePLayer player1;
    private GameTime gameTime;
    private static final long DURATION = 120000;


    private static class TestablePLayer extends ItemUsageContext {
        public TestablePLayer(Entity entity) {
            super(entity);
        }
    }

    @BeforeEach
    void setUp() {
        stat = new CombatStatsComponent(0, 0,0,0,0,0);
        player1 = new TestablePLayer(new Entity().addComponent(stat));
        gameTime = Mockito.mock(GameTime.class);
        healingPotion = new HealingPotion( 3);
        Mockito.when(gameTime.getTime()).thenReturn(System.currentTimeMillis());

        defensePotion = new DefensePotion( 3, gameTime);
        attackPotion = new AttackPotion(3, gameTime);
        speedPotion = new SpeedPotion(3, gameTime);
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

        assertEquals(75, player1.player.getComponent(CombatStatsComponent.class).getHealth(), "The potion should have 100 health.");
    }

    @Test
    void testDefenseApplyEffect() throws InterruptedException {
        int originalDefense = player1.player.getComponent(CombatStatsComponent.class).getDefense();
        defensePotion.useItem(player1);
        Mockito.when(gameTime.getTime()).thenReturn(System.currentTimeMillis() + DURATION / 2);
        defensePotion.update(player1);
        assertEquals(originalDefense + defensePotion.getEffectAmount(), player1.player.getComponent(CombatStatsComponent.class).getDefense());

        Mockito.when(gameTime.getTime()).thenReturn(System.currentTimeMillis() + DURATION + 1);
        defensePotion.update(player1);
        assertEquals(originalDefense, player1.player.getComponent(CombatStatsComponent.class).getDefense());
    }

    @Test
    void testAttackApplyEffect() throws InterruptedException {
        int originalAttack = player1.player.getComponent(CombatStatsComponent.class).getStrength();
        attackPotion.useItem(player1);
        Mockito.when(gameTime.getTime()).thenReturn(System.currentTimeMillis() + DURATION / 2);
        attackPotion.update(player1);
        assertEquals(originalAttack + 25, player1.player.getComponent(CombatStatsComponent.class).getStrength());

        Mockito.when(gameTime.getTime()).thenReturn(System.currentTimeMillis() + DURATION + 1);
        attackPotion.update(player1);
        assertEquals(originalAttack, player1.player.getComponent(CombatStatsComponent.class).getStrength());
    }

    @Test
    void testSpeedApplyEffect() throws InterruptedException {
        int originalSpeed = player1.player.getComponent(CombatStatsComponent.class).getSpeed();
        speedPotion.useItem(player1);
        Mockito.when(gameTime.getTime()).thenReturn(System.currentTimeMillis() + DURATION / 2);
        speedPotion.update(player1);
        assertEquals(originalSpeed + 25, player1.player.getComponent(CombatStatsComponent.class).getSpeed());

        Mockito.when(gameTime.getTime()).thenReturn(System.currentTimeMillis() + DURATION + 1);
        speedPotion.update(player1);
        assertEquals(originalSpeed, player1.player.getComponent(CombatStatsComponent.class).getSpeed());
    }
}

