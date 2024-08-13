package com.csse3200.game.inventory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.inventory.items.ItemUsageContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
public class InventoryTest {
    private Inventory test1;
    private Inventory test2;
    private Inventory test3;
    private Inventory[] tests;

    @BeforeEach
    void setUp() { // Initialise Inventory for testing
        test1 = new Inventory(1);
        test2 = new Inventory(2);
        test3 = new Inventory(10);
        tests = new Inventory[]{test1, test2, test3};
    }

    @Test
    void testNonPositiveCapacity() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Inventory(0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Inventory(-1);
        });
    }

    @Test
    void testInitialisation() {
        String msg = "Something went wrong when initialising an inventory!";
        assertEquals(1, test1.getCapacity(), msg);
        assertEquals(2, test2.getCapacity(), msg);
        assertEquals(10, test3.getCapacity(), msg);

        for (Inventory test : tests) {
            assertEquals(test.numFreeSlots(), test.getCapacity(), msg);
            assertFalse(test.isFull(), msg);
            assertFalse(test.hasItem(0), msg);
            assertEquals(-1, test.getIndex(0), msg);
            assertNull(test.getAt(0), msg);
        }
    }
}