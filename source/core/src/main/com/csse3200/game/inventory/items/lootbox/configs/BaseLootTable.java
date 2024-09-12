package com.csse3200.game.inventory.items.lootbox.configs;

import com.csse3200.game.inventory.items.AbstractItem;

import java.util.ArrayList;
import java.util.List;

/**
 * BaseLootTable is an abstract class representing a basic loot table used to manage and generate loot items.
 * It provides functionality to add items with specified weights, retrieve single or multiple random items,
 * and calculate their probabilities based on the assigned weights.
 */
public abstract class  BaseLootTable {
    // List to hold loot items and their weights
    protected List<LootItemConfig> items;

    /**
     * Constructs a BaseLootTable and initializes the list of items.
     * This constructor sets up the internal structure needed to manage loot items.
     */
    protected BaseLootTable() {
        this.items = new ArrayList<>();
    }

    /**
     * Adds a loot item to the loot table with the specified weight and constructor parameters.
     * The weight determines the probability of the item being selected when generating loot.
     *
     * @param item           The class type of the loot item (must extend AbstractItem).
     * @param weight         The weight of the loot item, influencing its selection probability.
     * @param parameterTypes The types of parameters needed for the item's constructor.
     * @param parameters     The parameters to be passed when creating a new instance of the item.
     */
    public void addItem(Class<? extends AbstractItem> item, double weight, Class<?>[] parameterTypes,
                        Object[] parameters) {
        this.items.add(new LootItemConfig(item, weight, parameterTypes, parameters));
    }

    /**
     * Retrieves the list of loot items in this loot table.
     *
     * @return List of LootItemConfig objects representing the items and their weights in the loot table.
     */
    public List<LootItemConfig> getItems() {return this.items;}

    /**
     * Selects a random item from the loot table based on the weights of each item.
     * Items with higher weights have a greater probability of being selected.
     *
     * @return A new instance of a randomly selected AbstractItem, or null if an error occurs during instantiation.
     */
    public AbstractItem getRandomItem() {
        double totalWeight = items.stream().mapToDouble(LootItemConfig::getWeight).sum();
        double randomValue = Math.random() * totalWeight;
        double cumulativeWeight = 0.0;

        for (LootItemConfig item : items) {
            cumulativeWeight += item.getWeight();
            if (randomValue <= cumulativeWeight) {
                try {
                    return item.createNewItem(); // Create a new item instance with parameters
                } catch (Exception e) {
                    e.printStackTrace(); // Handle instantiation exceptions
                }
            }
        }
        return null; // Should not reach here if weights are set correctly
    }

    /**
     * Retrieves a list of multiple random items from the loot table, based on the specified number of rolls.
     * Each roll independently selects an item, allowing for duplicates.
     *
     * @param rolls The number of times to roll for items, determining how many items are returned.
     * @return A list of randomly selected AbstractItem objects.
     */
    public List<AbstractItem> getRandomItems(int rolls) {
        List<AbstractItem> selectedItems = new ArrayList<>();
        for (int i = 0; i < rolls; i++) {
            selectedItems.add(getRandomItem());
        }
        return selectedItems;
    }
}
