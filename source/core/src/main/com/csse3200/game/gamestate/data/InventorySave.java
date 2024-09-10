package com.csse3200.game.gamestate.data;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.components.quests.QuestBasic;
import com.csse3200.game.inventory.items.AbstractItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class InventorySave implements Json.Serializable {
    public AbstractItem[] inventoryContent = new AbstractItem[0];
    private Logger logger = LoggerFactory.getLogger(InventorySave.class);

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

    }
}
