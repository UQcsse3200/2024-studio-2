package com.csse3200.game.inventory.items;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.inventory.items.exceptions.ConsumedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
class ConsumableItemTest {
    private TestableItem item;
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
        item = new TestableItem("test", 3, 10, 3);
        context = new ItemUsageContext();
    }

    @Test
    void testSingleUsage() {
        String msg = "Single use of ConsumableItem should reduce quantity by 1";
        int originalQuantity = item.getQuantity();

        item.useItem(context);
        assertEquals(originalQuantity - 1, item.getQuantity(), msg);
    }

    @Test
    void testManyUsages() {
        for (int i = 0; i < 3; i++) {
            item.useItem(context);
        }
        assertTrue(item.isEmpty(), "Expected item to be empty after 3 uses");

        String msg = "Should throw exception when using empty ConsumableItem";
        assertThrows(ConsumedException.class, () -> item.useItem(context), msg);
    }
}