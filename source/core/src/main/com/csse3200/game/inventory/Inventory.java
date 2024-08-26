package com.csse3200.game.inventory;

import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.inventory.items.ItemUsageContext;
import com.badlogic.gdx.utils.IntMap;

import java.util.Optional;
import java.util.TreeSet;
import static java.util.Arrays.fill;

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
        return this.getItemIndex(code).orElse(-1);
    }

    /**
     * Retrieves the item at the specified index in the inventory.
     *
     * @param index the index of the item in the inventory
     * @return the item at the specified index, or {@code null} if there is no item at that index
     * @throws ArrayIndexOutOfBoundsException if the index is out of range, i.e,
     *         {@code (index < 0 || index >= capacity)}
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
        this.getItemIndex(code).ifPresent(this::deleteItemAt);
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
    @Override
    public void deleteItemAt(int index) {
        if (inventory[index] != null) {
            this.removeAt(index);
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
     * @param context the context in which the item is used (see {@link ItemUsageContext}).
     */
    @Override
    public void useItem(int code, ItemUsageContext context) {
        this.getItemIndex(code).ifPresent(index -> useAndPossiblyRemove(index, context));
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
            this.useAndPossiblyRemove(index, context);
        }
    }

    /**
     * Adds an item to the inventory, either by stacking it with existing items or placing it in
     * a new slot.
     *
     * @param item the item to add to the inventory.
     * @return {@code true} if the item was added successfully, else {@code false}
     */
    @Override
    public boolean add(AbstractItem item) {
        if (this.isFull()) {
            return false;
        }

        // Check if item is already present:
        if (mapping.containsKey(item.getItemCode())) {
            // Iterate through map and if we can add any more items, add them
            for (Integer i : mapping.get(item.getItemCode())) {
                AbstractItem x = inventory[i];
                if (x.numAddable() >= 1) {
                    x.add(1);
                    return true;
                }
            }
        }

        // Cannot add item to existing instances of item, so add to a new slot
        this.addNewItem(item);
        return true;
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

        this.deleteItemAt(index); // delete the old item

        inventory[index] = item; // add the new item
        this.addToMapping(item.getItemCode(), index);
    }

    // PRIVATE HELPER FUNCTIONS:
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
            nextIndex = this.capacity; // Cannot add any new items currently so invalidate the index
            return;
        }

        // Update the next available index - note we expect there to be a free slot at this point
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

    /**
     * Retrieves the index of the first occurrence of an item with the specified code.
     *
     * @param code the code of the item to search for.
     * @return an {@link Optional} containing the index of the item if found,
     *         or {@link Optional#empty()} if the item is not in the inventory.
     */
    private Optional<Integer> getItemIndex(int code) {
        return this.hasItem(code) ? Optional.of(mapping.get(code).first()) : Optional.empty();
    }

    /**
     * Uses an item at the specified index and removes it from the inventory if it is empty after use.
     *
     * @param index the index of the item to use.
     * @param context the context in which the item is being used.
     */
    private void useAndPossiblyRemove(int index, ItemUsageContext context) {
        inventory[index].useItem(context);
        if (inventory[index].isEmpty()) {
            this.removeAt(index);
        }
    }

    /**
     * Removes the item at the specified index from the inventory, updates the mapping,
     * and adjusts the inventory state accordingly.
     *
     * @param index the index of the item to remove.
     */
    private void removeAt(int index) {
        int code = inventory[index].getItemCode();
        inventory[index] = null;
        mapping.get(code).remove(index);
        freeSlots++;

        if (mapping.get(code).isEmpty()) { // Remove item from mapping if no instances remain.
            mapping.remove(code);
        }

        if (index < nextIndex) { // Update the next available index if necessary.
            nextIndex = index;
        }
    }
}