package com.csse3200.game.inventory;

import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.gamestate.GameState;
import com.csse3200.game.gamestate.SaveHandler;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.inventory.items.ConsumableItem;
import com.csse3200.game.inventory.items.ItemUsageContext;
import com.csse3200.game.inventory.items.food.Foods;
import com.csse3200.game.inventory.items.potions.HealingPotion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
class InventoryTest {
    private Inventory test1;
    private Inventory test2;
    private Inventory[] tests;
    private AbstractItem[] items;
    private ItemUsageContext context;

    /**
     * Concrete subclass of ConsumableItem for testing purposes.
     */
    private static class TestableItem extends ConsumableItem {

        TestableItem(String name, int itemCode) {
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
        GameState.inventory.inventoryContent = new AbstractItem[0];
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
            assertFalse(test.hasItem("test_0"), msg);
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

        test.addAt(0, items[0]);
        assertTrue(test.hasItem(items[0].getItemCode()));
        test.clearInventory();
        assertFalse(test.hasItem(items[0].getItemCode()));
    }

    @Test
    void testBasicAddAndDelete() {
        // Check add and delete works with a single items
        assertNotEquals(-1, test1.add(items[0]));
        assertTrue(test1.hasItem(items[0].getItemCode()));
        assertEquals(0, test1.getIndex(items[0].getItemCode()));
        assertEquals(0, test1.getIndex(items[0].getName()));
        assertEquals(-1, test1.getIndex("Not in Inventory!"));
        assertEquals(items[0].getItemCode(), test1.getAt(0).getItemCode());
        assertTrue(test1.isFull());
        test1.deleteItemAt(0);
        assertFalse(test1.hasItem(items[0].getItemCode()));
    }

    @Test
    void testComplexAddAndDelete() {
        // Fill up inventory with items
        for (int i = 0; i < 3; i++) {
            assertNotEquals(-1, test2.add(items[i]));
        }
        assertTrue(test2.hasItem(items[2].getItemCode()));
        assertFalse(test2.hasItem(items[3].getItemCode()));
        assertEquals(items[0].getItemCode(), test2.getAt(0).getItemCode());
        assertTrue(test2.isFull());

        // Check adding a new item now does nothing.
        assertEquals(-1, test2.add(items[3]));
        assertFalse(test2.hasItem(items[3].getItemCode()));

        // Check deleting then adding an item works
        test2.deleteItem(items[0].getItemCode());
        assertFalse(test2.hasItem(items[0].getItemCode()));
        assertNotEquals(-1, test2.add(items[3]));
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
        assertNotEquals(-1, test1.add(items[0]));
        test1.useItemAt(0, context);
        assertTrue(test1.hasItem(items[0].getItemCode()));

        // Add an extra test item and use once, and check it is added to the same slot
        TestableItem item = new TestableItem("test_" + 0, 0);
        item.useItem(context);
        assertNotEquals(-1, test1.add(item));
        test1.useItemAt(0, context);

        test1.useItem(items[0].getItemCode(), context);
        assertFalse(test1.hasItem(items[0].getItemCode()));
    }

    @Test
    void testComplexAddAndUse() {
        // Add multiple to inventory with multiple indexes, use varying number of times and check
        // whether items are gone or not
        assertNotEquals(-1, test2.add(items[0]));
        assertNotEquals(-1, test2.add(items[1]));
        test2.useItem(items[0].getItemCode(), context);
        assertTrue(test2.hasItem(items[0].getItemCode()));
        assertTrue(test2.hasItem(items[1].getItemCode()));
        test2.useItem(items[0].getItemCode(), context);
        assertFalse(test2.hasItem(items[0].getItemCode()));
        assertTrue(test2.hasItem(items[1].getItemCode()));

        // Create 2 new test items with same item code.
        TestableItem x1 = new TestableItem("test", 0);
        TestableItem x2 = new TestableItem("test", 0);
        test2.clearInventory();
        assertNotEquals(-1, test2.add(x1));
        assertNotEquals(-1, test2.add(x2));

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

    @Test
    void testSortByCode() {
        // Add items to inventory in reverse (descending) order
        Inventory inventory = new Inventory(13);
        for (int i = 0; i < 5; i++) {
            inventory.add(new TestableItem("test_" + i, 4 - i));
        }

        // Check items are input in reverse (descending) order
        for (int i = 0; i < 5; i++) {
            assertEquals(4 - i, inventory.getAt(i).getItemCode());
        }

        // Sort items and check they are in correct (ascending) order.
        inventory.sortByCode();
        for (int i = 0; i < 5; i++) {
            assertEquals(i, inventory.getAt(i).getItemCode());
        }
    }

    @Test
    void shouldSaveLoadInventoryContents() {
        GameState.clearState();

        Inventory inventory = new Inventory(3);
        inventory.loadInventoryFromSave();

        inventory.add(new Foods.Apple(1));
        inventory.add(new HealingPotion(2));

        SaveHandler.save(GameState.class, "test/saves/inventory", FileLoader.Location.LOCAL);

        GameState.inventory.inventoryContent = new AbstractItem[0];

        SaveHandler.load(GameState.class, "test/saves/inventory", FileLoader.Location.LOCAL);

        inventory.loadInventoryFromSave();

        assertTrue(inventory.hasItem("Health Potion"));
        assertTrue(inventory.hasItem("Apple"));

        assertEquals(1, inventory.getAt(inventory.getIndex("Apple")).getQuantity());
        assertEquals("This is a health potion",
                inventory.getAt(inventory.getIndex("Health Potion")).getDescription());

        SaveHandler.delete(GameState.class, "test/saves/inventory", FileLoader.Location.LOCAL);
    }

    @Test
    void testSwap() {
        Inventory inventory = new Inventory(3);
        AbstractItem itemInS0;
        AbstractItem itemInS1;

        // 1. Swapping 2 items
            inventory.add(new Foods.Apple(1));
            inventory.add(new HealingPotion(2));

            // Before swapping
            itemInS0 = inventory.getAt(0); // Apple
            itemInS1 = inventory.getAt(1); // Healing potion

            inventory.swap(0, 1);

            // After swapping
            AbstractItem swapItems0 = inventory.getAt(0);//Healing potion
            AbstractItem swapItems1 = inventory.getAt(1);//Apple

            // Assert that items have been swapped correctly
            assertEquals(itemInS1,swapItems0);
            assertEquals(itemInS0,swapItems1);
            inventory.clearInventory();

        // 2. Swapping 1 item and null
            inventory.add(new Foods.Apple(1));

            // Before swapping
            itemInS0 = inventory.getAt(0); // Apple
            itemInS1 = inventory.getAt(1); // null

            inventory.swap(0, 1);

            // After swapping
            swapItems0 = inventory.getAt(0);//null
            swapItems1 = inventory.getAt(1);//Apple

            // Assert that items have been swapped correctly
            assertEquals(itemInS1,swapItems0);
            assertEquals(itemInS0,swapItems1);
            inventory.clearInventory();

        // 2. Swapping 2 null
            // Before swapping
            itemInS0 = inventory.getAt(0); // null
            itemInS1 = inventory.getAt(1); // null

            inventory.swap(0, 1);

            // Assert that items have been swapped correctly
            assertNull(itemInS1);
            assertNull(itemInS0);
            inventory.clearInventory();
    }

}