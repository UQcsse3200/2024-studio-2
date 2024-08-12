package com.csse3200.game.inventory;

import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.inventory.items.ItemUsageContext;

/**
 * Represents player's inventory. Can store a certain number of distinct items (determined by
 * limit), some items can be stacked (ie, stackable items).
 */
public interface InventoryInterface {
    int getCapacity(); // How many distinct item slots the inventory has
    int numFreeSlots(); // Returns how many slots are remaining in inventory
    boolean isFull();
    boolean hasItem(int itemCode); // check if inventory has item by code
    int getIndex(int itemCode); // get an index for an item by code
    void deleteItem(int itemCode); // delete any of a particular item by code
    void deleteItemAt(int index); // delete item by inventory index
    void useItem(int itemCode, ItemUsageContext context); // use any of a particular item by code
    void useItemAt(int index, ItemUsageContext context); // use item by inventory index
    void addItem(AbstractItem item);
    void addItem(AbstractItem item, int index);
}