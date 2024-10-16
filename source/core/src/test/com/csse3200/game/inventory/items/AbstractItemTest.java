package com.csse3200.game.inventory.items;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
class AbstractItemTest {
    private TestableItem1 item1;
    private TestableItem2 item2;
    private ItemUsageContext context;

    /**
     * Concrete subclass of AbstractItem for testing purposes.
     */
    private static class TestableItem1 extends AbstractItem {
        public int numUsed = 0;

        TestableItem1(String name, int itemCode, String description) {
            super(name, itemCode);
            this.setDescription(description);
        }

        @Override
        public void useItem(ItemUsageContext context) {
            // For testing purposes only
            numUsed++;
        }
    }

    /**
     * Concrete subclass of AbstractItem for testing purposes.
     */
    private static class TestableItem2 extends AbstractItem {
        int numUsed = 0;

        TestableItem2(String name, int itemCode, int limit, int quantity, String description) {
            super(name, itemCode, limit, quantity);
            this.setDescription(description);

        }

        @Override
        public void useItem(ItemUsageContext context) {
            // For testing purposes only
            numUsed++;
            quantity--;
        }
    }

    @BeforeEach
    void setUp() { // Initialize TestableItem and ItemUsageContext
        item1 = new TestableItem1("Test1", 0, "description1");
        item2 = new TestableItem2("Test2", 1, 10, 5, "description2");
        context = new ItemUsageContext();
    }

    @Test
    void testInitialiserWithoutLimit() {
        String msg = "Initialiser without limit/quantity failed when checking: ";
        assertEquals("Test1", item1.getName(), msg + "name");
        assertEquals(0, item1.getItemCode(), msg + "item code");
        assertEquals(1, item1.getLimit(), msg + "limit");
        assertEquals(1, item1.getQuantity(), msg + "quantity");
        assertEquals(item1.getLimit() - item1.getQuantity(), item1.numAddable(), msg + "can add");
        assertEquals("description1", item1.getDescription(), msg + "description");

        assertThrows(IllegalArgumentException.class, () -> item1.add(1), msg + "adding too many");

        assertFalse(item1.isEmpty(), msg + "is empty is false");
    }

    @Test
    void testIllegalInitialisation() {
        assertThrows(IllegalArgumentException.class, () -> {
            // Call the method that is expected to throw the exception
            new TestableItem2("test", -1, -1, 0, "d");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            // Call the method that is expected to throw the exception
            new TestableItem2("test", -1, 0, -1, "d");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            // Call the method that is expected to throw the exception
            new TestableItem2("test", -1, 1, 2, "d");
        });
    }

    @Test
    void testIllegalAccess() {
        item1.setDescription(null);
        assertThrows(IllegalAccessError.class, () -> item1.getDescription());
        item2.setTexturePath(null);
        assertThrows(IllegalAccessError.class, () -> item2.getTexturePath());
    }

    @Test
    void testInitialiserWithLimit() {
        String msg = "Initialiser with limit/quantity failed when checking: ";
        assertEquals("Test2", item2.getName(), msg + "name");
        assertEquals(1, item2.getItemCode(), msg + "item code");
        assertEquals(10, item2.getLimit(), msg + "limit");
        assertEquals(5, item2.getQuantity(), msg + "quantity");
        assertEquals(item2.getLimit() - item2.getQuantity(), item2.numAddable(), msg + "can add");
        assertEquals("description2", item2.getDescription(), msg + "description");

        assertThrows(IllegalArgumentException.class, () -> item2.add(20), msg + "adding too many");
        int originalQuantity = item2.getQuantity();
        item2.add(1);
        assertEquals(originalQuantity + 1, item2.getQuantity(), msg + "adding 1");

        assertFalse(item2.isEmpty(), msg + "is empty is false");
        int n = item2.getQuantity();
        for (int i = 0; i < n; i++) {
            item2.useItem(context);
        }
        assertTrue(item2.isEmpty(), msg + "is empty is true");
    }

    @Test
    void testUseItem() {
        String msg = "Use item failed ";

        item1.useItem(context);
        assertEquals(1, item1.numUsed, msg);

        int originalQuantity = item2.getQuantity();
        item2.useItem(context);
        assertEquals(1, item2.numUsed, msg);
        assertEquals(originalQuantity - 1, item2.getQuantity(), msg);
    }
}