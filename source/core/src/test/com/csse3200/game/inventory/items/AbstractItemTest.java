package com.csse3200.game.inventory.items;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
public class AbstractItemTest {
    private TestableItem item;
    private ItemUsageContext context;

    /**
     * Concrete subclass of AbstractItem for testing purposes.
     */
    private static class TestableItem extends AbstractItem {
        public int numUsed = 0;

        public TestableItem(String name, int limit, int quantity) {
            super(name, limit, quantity);
        }

        @Override
        public void useItem(ItemUsageContext context) {
            // For testing purposes only
            numUsed++;
        }
    }

    @BeforeEach
    void setUp() { // Initialize TestableItem and ItemUsageContext
        item = new TestableItem("TestItem", 7, 5);
        context = new ItemUsageContext();
    }

    @Test
    void testGetName() {
        assertEquals("TestItem", item.getName(), "The name of the item should be 'TestItem'.");
    }

    @Test
    void testGetLimit() {
        assertEquals(7, item.getLimit(), "The limit of the item should be 7.");
    }

    @Test
    void testGetQuantity() {
        assertEquals(5, item.getQuantity(), "The quantity of the item should be 5.");
    }

    @Test
    void testUseItem() {
        item.useItem(context);
        assertEquals(1, item.numUsed, "NumUsed should return 1.");
    }
}