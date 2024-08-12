package com.csse3200.game.inventory;

import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.inventory.items.ItemUsageContext;
import com.badlogic.gdx.utils.IntMap;
import java.util.TreeSet;

// TODO: Testing, Documentation (JavaDocs & Wiki), Modify Add Item to Change quantity if
//  Consumable add private method for addNew item vs adding to existing stack. Check out how to
//  add to the map with default adds to make sure to generate new sets when needed. Make sure to
//  remove old sets after final deletion.

// TODO: Create custom pollFirst that doesn't show warnings

// TODO: Check how memory is affected by constructing items before/inside for loops

// TODO: Add support for adding multiple of an item, or adding until full


public class Inventory implements InventoryInterface {
    private final int capacity; // Remove tag final if inventory becomes upgradeable
    private int freeSlots;
    private int nextIndex = 0; // Maintain the index where the next item can be stored
    private final IntMap<TreeSet<Integer>> mapping; // ItemCode : Set of Index's
    private final AbstractItem[] inventory; // Array of actual items & null values

    public Inventory(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Inventory must have positive capacity!");
        }

        this.capacity = capacity;
        this.freeSlots = capacity;
        this.mapping = new IntMap<>();
        this.inventory = new AbstractItem[capacity];
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public int numFreeSlots() {
        return freeSlots;
    }

    @Override
    public boolean isFull() {
        return freeSlots == 0;
    }

    @Override
    public boolean hasItem(int code) {
        return mapping.containsKey(code);
    }

    // Returns -1 if item is not in inventory
    @Override
    public int getIndex(int code) {
        if (this.hasItem(code)) {
            return mapping.get(code).first();
        }
        return -1;
    }

    // Currently removes item entirely - needs to be updated to reduce quantity.
    @Override
    public void deleteItem(int code) {
        if (this.hasItem(code)) {
            int index = mapping.get(code).first();
            mapping.get(code).remove(index);
            inventory[index] = null;
            freeSlots++;

            if (index < nextIndex) { // Update nextIndex
                nextIndex = index;
            }
        }
    }

    // Up to user to check they have not exceeded the bounds of the inventory
    @Override
    public void deleteItemAt(int index) {
        if (inventory[index] != null) {
            int code = inventory[index].getItemCode();
            inventory[index] = null;
            mapping.get(code).remove(index);
            freeSlots++;

            if (index < nextIndex) { // Update nextIndex
                nextIndex = index;
            }
        }
    }

    // Need to update the following to remove an item if consumable!
    @Override
    public void useItem(int code, ItemUsageContext context) {
        if (this.hasItem(code)) {
            int index = mapping.get(code).first();
            inventory[index].useItem(context);
            if (inventory[index].isConsumed()) {
                this.deleteItemAt(index);
            }
        }
    }

    // Need to update the following to remove an item if consumable!
    @Override
    public void useItemAt(int index, ItemUsageContext context) {
        if (inventory[index] != null) {
            inventory[index].useItem(context);
            if (inventory[index].isConsumed()) {
                this.deleteItemAt(index);
            }
        }
    }

    @Override
    public void addItem(AbstractItem item) {
        if (this.isFull()) {return;}

        // Check if item is already present:
        if (mapping.containsKey(item.getItemCode())) {
            // Iterate through map and if we can add any more items add them, otherwise addNewItem
            for (Integer i : mapping.get(item.getItemCode())) {
                AbstractItem x = inventory[i];
                if (x.canAdd(1)) {
                    x.add(1);
                    return;
                }
            }
        }
        this.addNewItem(item);
    }

    // This acts as a `replace item` as well for now
    // Up to user to determine that the index is valid
    @Override
    public void addItem(AbstractItem item, int index) {
        if (inventory[index] == null) {
            freeSlots--;
        }
        inventory[index] = item;
        this.addToMapping(item.getItemCode(), index);
    }

    private void addNewItem(AbstractItem item) {
        inventory[nextIndex] = item;
        this.addToMapping(item.getItemCode(), nextIndex);
        freeSlots--;

        // Update nextIndex
        while (inventory[nextIndex] != null) {nextIndex++;}
    }

    private void addToMapping(int itemCode, int index) {
        if (!mapping.containsKey(itemCode)) {
            mapping.put(itemCode, new TreeSet<>());
        }
        mapping.get(itemCode).add(index);
    }
}