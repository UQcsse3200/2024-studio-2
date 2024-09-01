package com.csse3200.game.inventory.items;


import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.inventory.items.food.AbstractFood;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(GameExtension.class)
public class AbstractFoodTest {
    private TestableItem food;
    private TestableItem food1;
    private TestableStat stats1;
    private TestableStat stats;

    private static class TestableItem extends AbstractFood {
        public TestableItem(String name, int itemCode, int limit, int quantity, int feedingEffect, CombatStatsComponent stat) {
            super(name,
                    itemCode,
                    limit,
                    quantity,
                    feedingEffect,
                    stat);
        }
    }

    private static class TestableStat extends CombatStatsComponent {
        public TestableStat(int health, int hunger, int strength, int defense, int speed, int experience) {
            super(health, hunger, strength, defense, speed, experience);
        }
}

    @BeforeEach
    void setUp() { // Initialize TestableItem and ItemUsageContext
        //effect1 = new TestableEffect(10);
        stats = new TestableStat(50, 0, 50, 50, 50, 50);
        stats1 = new TestableStat(50, 95, 50, 50, 50, 50);
        food = new TestableItem("test", 3, 10, 3, 10, stats);
        food1 = new TestableItem("test1", 3, 10, 3, 10, stats1);
    }

    @Test
    public void testApplyEffect() {
        int orignalHunger = stats.getHunger();

        food.useItem(null);
        assertEquals(2, food.getQuantity(), "The food should have 2 uses left after one use.");
        assertEquals(orignalHunger + 10, stats.getHunger(), "The hunger should be 10");
        food.useItem(null);
        assertEquals(1, food.getQuantity(), "The food should have 1 use left after two uses.");
        assertEquals(orignalHunger + 20, stats.getHunger(), "The hunger should be 20");
        food.useItem(null);
        assertTrue(food.isEmpty(), "The food should be empty after 3 uses.");
        assertEquals(orignalHunger + 30, stats.getHunger(), "The hunger should be 30");
    }

    @Test
    public void testMaxHunger() {
        food1.useItem(null);
        assertEquals(100, stats.getHunger(), "The hunger should be 100");
    }


}
