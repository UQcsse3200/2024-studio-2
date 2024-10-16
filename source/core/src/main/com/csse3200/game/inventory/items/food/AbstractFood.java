package com.csse3200.game.inventory.items.food;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.inventory.items.ConsumableItem;
import com.csse3200.game.inventory.items.ItemUsageContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The AbstractFood class manages the amount of hunger points (provided from food items) is added to the hunger bar
 * This classes applies the effect of the nutrition points towards the animal hunger bar
 */
public class AbstractFood extends ConsumableItem {
    private static final Logger logger = LoggerFactory.getLogger(AbstractFood.class);
    /**
     * The feeding effect that food can apply on animals
     */
    private final int feedingAmount;

    /**
     * Constructs a AbstractFood with the specified uses
     *
     * @param name the name of the item
     * @param itemCode the item code
     * @param limit the stack limit of the item
     * @param quantity the initial quantity for this item
     */
    protected AbstractFood(String name, int itemCode, int limit, int quantity, int feedingAmount) {
        super(name, itemCode, limit, quantity);
        this.feedingAmount = feedingAmount;
    }

    /**
     *  Uses the food by applying its effects and decreasing the number of uses
     *  If no uses are left the potion will not apply the effect and will throw an error
     *
     * @param context the context in which the item is used (contains a Player to use on)
     */
    @Override
    public void useItem(ItemUsageContext context) {
        super.useItem(context);
        if (context.player == null) {
            logger.warn("Using item that requires a player without providing a player!");
        } else {
            context.player.getComponent(CombatStatsComponent.class).addHunger(this.feedingAmount);
            String msg = String.format(
                    "Ate food %s and increased hunger by %d", this.getName(), this.feedingAmount);
            logger.debug(msg);
        }
    }
}
