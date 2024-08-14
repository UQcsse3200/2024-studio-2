package com.csse3200.game.inventory;

import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.inventory.items.ItemUsageContext;

import java.util.Optional;

/**
 * Represents player's inventory. Can store a certain number of distinct items (determined by
 * limit), some items can be stacked (ie, stackable items).
 *
 * <p><b>Note:</b> this interface in future can possibly be abstracted further and used as a
 * container in multiple contexts, e.g. loot boxes or chests in game etc.</p>
 */
public interface InventoryInterface {
    /**
     * Retrieves the total capacity of the inventory.
     *
     * @return the number of distinct item slots available in the inventory
     */
    int getCapacity();

    /**
     * Returns the number of free slots available in the inventory.
     *
     * @return the number of available slots in the inventory
     */
    int numFreeSlots();

    /**
     * Checks if the inventory is full.
     *
     * @return {@code true} if the inventory has no free slots, {@code false} otherwise
     */
    boolean isFull();

    /**
     * Checks if the inventory contains an item with the specified code.
     *
     * @param itemCode the code of the item to check for
     * @return {@code true} if the inventory contains the item, {@code false} otherwise
     */
    boolean hasItem(int itemCode);

    /**
     * Retrieves the index of an item with the specified code.
     *
     * @param itemCode the code of the item to retrieve the index for
     * @return the index of the item in the inventory, or -1 if the item is not found
     */
    int getIndex(int itemCode);

    /**
     * Retrieves the item at the specified index in the inventory.
     *
     * @param index the index of the item to retrieve
     * @return an {@code Optional} containing the item if present, or {@code Optional.empty()} if no item is found
     */
    Optional<AbstractItem> getAt(int index);

    /**
     * Deletes an instance of an item with the specified code from the inventory.
     *
     * @param itemCode the code of the item to delete
     */
    void deleteItem(int itemCode);

    /**
     * Deletes the item at the specified index in the inventory.
     *
     * @param index the index of the item to delete
     */
    void deleteItemAt(int index);

    /**
     * Clears all items from the inventory.
     * <p><b>Warning:</b> This action is irreversible and will remove all items from the
     * inventory.</p>
     */
    void clearInventory();

    /**
     * Uses an item with the specified code from the inventory.
     *
     * @param itemCode the code of the item to use
     * @param context the context in which the item is used (see {@link ItemUsageContext})
     */
    void useItem(int itemCode, ItemUsageContext context);

    /**
     * Uses the item at the specified index in the inventory.
     *
     * @param index the index of the item to use
     * @param context the context in which the item is used (see {@link ItemUsageContext})
     */
    void useItemAt(int index, ItemUsageContext context);

    /**
     * Adds an item to the inventory. If the item can be stacked, it may be added to an existing
     * stack.
     *
     * @param item the item to add to the inventory
     */
    void add(AbstractItem item);

    /**
     * Adds an item to a specific index in the inventory, replacing any existing item at that index.
     *
     * @param index the index at which to add the item
     * @param item the item to add to the inventory
     */
    void addAt(int index, AbstractItem item);
}