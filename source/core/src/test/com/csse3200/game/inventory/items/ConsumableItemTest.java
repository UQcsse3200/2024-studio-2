package com.csse3200.game.inventory.items;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
public class ConsumableItemTest {
    private TestableItem item1;
    private static ItemUsageContext context;

    /**
     * Concrete subclass of ConsumableItems for testing purposes.
     */
    private static class TestableItem extends ConsumableItem {

        public TestableItem(String name, int itemCode, int limit, int quantity) {
            super(name, itemCode, limit, quantity);
        }
    }

    @BeforeEach
    void setUp() { // Initialize TestableItem and ItemUsageContext
        item1 = new TestableItem("Test1", 3, 10, 3);
        context = new ItemUsageContext();
    }

    @Test
    void testIfConsumableItemIsUsed() {
        String msg = "Checking if consumable item is used: ";
        int originalQuantity = item1.getQuantity();

        item1.useItem(context);
        assertNotEquals(originalQuantity, item1.getQuantity());
    }

    @Test
    void testConsumableItemUsedThrowsError() {
        String msg = "Checking if consumable item is empty: ";

        item1.useItem(context);
        item1.useItem(context);
        item1.useItem(context);
        assertTrue(item1.isEmpty(), msg + "is empty is true");

        assertThrows(IllegalStateException.class, () -> item1.useItem(context));
    }
}
