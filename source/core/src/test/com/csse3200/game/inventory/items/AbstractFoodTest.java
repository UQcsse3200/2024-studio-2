package com.csse3200.game.inventory.items;

//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.junit.jupiter.api.Assertions.assertEquals;

import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.inventory.items.effects.AbstractEffect;
import com.csse3200.game.inventory.items.effects.feeding.FeedEffect;
import com.csse3200.game.inventory.items.food.AbstractFood;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
public class AbstractFoodTest {
    private AbstractFoodTest.TestableItem item;
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

//    @BeforeEach
//    void setUp() { // Initialize TestableItem and ItemUsageContext
//        item = new TestableItem("test", 3, 10, 3, 10);
//        context = new ItemUsageContext();
//    }
}
