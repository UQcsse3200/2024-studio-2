package com.csse3200.game.inventory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

    @BeforeEach
    void setUp() { // Initialise Inventory for testing
        this.test1 = new Inventory(1);
        this.test2 = new Inventory(2);
        this.test3 = new Inventory(10);
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
        assertEquals(test1.numFreeSlots(), test1.getCapacity(), msg);
        assertEquals(test2.numFreeSlots(), test2.getCapacity(), msg);
        assertEquals(test3.numFreeSlots(), test3.getCapacity(), msg);
        assertFalse(test1.isFull(), msg);
        assertFalse(test2.isFull(), msg);
        assertFalse(test3.isFull(), msg);
    }
}