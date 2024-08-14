package com.csse3200.game.inventory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.inventory.items.ConsumableItem;
import com.csse3200.game.inventory.items.ItemUsageContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
public class InventoryTest {
    private Inventory test1;
    private Inventory test2;
    private Inventory[] tests;
    private AbstractItem[] items;
    private ItemUsageContext context;

    /**
     * Concrete subclass of ConsumableItem for testing purposes.
     */
    private static class TestableItem extends ConsumableItem {

        public TestableItem(String name, int itemCode) {
            super(name, itemCode, 2, 2);
        }

        @Override
        public void useItem(ItemUsageContext context) {
            // For testing purposes only
            super.useItem(context);
        }
    }

    @BeforeEach
    void setUp() { // Initialise Inventory for testing
        test1 = new Inventory(1);
        test2 = new Inventory(3);
        tests = new Inventory[]{test1, test2};
        items = new AbstractItem[5];
        for (int i = 0; i < 5; i++) {
            items[i] = new TestableItem("test_" + i, i);
        }

        context = new ItemUsageContext();
    }

    @Test
    void testNonPositiveCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new Inventory(0));
        assertThrows(IllegalArgumentException.class, () -> new Inventory(-1));
    }

    @Test
    void testInitialisation() {
        String msg = "Something went wrong when initialising an inventory!";
        assertEquals(1, test1.getCapacity(), msg);
        assertEquals(3, test2.getCapacity(), msg);

        for (Inventory test : tests) {
            assertEquals(test.numFreeSlots(), test.getCapacity(), msg);
            assertFalse(test.isFull(), msg);
            assertFalse(test.hasItem(0), msg);
            assertEquals(-1, test.getIndex(0), msg);
            assertNull(test.getAt(0), msg);
        }
    }

    @Test
    void testClearAll() {
        Inventory test = new Inventory(10);
        for (AbstractItem item : items) {
            test.add(item);
        }

        test.clearInventory();

        for (AbstractItem item : items) {
            assertFalse(test.hasItem(item.getItemCode()));
        }
    }

    @Test
    void testBasicAddAndDelete() {
        // Check add and delete works with a single item
        test1.add(items[0]);
        assertTrue(test1.hasItem(items[0].getItemCode()));
        assertEquals(0, test1.getIndex(items[0].getItemCode()));
        assertEquals(items[0].getItemCode(), test1.getAt(0).getItemCode());
        assertTrue(test1.isFull());
        test1.deleteItem(items[0].getItemCode());
        assertFalse(test1.hasItem(items[0].getItemCode()));
    }

    @Test
    void testComplexAddAndDelete() {
        // Fill up inventory with items
        for (int i = 0; i < 3; i++) {
            test2.add(items[i]);
        }
        assertTrue(test2.hasItem(items[2].getItemCode()));
        assertFalse(test2.hasItem(items[3].getItemCode()));
        assertEquals(items[0].getItemCode(), test2.getAt(0).getItemCode());
        assertTrue(test2.isFull());

        // Check adding a new item now does nothing.
        test2.add(items[3]);
        assertFalse(test2.hasItem(items[3].getItemCode()));

        // Check deleting then adding an item works
        test2.deleteItem(items[0].getItemCode());
        assertFalse(test2.hasItem(items[0].getItemCode()));
        test2.add(items[3]);
        assertTrue(test2.hasItem(items[3].getItemCode()));

        // Check replacing an item at an index works.
        test2.addAt(1, items[4]);
        assertTrue(test2.hasItem(items[4].getItemCode()));
        assertFalse(test2.hasItem(items[1].getItemCode()));
        assertEquals(items[4].getItemCode(), test2.getAt(1).getItemCode());

        // Test clearing inventory works:
        test2.clearInventory();
        assertEquals(3, test2.getCapacity());
        assertEquals(3, test2.numFreeSlots());
        for (AbstractItem item : items) {
            assertFalse(test2.hasItem(item.getItemCode()));
        }
    }

    @Test
    void testBasicAddAndUse() {
        // Add to inventory with single index, use it twice - check it has gone.
        test1.add(items[0]);
        test1.useItem(items[0].getItemCode(), context);
        assertTrue(test1.hasItem(items[0].getItemCode()));
        test1.useItemAt(0, context);
        assertFalse(test1.hasItem(items[0].getItemCode()));
    }

    @Test
    void testComplexAddAndUse() {
        // Add multiple to inventory with multiple indexes, use varying times and check whether
        // gone or not
        test2.add(items[0]);
        test2.add(items[1]);
        test2.useItem(items[0].getItemCode(), context);
        assertTrue(test2.hasItem(items[0].getItemCode()));
        assertTrue(test2.hasItem(items[1].getItemCode()));
        test2.useItem(items[0].getItemCode(), context);
        assertTrue(test2.hasItem(items[0].getItemCode()));
        assertFalse(test2.hasItem(items[1].getItemCode()));

        // Create 2 new test items with same item code.
        TestableItem x1 = new TestableItem("test", 0);
        TestableItem x2 = new TestableItem("test", 0);
        test2.clearInventory();
        test2.add(x1);
        test2.add(x2);

        // Check using second item doesn't affect first
        test2.useItemAt(1, context);
        test2.useItemAt(1, context);
        assertTrue(test2.hasItem(0)); // Should only have deleted 1 stack
        assertNull(test2.getAt(1));

        // Check inventory does not have the test items after using both.
        test2.useItem(0, context);
        test2.useItem(0, context);
        assertFalse(test2.hasItem(0));
    }
}