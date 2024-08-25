package com.csse3200.game.inventory;

import com.badlogic.gdx.utils.IntMap;
import com.csse3200.game.inventory.items.AbstractItem;

import java.util.*;

//TODO implement testing for categorise - need to pull item classes

/**
 * Class for managing inventory actions. Sorts inventory items by name, item code or quantity.
 * Categorises items by class. Allows for searching through inventory for item name
 */
public class InventoryManager extends Inventory {
    // maps item quantity to inventory indices where they are stored
    private final IntMap<TreeSet<Integer>> itemsByQuantity;
    // maps item categories to indices where they are stored
    private final Map<String, TreeSet<Integer>> categorisedItems;
    private List<AbstractItem> sortedQuantity;


    /**
     * Constructs an inventory manager class for the given inventory
     * @param capacity maximum number of items in inventory
     */
    public InventoryManager(int capacity) {
        super(capacity);
        this.itemsByQuantity = new IntMap<>();
        this.categorisedItems = new HashMap<>();
    }

    public void addToQuantityMap() {
        this.sortedQuantity = this.sortByQuantity();
        for (AbstractItem item : this.sortedQuantity) {
            int index = this.getIndex(item.getItemCode());
            if (!itemsByQuantity.containsKey(item.getQuantity())) {
                itemsByQuantity.put(item.getQuantity(), new TreeSet<>());
            }
            itemsByQuantity.get(item.getQuantity()).add(index);
        }
    }

    /**
     * Sorts items in inventory by quantity
     * @return returns items sorted by quantity
     */
    public List<AbstractItem> sortByQuantity() {
        List<AbstractItem> items = this.getItems();
        items.sort(
                Comparator.comparing(AbstractItem::getQuantity)
        );
        return items;
    }

    /**
     * Searches through the inventory for the given item and returns it. If not in inventory,
     * throws an exception - this will be handled later (display to user)
     * @param name name of item to search for
     * @return item
     * @throws IllegalAccessException if name not in inventory
     */
    public AbstractItem searchByName(String name) throws IllegalAccessException {
        List<AbstractItem> items = this.getItems();
        for (AbstractItem item : items) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        throw new IllegalAccessException();
    }

    /**
     * Categorises items in inventory.
     */
    public void categorise() {
        List<AbstractItem> items = this.getItems();
//        Map<String, List<AbstractItem>> categorisedItems = new HashMap<>();

        for (AbstractItem item : items) {
            String category = item.getClass().getSuperclass().getSimpleName();
            int index = this.getIndex(item.getItemCode());
            if (!categorisedItems.containsKey(category)) {
                categorisedItems.put(category, new TreeSet<>());
            }
            categorisedItems.get(category).add(index);
        }
    }

    /**
     * Gets all items indices in given category
     * @param category the category of items
     * @return item indices in given category
     */
    public TreeSet<Integer> getByCategory(String category) {
        return categorisedItems.get(category);
    }

    /**
     * Retrieves items in inventory. Called in each method due to inventory updating
     * @return list of items stored in inventory
     */
    private List<AbstractItem> getItems() {
        List<AbstractItem> items = new ArrayList<>();
        for (int i = 0; i < this.getCapacity(); i++) {
            AbstractItem item = this.getAt(i);
            if (item != null) {
                items.add(item);
            }
        }
        return items;
    }
}
