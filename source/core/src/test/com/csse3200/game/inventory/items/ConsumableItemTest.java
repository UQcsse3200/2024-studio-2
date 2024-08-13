package com.csse3200.game.inventory.items;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.internal.matchers.text.ValuePrinter.print;

import com.csse3200.game.extensions.GameExtension;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
public class ConsumableItemTest {
    private TestableItem1 item1;
    private TestableItem1 item2;
    private static ItemUsageContext context;

    /**
     * Concrete subclass of ConsumableItems for testing purposes.
     */
    private static class TestableItem1 extends ConsumableItem {
        public int numUsed = 0;

        public TestableItem1(String name, int itemCode, int limit, int quantity) {
            super(name, itemCode, limit, quantity);
        }

        @Override
        public void useItem(ItemUsageContext inputs) {
            //for testing
            if (super.isEmpty()) {
                throw new ArrayIndexOutOfBoundsException("This item has ran out of uses");
            }
            else {
                this.quantity--;
                super.isEmpty();
            }
        }
    }

    @BeforeEach
    void setUp() { // Initialize TestableItem and ItemUsageContext
        item1 = new TestableItem1("Test1", 3, 10, 3);
        item2 = new TestableItem1("Test2", 3, 10, 4);
        context = new ItemUsageContext();
    }

    @Test
    void testIfConsumableItemIsUsed() {
        String msg = "Checking if consumable item is used: ";
        int originalQuantity = item2.getQuantity();

        item2.useItem(context);
        assertNotEquals(originalQuantity, item2.quantity);
    }

    @Test
    void testIfEmpty() {
        String msg = "Checking if consumable item is empty: ";
        int originalQuantity = item1.getQuantity();

        item1.useItem(context);
        item1.useItem(context);
        item1.useItem(context);
        assertTrue(item1.isEmpty(), msg + "is empty is true");

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> item1.useItem(context));
    }
}
