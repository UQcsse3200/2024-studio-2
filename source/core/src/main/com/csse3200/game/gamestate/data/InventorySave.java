package com.csse3200.game.gamestate.data;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.components.quests.QuestBasic;
import com.csse3200.game.inventory.items.AbstractItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;

public class InventorySave implements Json.Serializable {
    private final Logger logger = LoggerFactory.getLogger(InventorySave.class);
    public AbstractItem[] inventoryContent = new AbstractItem[0];

    public void write(Json json) {
        json.writeArrayStart("inventory");
        logger.info("list: {}", Arrays.toString(inventoryContent));
        for(AbstractItem element : inventoryContent) {
            if(element == null) {
                json.writeValue(null);
            } else {
                logger.info("item: {}", element);
                json.writeObjectStart();
                json.writeType(element.getClass());
                json.writeValue("quantity", element.getQuantity());
                json.writeObjectEnd();
            }

        }
        json.writeArrayEnd();
    }

    public void read(Json json, JsonValue jsonData) {
        ArrayList<AbstractItem> newItems = new ArrayList<>();
        for (JsonValue item : jsonData.child) {
            if(!item.isNull()) {
                try {
                    Class<?> clazz = Class.forName(item.get("class").asString());
                    Constructor<?> con = clazz.getConstructor(int.class);
                    AbstractItem newItem = (AbstractItem) con.newInstance(item.get("quantity").asInt());
                    newItems.add(newItem);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                newItems.add(null);
            }
        }
        inventoryContent = newItems.toArray(new AbstractItem[0]);
    }
}
