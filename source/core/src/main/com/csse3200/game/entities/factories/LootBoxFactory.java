package com.csse3200.game.entities.factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.inventory.items.lootbox.UniversalLootBox;
import com.csse3200.game.inventory.items.lootbox.configs.BaseLootTable;

import java.lang.reflect.Constructor;


public class LootBoxFactory {

    private static final String CONFIG_FILE = "configs/loottables.json";  // Ensure this is in your assets folder

    // Create a loot box based on its name and assign it to a player
    public UniversalLootBox createLootBox(String lootBoxName, Entity player) throws ClassNotFoundException {
        JsonReader jsonReader = new JsonReader();
        JsonValue root = jsonReader.parse(Gdx.files.internal(CONFIG_FILE));

        // Iterate through the loot boxes in the JSON config
        for (JsonValue lootBoxJson : root.get("lootBoxes")) {
            if (lootBoxJson.getString("name").equals(lootBoxName)) {
                BaseLootTable lootTable = createLootTable(lootBoxJson);
                int rolls = lootBoxJson.getInt("rolls");
                String description = lootBoxJson.getString("description");
                String imagePath = lootBoxJson.getString("image");
                int code = lootBoxJson.getInt("code");

                // Create the loot box
                UniversalLootBox lootBox = new UniversalLootBox(lootTable, rolls, player, description, imagePath, code);


                return lootBox;
            }
        }

        throw new IllegalArgumentException("Loot box not found: " + lootBoxName);
    }

    // Create a loot table from the JSON config, dynamically creating items
    private BaseLootTable createLootTable(JsonValue config) throws ClassNotFoundException {
        BaseLootTable lootTable = new BaseLootTable();

        // Populate the loot table with items from the config
        for (JsonValue itemJson : config.get("items")) {
            Class<?> itemClass = Class.forName(itemJson.getString("class"));
            double weight = itemJson.getDouble("weight");

            // Parse constructor arguments and their types
            Object[][] parsedData = parseConstructorArgsAndTypes(itemJson.get("constructorArgs"));
            Object[] constructorArgs = parsedData[0];  // Extract the arguments
            Class<?>[] paramTypes = (Class<?>[]) parsedData[1];  // Extract the parameter types

            // Try to create the item using reflection and add to the loot table
            try {
                AbstractItem item = createItem(itemClass, paramTypes, constructorArgs);
                lootTable.addItem((Class<? extends AbstractItem>) itemClass, weight, paramTypes, constructorArgs);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error creating item: " + itemJson.getString("class"), e);
            }
        }

        return lootTable;
    }

    // Combined method to parse constructor arguments and their types
    private Object[][] parseConstructorArgsAndTypes(JsonValue jsonArgs) {
        Object[] constructorArgs = new Object[jsonArgs.size];
        Class<?>[] paramTypes = new Class<?>[jsonArgs.size];

        for (int i = 0; i < jsonArgs.size; i++) {
            JsonValue arg = jsonArgs.get(i);

            // Check the type and assign both the argument and its corresponding class type
            if (arg.isNumber()) {
                if (arg.asFloat() == (float) arg.asInt()) {
                    constructorArgs[i] = arg.asInt();  // It's an integer
                    paramTypes[i] = int.class;  // Set the parameter type as int.class
                } else {
                    constructorArgs[i] = arg.asFloat();  // It's a float
                    paramTypes[i] = float.class;  // Set the parameter type as float.class
                }
            } else if (arg.isBoolean()) {
                constructorArgs[i] = arg.asBoolean();  // It's a boolean
                paramTypes[i] = boolean.class;  // Set the parameter type as boolean.class
            } else if (arg.isString()) {
                constructorArgs[i] = arg.asString();  // It's a string
                paramTypes[i] = String.class;  // Set the parameter type as String.class
            } else {
                constructorArgs[i] = arg.asString();  // Default fallback to string
                paramTypes[i] = String.class;  // Fallback to String class
            }
        }

        return new Object[][]{constructorArgs, paramTypes};  // Return both arguments and types
    }

    // Helper method to instantiate an item dynamically using reflection
    private AbstractItem createItem(Class<?> itemClass, Class<?>[] paramTypes, Object[] constructorArgs) throws Exception {
        Constructor<?> constructor = itemClass.getDeclaredConstructor(paramTypes);
        return (AbstractItem) constructor.newInstance(constructorArgs);  // Create the item with the correct argument types
    }
}