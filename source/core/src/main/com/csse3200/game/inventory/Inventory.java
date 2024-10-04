package com.csse3200.game.inventory;

import com.csse3200.game.gamestate.GameState;
import com.csse3200.game.gamestate.data.InventorySave;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.inventory.items.ItemUsageContext;

import java.util.*;

import static java.util.Arrays.fill;

/**
 * The Inventory class manages a collection of items, allowing for storage, retrieval, and
 * manipulation. This class supports adding and removing items, checking inventory status, and
 * using items within the inventory. It is intended as a player inventory - to store items
 * players retrieve from the game.
 */
public class Inventory implements InventoryInterface {
    private int capacity; // The maximum number of items the inventory can hold.
    private int freeSlots; // The current number of available slots in the inventory.
    private int nextIndex = 0; // The index where the next item can be stored.
    // Array representing the inventory, holding items or null values.
    private AbstractItem[] memoryView; // Array of actual items & null values

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
        this.memoryView = new AbstractItem[capacity];

    }

    /**
     * Loads and initialises the contents of the inventory from a save.
     * If one does not exist, creates one.
     *
     * @see InventorySave
     */
    public void loadInventoryFromSave() {
        if(GameState.inventory.inventoryContent.length != 0) {
            reconstructFromArray(GameState.inventory.inventoryContent);
        } else {
            GameState.inventory.inventoryContent = this.memoryView;
        }
    }

    private void reconstructFromArray(AbstractItem[] newView) {
        this.capacity = newView.length;
        this.memoryView = newView;
        this.freeSlots = capacity;
        this.nextIndex = capacity; // Initialise to invalid index

        for (int i = 0; i < capacity; i++) {
            if (memoryView[i] != null) {
                freeSlots--;
            } else {
                // Update next index if not already set
                this.nextIndex = nextIndex == capacity ? i : nextIndex;
            }
        }
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
        return Arrays.stream(memoryView).anyMatch(item -> item != null && item.getItemCode() == code);
    }


    /**
     * Checks if an item with the given name exists in the inventory.
     *
     * @param name the name of the item.
     * @return {@code true} if the item is present, {@code false} otherwise.
     */
    @Override
    public boolean hasItem(String name) {
        return Arrays.stream(memoryView).anyMatch(item -> item != null && item.getName().equals(name));
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
     * Retrieves the index of the first occurrence of an item with the given name.
     *
     * @param name the name of the item.
     * @return the index of the item, or -1 if the item is not found.
     */
    @Override
    public int getIndex(String name) {
        return this.getItemIndex(name).orElse(-1);
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
        return memoryView[index];
    }

    /**
     * Swaps the items between two inventory slots. If the target slot is empty,
     * the item from the source slot is moved to the target slot. If the target slot
     * contains an item, the items between the source and target slots are swapped.
     *
     * @param src    The index of the source slot from which the item is being moved.
     * @param target The index of the target slot to which the item is being moved.
     */
    public void swap(int src, int target) {
        AbstractItem from = deleteItemAt(src);
        AbstractItem to = deleteItemAt(target);
        if (from != null) {addAt(target, from);}
        if (to != null) {addAt(src, to);}
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
     * @return the item that was removed (or null if there was no item at that index)
     */
    @Override
    public AbstractItem deleteItemAt(int index) {
        AbstractItem item = memoryView[index];
        if (item != null) {
            this.removeAt(index);
        }
        return item;
    }

    /**
     * Clears all items from the inventory.
     * <p>Warning: All items will be deleted and cannot be recovered.</p>
     */
    @Override
    public void clearInventory() {
        freeSlots = capacity;
        nextIndex = 0;
        fill(memoryView, null);
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
        if (memoryView[index] != null) {
            this.useAndPossiblyRemove(index, context);
        }
    }

    /**
     * Adds an item to the inventory, either by stacking it with existing items or placing it in
     * a new slot.
     *
     * @param item the item to add to the inventory.
     * @return the index the item was added at (-1 if not added)
     */
    @Override
    public int add(AbstractItem item) {
       int i = 0;
        for (AbstractItem x : memoryView) {
            if (x != null && x.getItemCode() == item.getItemCode() && x.numAddable() > 0) {
                x.add(1);
                return i;
            }
            i++;
        }

        // If there are no stacks this can be added to and no new slots.
        if (this.isFull()) {
            return -1;
        }

        // Cannot add item to existing instances of item, so add to a new slot
        i = nextIndex;
        this.addNewItem(item);
        return i;
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
        if (memoryView[index] == null) {
            freeSlots--;
        }

        this.deleteItemAt(index); // delete the old item

        memoryView[index] = item; // add the new item
    }

    /**
     * Sorts the inventory by item code (in ascending order)
     */
    @Override
    public void sortByCode() {
        Arrays.sort(memoryView, Comparator.nullsLast(Comparator.comparing(AbstractItem::getItemCode)));
    }

    // PRIVATE HELPER FUNCTIONS:
    /**
     * Adds an item to a new slot in the inventory.
     * <p>Precondition: the inventory must <b>not</b> be full.</p>
     *
     * @param item the item to add to a new slot.
     */
    private void addNewItem(AbstractItem item) {
        memoryView[nextIndex] = item;
        freeSlots--;

        if (this.isFull()) {
            nextIndex = this.capacity; // Cannot add any new items currently so invalidate the index
            return;
        }

        // Update the next available index - note we expect there to be a free slot at this point
        while (memoryView[nextIndex] != null) {nextIndex++;}
    }

    /**
     * Retrieves the index of the first occurrence of an item with the specified code.
     *
     * @param code the code of the item to search for.
     * @return an {@link Optional} containing the index of the item if found,
     *         or {@link Optional#empty()} if the item is not in the inventory.
     */
    private Optional<Integer> getItemIndex(int code) {
        int i = 0;
        for (AbstractItem item : memoryView) {
            if (item != null && item.getItemCode() == code) {
                return Optional.of(i);
            }
            i++;
        }
        return Optional.empty();
    }

    /**
     * Retrieves the index of the first occurrence of an item with the specified name.
     *
     * @param name the name of the item to search for.
     * @return an {@link Optional} containing the index of the item if found,
     *         or {@link Optional#empty()} if the item is not in the inventory.
     */
    private Optional<Integer> getItemIndex(String name) {
        int i = 0;
        for (AbstractItem item : memoryView) {
            if (item.getName().equals(name)) {
                return Optional.of(i);
            }
            i++;
        }
        return Optional.empty();
    }

    /**
     * Uses an item at the specified index and removes it from the inventory if it is empty after use.
     *
     * @param index the index of the item to use.
     * @param context the context in which the item is being used.
     */
    private void useAndPossiblyRemove(int index, ItemUsageContext context) {
        memoryView[index].useItem(context);
        if (memoryView[index].isEmpty()) {
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
        freeSlots++;
        memoryView[index] = null;

        if (index < nextIndex) { // Update the next available index if necessary.
            nextIndex = index;
        }
    }
}