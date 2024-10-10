package com.csse3200.game.inventory.items.lootbox.configs;

import com.csse3200.game.inventory.items.AbstractItem;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * LootItemConfig is a configuration class representing a specific loot item, including its class type,
 * weight, constructor parameters, and how to instantiate it. This class is used in loot tables to
 * define items that can be generated from loot boxes with a specified probability.
 */
public class LootItemConfig {
    private final Class<? extends AbstractItem> item;
    private final double weight;
    private final Class<?>[] parameterTypes;
    private final Object[] parameters;

    /**
     * Constructs a LootItemConfig with the specified item class, weight, and constructor parameters.
     *
     * @param item           The class type of the loot item (must extend AbstractItem).
     * @param weight         The weight of the item, determining its probability of being selected.
     * @param parameterTypes An array of Classes representing the types of parameters for the item's constructor.
     * @param parameters     An array of Objects representing the parameters to pass to the constructor when creating an item.
     */
    public LootItemConfig(Class<? extends AbstractItem> item, double weight, Class<?>[] parameterTypes,
                          Object[] parameters) {
        this.item = item;
        this.weight = weight;
        this.parameterTypes = parameterTypes;
        this.parameters = parameters;
    }

    /**
     * Gets the weight of the loot item, used to determine its selection probability in a loot table.
     *
     * @return The weight of the item as a double.
     */
    public double getWeight() {return weight;}

    /**
     * Creates a new instance of the loot item using reflection, with the specified constructor parameters.
     * This method finds the correct constructor based on the parameter types and instantiates the item.
     *
     * @return A new instance of the specified AbstractItem.
     * @throws NoSuchMethodException See below
     * @throws InvocationTargetException See below
     * @throws InstantiationException See below
     * @throws IllegalAccessException If the item cannot be instantiated,
     *     including issues with constructor access or parameter mismatches.
     */
    public AbstractItem createNewItem() throws NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException {
        // Use the constructor with specified parameter types
        Constructor<? extends AbstractItem> constructor = item.getDeclaredConstructor(parameterTypes);
        return constructor.newInstance(parameters);
    }
}