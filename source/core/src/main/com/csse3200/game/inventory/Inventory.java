package com.csse3200.game.inventory;

import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.inventory.items.ItemUsageContext;
import com.badlogic.gdx.utils.IntMap;
import java.util.TreeSet;
import static java.util.Arrays.fill;

// TODO List (for version 1 and future implementations):
// TODO: Clean up all javadocs
// TODO: Write Wiki page
// TODO: Refactor and add helper functions to reduce code duplication
// TODO: Check how memory is affected by constructing items before/inside for loops
// TODO: Add support for adding multiple of an item, or adding until full
// TODO: Add support for indexing, searching and sorting alphabetically by name
// TODO: Error checking here and in abstract item needs to become significantly more rigorous
// TODO: Naming and spell checking also needs to be run here and on abstract item.
// TODO: Add inventory view class which adds support for rendering inventory


/**
 * The Inventory class manages a collection of items, allowing for storage, retrieval, and
 * manipulation. This class supports adding and removing items, checking inventory status, and
 * using items within the inventory. It is intended as a player inventory - to store items
 * players retrieve from the game.
 */
public class Inventory implements InventoryInterface {
    private final int capacity; // The maximum number of items the inventory can hold.
    private int freeSlots; // The current number of available slots in the inventory.
    private int nextIndex = 0; // The index where the next item can be stored.
    // Maps item codes to sets of inventory indices where they are stored.
    private final IntMap<TreeSet<Integer>> mapping;
    // Array representing the inventory, holding items or null values.
    private final AbstractItem[] inventory; // Array of actual items & null values

    /**
     * Constructs an Inventory with a specified capacity.
     *
     * @param capacity the maximum number of items the inventory can hold.
     * @throws IllegalArgumentException if the capacity is not a positive integer.
     */
    public Inventory(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Inventory must have positive capacity!");
        }

        this.capacity = capacity;
        this.freeSlots = capacity;
        this.mapping = new IntMap<>();
        this.inventory = new AbstractItem[capacity];
    }

    /**
     * @return the total capacity of the inventory.
     */
    @Override
    public int getCapacity() {
        return capacity;
    }

    /**
     * @return the number of free slots available in the inventory.
     */
    @Override
    public int numFreeSlots() {
        return freeSlots;
    }

    /**
     * @return {@code true} if the inventory is full, {@code false} otherwise.
     */
    @Override
    public boolean isFull() {
        return freeSlots == 0;
    }

    /**
     * Checks if an item with the given code exists in the inventory.
     *
     * @param code the unique code of the item.
     * @return {@code true} if the item is present, {@code false} otherwise.
     */
    @Override
    public boolean hasItem(int code) {
        return mapping.containsKey(code);
    }

    /**
     * Retrieves the index of the first occurrence of an item with the given code.
     *
     * @param code the unique code of the item.
     * @return the index of the item, or -1 if the item is not found.
     */
    @Override
    public int getIndex(int code) {
        if (this.hasItem(code)) {
            return mapping.get(code).first();
        }
        return -1;
    }

    /**
     * Retrieves an item from the inventory at the given index.
     *
     * @param index the index at which to retrieve the item.
     * @return the item at the index, or {@code null} if none found.
     */
    @Override
    public AbstractItem getAt(int index) {
        return inventory[index];
    }

    /**
     * Deletes the first occurrence of an item with the specified code from the inventory.
     * <p>Currently removes the item entirely. Needs to be updated to reduce quantity if
     * applicable - ie if we only want to delete <b>n</b> of the item.</p>
     *
     * @param code the unique code of the item.
     */
    @Override
    public void deleteItem(int code) {
        if (this.hasItem(code)) {
            this.deleteItemAt(mapping.get(code).first());
        }
    }

    /**
     * Deletes the item at the specified index from the inventory.
     * <p>It is currently up to the user to ensure they have not exceeded the bounds of the
     * inventory.</p>
     * <p>If the item is removed, updates the internal mappings and adjusts the next available
     * index.</p>
     *
     * @param index the index of the item to delete.
     */
    // Up to user to check they have not exceeded the bounds of the inventory
    @Override
    public void deleteItemAt(int index) {
        if (inventory[index] != null) {
            int code = inventory[index].getItemCode();
            inventory[index] = null;
            mapping.get(code).remove(index);
            freeSlots++;

            if (mapping.get(code).isEmpty()) { // Remove item if no stacks remaining
                mapping.remove(code);
            }

            if (index < nextIndex) { // Update nextIndex
                nextIndex = index;
            }
        }
    }

    /**
     * Clears all items from the inventory.
     * <p>Warning: All items will be deleted and cannot be recovered.</p>
     */
    @Override
    public void clearInventory() {
        freeSlots = capacity;
        nextIndex = 0;
        mapping.clear();
        fill(inventory, null);
    }

    /**
     * Uses the first occurrence of an item with the specified code in the inventory.
     * <p>If the item is consumable and becomes empty after use, it is removed from the
     * inventory.</p>
     *
     * @param code the unique code of the item.
     * @param context the context in which the item is used (see {@link ItemUsageContext}.
     */
    @Override
    public void useItem(int code, ItemUsageContext context) {
        if (this.hasItem(code)) {
            int index = mapping.get(code).first();
            inventory[index].useItem(context);
            if (inventory[index].isEmpty()) {
                this.deleteItemAt(index);
            }
        }
    }

    /**
     * Uses the item at the specified index in the inventory.
     * <p>If the item is consumable and becomes empty after use, it is removed from the
     * inventory.</p>
     *
     * @param index the index of the item to use.
     * @param context the context in which the item is used (see {@link ItemUsageContext}).
     */
    @Override
    public void useItemAt(int index, ItemUsageContext context) {
        if (inventory[index] != null) {
            inventory[index].useItem(context);
            if (inventory[index].isEmpty()) {
                this.deleteItemAt(index);
            }
        }
    }

    /**
     * Adds an item to the inventory, either by stacking it with existing items or placing it in
     * a new slot.
     *
     * @param item the item to add to the inventory.
     */
    @Override
    public void add(AbstractItem item) {
        if (this.isFull()) {return;}

        // Check if item is already present:
        if (mapping.containsKey(item.getItemCode())) {
            // Iterate through map and if we can add any more items add them, otherwise addNewItem
            for (Integer i : mapping.get(item.getItemCode())) {
                AbstractItem x = inventory[i];
                if (x.numAddabble() >= 1) {
                    x.add(1);
                    return;
                }
            }
        }
        this.addNewItem(item);
    }

    /**
     * Adds an item at the specified index in the inventory, replacing any existing item at that
     * index.
     * <p>Note - it is currently up to the user to determine the given index is valid!</p>
     *
     * @param index the index at which to add the item.
     * @param item the item to add to the inventory.
     */
    @Override
    public void addAt(int index, AbstractItem item) {
        if (inventory[index] == null) {
            freeSlots--;
        }

        this.deleteItemAt(index); // delete old item

        inventory[index] = item; // add new item
        this.addToMapping(item.getItemCode(), index);
    }

    /**
     * Adds an item to a new slot in the inventory.
     * <p>Precondition: the inventory must <b>not</b> be full.</p>
     *
     * @param item the item to add to a new slot.
     */
    private void addNewItem(AbstractItem item) {
        inventory[nextIndex] = item;
        this.addToMapping(item.getItemCode(), nextIndex);
        freeSlots--;

        if (this.isFull()) {
            nextIndex = this.capacity; // Cannot add any new items currently so invalid index
            return;
        }

        // Update nextIndex
        while (inventory[nextIndex] != null) {nextIndex++;}
    }

    /**
     * Adds an item code and index to the internal mapping (item code to index(s)) structure.
     *
     * @param itemCode the unique code of the item.
     * @param index the index where the item is stored.
     */
    private void addToMapping(int itemCode, int index) {
        if (!mapping.containsKey(itemCode)) {
            mapping.put(itemCode, new TreeSet<>());
        }
        mapping.get(itemCode).add(index);
    }
}