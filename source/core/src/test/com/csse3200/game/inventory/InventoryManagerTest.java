package com.csse3200.game.inventory;

import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.inventory.items.TestItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InventoryManagerTest {
    private InventoryManager inventoryManagerThree;
    private InventoryManager inventoryManagerOne;
    private TestItem item1;
    private TestItem item2;
    private TestItem item3;

    @Before
    public void setUp() {
        Inventory inventory1 = new Inventory(1);
        Inventory inventory3 = new Inventory(3);

        item1 = new TestItem("Apple", 10, 5, 3, "fruit");
        item2 = new TestItem("Sword", 99, 10, 10, "weapon");
        item3 = new TestItem("Grape", 4, 3, 2, "food");

        inventoryManagerOne = new InventoryManager(1);
        inventoryManagerThree = new InventoryManager(3);

        inventoryManagerOne.add(item1);

        inventoryManagerThree.add(item1);
        inventoryManagerThree.add(item2);
        inventoryManagerThree.add(item3);
    }

    @Test
    public void testSortByName() {
        // sort 1 item
        List<AbstractItem> sorted1 = inventoryManagerOne.sortByName();
        List<AbstractItem> expected1 = new ArrayList<>();
        expected1.add(item1);
        assertEquals(expected1, sorted1);

        // sort 3 items
        List<AbstractItem> sorted3 = inventoryManagerThree.sortByName();
        List<AbstractItem> expected3 = new ArrayList<>();
        expected3.add(item1);
        expected3.add(item3);
        expected3.add(item2);
        assertEquals(expected3, sorted3);
    }

    @Test
    public void testSortByCode() {
        // sort 1 item
        List<AbstractItem> sorted1 = inventoryManagerOne.sortByCode();
        List<AbstractItem> expected1 = new ArrayList<>();
        expected1.add(item1);
        assertEquals(expected1, sorted1);

        // sort 3 items
        List<AbstractItem> sorted3 = inventoryManagerThree.sortByCode();
        List<AbstractItem> expected3 = new ArrayList<>();
        expected3.add(item3);
        expected3.add(item1);
        expected3.add(item2);
        assertEquals(expected3, sorted3);
    }

    @Test
    public void testSortByQuantity() {
        // sort 1 item
        List<AbstractItem> sorted1 = inventoryManagerOne.sortByQuantity();
        List<AbstractItem> expected1 = new ArrayList<>();
        expected1.add(item1);
        assertEquals(expected1, sorted1);

        // sort 3 items
        List<AbstractItem> sorted3 = inventoryManagerThree.sortByQuantity();
        List<AbstractItem> expected3 = new ArrayList<>();
        expected3.add(item3);
        expected3.add(item1);
        expected3.add(item2);
        assertEquals(expected3, sorted3);
    }

    @Test
    public void testSearchByName() throws IllegalAccessException {
        assertEquals(item1, inventoryManagerOne.searchByName("Apple"));

        assertEquals(item3, inventoryManagerThree.searchByName("Grape"));
        assertEquals(item1, inventoryManagerThree.searchByName("Apple"));
        assertEquals(item2, inventoryManagerThree.searchByName("Sword"));


    }

}
