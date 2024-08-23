package com.csse3200.game.inventory;

import com.csse3200.game.inventory.items.AbstractItem;

import java.util.*;

//TODO implement testing for categorise - need to pull item classes

/**
 * Class for managing inventory actions. Sorts inventory items by name, item code or quantity.
 * Categorises items by class. Allows for searching through inventory for item name
 */
public class InventoryManager {
    private final Inventory inventory;  // inventory storing items

    /**
     * Consutructs an inventory manager class for the given inventory
     * @param inventory inventory holding items
     */
    public InventoryManager(Inventory inventory) {
        this.inventory = inventory;
    }

    /**
     * Sorts items in inventory by name
     * @return list of sorted items
     */
    public List<AbstractItem> sortByName() {
        List<AbstractItem> items = this.getItems();
        items.sort(
                Comparator.comparing(AbstractItem::getName)
        );
        return items;
    }

    /**
     * Sorts items in inventory by item code
     * @return returns items sorted by code
     */
    public List<AbstractItem> sortByCode() {
        List<AbstractItem> items = this.getItems();
        items.sort(
                Comparator.comparing(AbstractItem::getItemCode)
        );
        return items;
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
     * @return map containing the category as the key, and list of items in that category as value
     */
    public Map<String, List<AbstractItem>> categorise() {
        List<AbstractItem> items = this.getItems();
        Map<String, List<AbstractItem>> categorisedItems = new HashMap<>();

        for (AbstractItem item : items) {
            String category = item.getClass().getSuperclass().getSimpleName();
            if (categorisedItems.containsKey(category)) {
                categorisedItems.get(category).add(item);
            } else {
                List<AbstractItem> categoryItem = new ArrayList<>();
                categoryItem.add(item);
                categorisedItems.put(category, categoryItem);
            }
        }
        return categorisedItems;
    }

    /**
     * Gets all items in given category
     * @param category the category of items
     * @return items in given category
     */
    public List<AbstractItem> getByCategory(String category) {
        Map<String, List<AbstractItem>> categorisedItems = this.categorise();
        return categorisedItems.get(category);
    }


    /**
     * Retrieves items in inventory. Called in each method due to inventory updating
     * @return list of items stored in inventory
     */
    private List<AbstractItem> getItems() {
        List<AbstractItem> items = new ArrayList<>();
        for (int i = 0; i < inventory.getCapacity(); i++) {
            AbstractItem item = inventory.getAt(i);
            if (item != null) {
                items.add(item);
            }
        }
        return items;
    }


}
