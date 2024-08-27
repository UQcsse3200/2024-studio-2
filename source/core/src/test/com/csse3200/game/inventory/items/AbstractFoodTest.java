package com.csse3200.game.inventory.items;


import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.inventory.items.effects.feeding.FeedEffect;
import com.csse3200.game.inventory.items.food.AbstractFood;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(GameExtension.class)
public class AbstractFoodTest {
    private TestableItem food;
    private TestableEffect effect1;

    private static class TestableItem extends AbstractFood {
        public TestableItem(String name, int itemCode, int limit, int quantity, FeedEffect feedingEffect) {
            super(name,
                    itemCode,
                    limit,
                    quantity,
                    feedingEffect);
        }
    }

    private static class TestableEffect extends FeedEffect {
        public TestableEffect(int feedingEffect) {
            super(feedingEffect);
        }
    }

    @BeforeEach
    void setUp() { // Initialize TestableItem and ItemUsageContext
        effect1 = new TestableEffect(10);
        food = new TestableItem("test", 3, 10, 3, effect1);
    }

    @Test
    public void testApplyEffect() {
        food.useItem(null);
        assertEquals(2, food.getQuantity(), "The food should have 2 uses left after one use.");

        food.useItem(null);
        assertEquals(1, food.getQuantity(), "The food should have 1 use left after two uses.");

        food.useItem(null);
        assertTrue(food.isEmpty(), "The food should be empty after 3 uses.");

        assertEquals(effect1, food.getFeedingEffect());
    }


}
