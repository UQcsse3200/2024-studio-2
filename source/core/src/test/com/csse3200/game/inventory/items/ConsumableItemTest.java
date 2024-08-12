package com.csse3200.game.inventory.items;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.csse3200.game.extensions.GameExtension;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
public class ConsumableItemTest {
    private static TestableItem1 item1;
    private static ItemUsageContext context;

    /**
     * Concrete subclass of ConsumableItems for testing purposes.
     */
    private static class TestableItem1 extends ConsumableItem {
        public int numUsed = 0;

        public TestableItem1(String name, int itemCode, int limit, int quantity) {
            super(name, quantity, quantity, isEmpty);
        }

        @Override
        public void useItem(ItemUsageContext context) {
            // For testing purposes only
            numUsed++;
            quantity--;
        }

        @BeforeEach
        void setUp() { // Initialize TestableItem and ItemUsageContext
            item1 = new TestableItem1("Test1", 1, 10, 1);
            context = new ItemUsageContext();
        }

        @Test
        void testIfNotEmpty() {
            String msg = "Checking if consumable item is not empty: ";
            assertFalse(item1.isEmpty(), msg + "is empty is false");
        }

        @Test
        void testIfEmpty() {
            String msg = "Checking if consumable item is empty: ";
            int originalQuantity = item1.getQuantity();

            item1.useItem(context);
            assertTrue(item1.isEmpty(), msg + "is empty is true");
        }
    }
}
