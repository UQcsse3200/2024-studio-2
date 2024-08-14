package com.csse3200.game.inventory;

import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.inventory.items.ItemUsageContext;

import java.util.Optional;

// TODO : Add javadocs for interface!
/**
 * Represents player's inventory. Can store a certain number of distinct items (determined by
 * limit), some items can be stacked (ie, stackable items).
 *
 * <p><b>Note:</b> this interface in future can possibly be abstracted further and used as a </p>
 */
public interface InventoryInterface {
    int getCapacity(); // How many distinct item slots the inventory has
    int numFreeSlots(); // Returns how many slots are remaining in inventory
    boolean isFull();
    boolean hasItem(int itemCode); // check if inventory has item by code
    int getIndex(int itemCode); // get an index for an item by code
    Optional<AbstractItem> getAt(int index); // Get the item at the specified index
    void deleteItem(int itemCode); // delete any of a particular item by code
    void deleteItemAt(int index); // delete item by inventory index
    void clearInventory(); // Warning - clears entire inventory!
    void useItem(int itemCode, ItemUsageContext context); // use any of a particular item by code
    void useItemAt(int index, ItemUsageContext context); // use item by inventory index
    void add(AbstractItem item);
    void addAt(int index, AbstractItem item);
}