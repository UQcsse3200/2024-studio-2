package com.csse3200.game.inventory.items;


import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.inventory.items.effects.feeding.FeedEffect;
import com.csse3200.game.inventory.items.food.AbstractFood;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
public class AbstractFoodTest {
    private TestableItem item;
    private TestableEffect effect1;
    private static ItemUsageContext context;

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
        item = new TestableItem("test", 3, 10, 3, effect1);
        context = new ItemUsageContext();
    }
}
