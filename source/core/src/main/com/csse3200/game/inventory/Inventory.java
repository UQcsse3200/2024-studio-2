package com.csse3200.game.inventory;

import com.csse3200.game.gamestate.GameState;
import com.csse3200.game.gamestate.data.InventorySave;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.inventory.items.ItemUsageContext;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.TreeSet;
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
    // Maps item codes to sets of inventory indices where they are stored.
    private TreeMap<Integer, TreeSet<Integer>> codeToIndices;
    // Maps item names to sets of inventory indices where they are stored.
    private TreeMap<String, TreeSet<Integer>> nameToIndices;
    // Array representing the inventory, holding items or null values.
    private AbstractItem[] memoryView; // Array of actual items & null values
    /* Name of an item being searched for - used for quests */
    private String questItem;
    /* Name of an item being searched for - used for quests */
    private int questItemCount;
    /* The item collection task trigger. */
    private String collectionTrigger;

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
        this.codeToIndices = new TreeMap<>();
        this.nameToIndices = new TreeMap<>();
        this.memoryView = new AbstractItem[capacity];

    }

    /**
     * Loads and initialises the contents of the inventory from a save.
     * If one does not exist, creates one.
     *
     * @see InventorySave
     */
    public void loadInventoryFromSave() {
//        if(GameState.inventory == null) {
//            GameState.inventory = new InventorySave();
//        }
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
        codeToIndices = new TreeMap<>();
        nameToIndices = new TreeMap<>();

        for (int i = 0; i < capacity; i++) {
            if (memoryView[i] != null) {
                AbstractItem item = memoryView[i];
                // Add to code/name mapping to indices
                // Note we assume code and names have a 1-1 relationship
                if (!codeToIndices.containsKey(item.getItemCode())) {
                    codeToIndices.put(item.getItemCode(), new TreeSet<>());
                    nameToIndices.put(item.getName(), new TreeSet<>());
                }

                codeToIndices.get(item.getItemCode()).add(i);
                nameToIndices.get(item.getName()).add(i);
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
        return codeToIndices.containsKey(code);
    }

    /**
     * Checks if an item with the given name exists in the inventory.
     *
     * @param name the name of the item.
     * @return {@code true} if the item is present, {@code false} otherwise.
     */
    @Override
    public boolean hasItem(String name) {
        return nameToIndices.containsKey(name);
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
        if (memoryView[index] != null) {
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
        codeToIndices.clear();
        nameToIndices.clear();
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
     * @return {@code true} if the item was added successfully, else {@code false}
     */
    @Override
    public boolean add(AbstractItem item) {
        // Check if item is already present:
        if (codeToIndices.containsKey(item.getItemCode())) {
            // Iterate through map and if we can add any more items, add them
            for (Integer i : codeToIndices.get(item.getItemCode())) {
                AbstractItem x = memoryView[i];
                if (x.numAddable() >= 1) {
                    x.add(1);
                    return true;
                }
            }
        }

        // If there are no stacks this can be added to and no new slots.
        if (this.isFull()) {
            return false;
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
        if (memoryView[index] == null) {
            freeSlots--;
        }

        this.deleteItemAt(index); // delete the old item

        memoryView[index] = item; // add the new item
        this.addToMappings(item, index);
    }

    /**
     * Sorts the inventory by item code (in ascending order)
     */
    @Override
    public void sortByCode() {
        // Create new underlying memory:
        AbstractItem[] newInventory = new AbstractItem[capacity];
        TreeMap<String, TreeSet<Integer>> newNameMap = new TreeMap<>();

        // Iterate through items (sorted by code) and reindex in order:
        int i = 0; // Index counter
        for (Map.Entry<Integer, TreeSet<Integer>> entry : codeToIndices.entrySet()) {
            TreeSet<Integer> newIndices = new TreeSet<>();
            TreeSet<Integer> oldIndices = entry.getValue();
            newNameMap.put(memoryView[oldIndices.first()].getName(), new TreeSet<>());
            TreeSet<Integer> newNames = newNameMap.get(memoryView[oldIndices.first()].getName());

            // For each item, reindex and add to new underlying memory representation:
            for (Integer index : oldIndices) {
                newInventory[i] = memoryView[index];
                newIndices.add(i);
                newNames.add(i);
                i++;
            }

            // Replace indices with re-indexed set
            entry.setValue(newIndices);
        }

        // Update underlying memory
        nameToIndices = newNameMap;
        memoryView = newInventory;
    }

    /**
     * Sorts the inventory by item name.
     */
    @Override
    public void sortByName() {
        // Create new underlying memory:
        AbstractItem[] newInventory = new AbstractItem[capacity];
        TreeMap<Integer, TreeSet<Integer>> newCodeMap = new TreeMap<>();

        // Iterate through items (sorted by code) and reindex in order:
        int i = 0; // Index counter
        for (Map.Entry<String, TreeSet<Integer>> entry : nameToIndices.entrySet()) {
            TreeSet<Integer> newIndices = new TreeSet<>();
            TreeSet<Integer> oldIndices = entry.getValue();
            newCodeMap.put(memoryView[oldIndices.first()].getItemCode(), new TreeSet<>());
            TreeSet<Integer> newCodes = newCodeMap.get(memoryView[oldIndices.first()].getItemCode());

            // For each item, reindex and add to new underlying memory representation:
            for (Integer index : oldIndices) {
                newInventory[i] = memoryView[index];
                newIndices.add(i);
                newCodes.add(i);
                i++;
            }

            // Replace indices with re-indexed set
            entry.setValue(newIndices);
        }

        // Update underlying memory
        codeToIndices = newCodeMap;
        memoryView = newInventory;
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
        this.addToMappings(item, nextIndex);
        freeSlots--;

        if (this.isFull()) {
            nextIndex = this.capacity; // Cannot add any new items currently so invalidate the index
            return;
        }

        // Update the next available index - note we expect there to be a free slot at this point
        while (memoryView[nextIndex] != null) {nextIndex++;}
    }

    /**
     * Adds an item and index to the internal mapping (item code/names to index(s)) structure.
     *
     * @param item the item to add
     * @param index the index where the item is stored.
     */
    private void addToMappings(AbstractItem item, int index) {
        if (!codeToIndices.containsKey(item.getItemCode())) {
            codeToIndices.put(item.getItemCode(), new TreeSet<>());
        }
        codeToIndices.get(item.getItemCode()).add(index);

        if (!nameToIndices.containsKey(item.getName())) {
            nameToIndices.put(item.getName(), new TreeSet<>());
        }
        nameToIndices.get(item.getName()).add(index);
    }

    /**
     * Retrieves the index of the first occurrence of an item with the specified code.
     *
     * @param code the code of the item to search for.
     * @return an {@link Optional} containing the index of the item if found,
     *         or {@link Optional#empty()} if the item is not in the inventory.
     */
    private Optional<Integer> getItemIndex(int code) {
        return this.hasItem(code) ? Optional.of(codeToIndices.get(code).first()) : Optional.empty();
    }

    /**
     * Retrieves the index of the first occurrence of an item with the specified name.
     *
     * @param name the name of the item to search for.
     * @return an {@link Optional} containing the index of the item if found,
     *         or {@link Optional#empty()} if the item is not in the inventory.
     */
    private Optional<Integer> getItemIndex(String name) {
        return this.hasItem(name) ? Optional.of(nameToIndices.get(name).first()) : Optional.empty();
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
        AbstractItem item = memoryView[index];
        codeToIndices.get(item.getItemCode()).remove(index);
        nameToIndices.get(item.getName()).remove(index);
        freeSlots++;

        memoryView[index] = null;

        if (codeToIndices.get(item.getItemCode()).isEmpty()) { // Remove item from mapping if no instances
            // remain.
            codeToIndices.remove(item.getItemCode());
        }

        if (nameToIndices.get(item.getName()).isEmpty()) { // Remove item from mapping if no
            // instances
            // remain.
            nameToIndices.remove(item.getName());
        }

        if (index < nextIndex) { // Update the next available index if necessary.
            nextIndex = index;
        }
    }

    // Quests additions - starting here

    /** Inventory listen for quests item collection tasks
     * @param itemName - the item name that will be listened for
     * @param quantity - the number of item occurrences required for
     *                 the quest to be completed. */
    public void questItemListen(String itemName, int quantity, String collectionTrigger){
        this.questItem = itemName;
        this.questItemCount = quantity;
        this.collectionTrigger = collectionTrigger;
    }

    /** @return trigger string upon completion of item collection task. */
    public String getCollectionTrigger() {
        return collectionTrigger;
    }

    /** Quests item collection completion - used to send out "items collected"
     * @return True if specified numbers of item has been collected. */
    public boolean itemCollectionSuccessful() {
        int count = 0;
        for (int item = 0; item < this.nextIndex; item++) {
            if (memoryView[item].getName().equals(this.questItem)) {
                count += memoryView[item].getQuantity();
            }
        }
        if (count > 0) {
            return count >= this.questItemCount;
        }
        return false;
    }
}