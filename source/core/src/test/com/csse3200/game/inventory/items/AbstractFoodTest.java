package com.csse3200.game.inventory.items;


import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
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
    private TestablePLayer player1;
    private CombatStatsComponent stat;


    private static class TestablePLayer extends ItemUsageContext {
        public TestablePLayer(Entity entity) {
            super(entity);
        }
    }

    private static class TestableItem extends AbstractFood{
        public TestableItem(String name, int itemCode, int limit, int quantity, int feedingEffect) {
            super(name,
                    itemCode,
                    limit,
                    quantity,
                    feedingEffect);
        }
    }

    @BeforeEach
    void setUp() { // Initialize TestableItem and ItemUsageContext
        stat = new CombatStatsComponent(50, 50,50,50,50,50);
        player1 = new TestablePLayer(new Entity().addComponent(stat));
        food = new TestableItem("test", 3, 10, 3, 10);
    }

    @Test
    public void testApplyEffect() {
        food.useItem(player1);
        assertEquals(2, food.getQuantity(), "The food should have 2 uses left after one use.");
        assertEquals(60, player1.player.getComponent(CombatStatsComponent.class).getHunger(), "The hunger should be 60");

        food.useItem(player1);
        assertEquals(1, food.getQuantity(), "The food should have 1 use left after two uses.");
        assertEquals(70, player1.player.getComponent(CombatStatsComponent.class).getHunger(), "The hunger should be 70");

        food.useItem(player1);
        assertTrue(food.isEmpty(), "The food should be empty after 3 uses.");
        assertEquals( 80, player1.player.getComponent(CombatStatsComponent.class).getHunger(), "The hunger should be 80");
    }
}
