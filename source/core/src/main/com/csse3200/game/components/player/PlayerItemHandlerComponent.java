package com.csse3200.game.components.player;

import com.csse3200.game.components.Component;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.inventory.items.ItemUsageContext;

public class PlayerItemHandlerComponent extends Component {
    @Override
    public void create() {
        entity.getEvents().addListener("useItem", this::useItem);
    }

    // Use the item on a player.
    private void useItem(AbstractItem item) {
        ItemUsageContext context = new ItemUsageContext(entity);
        item.useItem(context);
    }
}
