package com.csse3200.game.entities.configs;

import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.inventory.items.food.Foods;

public class AppleEntityConfig extends BaseEntityItemConfig{
    public String appleName = "Apple";
    public String[] appleDescription = {"This is a apple, to pick up press P"};

    @Override
    public String getItemName() {
        return appleName;
    }

    @Override
    public String[] getItemDescription() {
        return appleDescription;
    }
}
