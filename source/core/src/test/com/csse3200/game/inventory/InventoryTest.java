package com.csse3200.game.inventory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.inventory.items.AbstractItem;
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

    /**
     * Concrete subclass of AbstractItem for testing purposes.
     */
    private static class TestableItem extends AbstractItem {
        public int numUsed = 0;

        public TestableItem(String name, int itemCode) {
            super(name, itemCode);
        }

        @Override
        public void useItem(ItemUsageContext context) {
            // For testing purposes only
            numUsed++;
        }
    }

    @BeforeEach
    void setUp() { // Initialise Inventory for testing
        test1 = new Inventory(1);
        test2 = new Inventory(10);
        tests = new Inventory[]{test1, test2};
        items = new AbstractItem[5];
        for (int i = 0; i < 5; i++) {
            items[i] = new TestableItem("test_" + i, i);
        }
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
        assertEquals(10, test2.getCapacity(), msg);

        for (Inventory test : tests) {
            assertEquals(test.numFreeSlots(), test.getCapacity(), msg);
            assertFalse(test.isFull(), msg);
            assertFalse(test.hasItem(0), msg);
            assertEquals(-1, test.getIndex(0), msg);
            assertNull(test.getAt(0), msg);
        }
    }

    @Test
    void testAddAndDelete() {
        // Check add and delete works with a single item
        test1.add(items[0]);
        assertTrue(test1.hasItem(items[0].getItemCode()));
        assertEquals(0, test1.getIndex(items[0].getItemCode()));
        assertEquals(items[0].getItemCode(), test1.getAt(0).getItemCode());
        assertTrue(test1.isFull());
        test1.deleteItem(items[0].getItemCode());
        assertFalse(test1.hasItem(items[0].getItemCode()));

        // Check add and delete works as expected with multiple items


        //assert false; // TODO (make sure to test get etc.)
    }

    @Test
    void testAddAndUse() {
        assert false; // TODO
    }

}