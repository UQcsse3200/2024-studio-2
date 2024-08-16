package com.csse3200.game.inventory.items.food;

import com.csse3200.game.inventory.items.ConsumableItem;
import com.csse3200.game.inventory.items.ItemUsageContext;
import com.csse3200.game.inventory.items.effects.AbstractEffect;
import com.csse3200.game.inventory.items.effects.feeding.FeedEffect;
import com.csse3200.game.inventory.items.exceptions.ConsumedException;

/**
 * The AbstractFood class manages the amount of nutrition points (provided from food items) is added to the hunger bar
 * This classes applies the effect of the nutrition points towards the animal hunger bar
 */
public class AbstractFood extends ConsumableItem {
    /**
     * The feeding effect that food can apply on animals
     */
    protected AbstractEffect feedingEffect;

    /**
     * Constructs a ConsumableItems with the specified uses
     *
     * @param name the name of the item
     * @param itemCode the item code
     * @param limit the stack limit of the item
     * @param quantity the initial quantity for this item
     */
    public AbstractFood(String name, int itemCode, int limit, int quantity, FeedEffect feedingEffect) {
        super(name, itemCode, limit, quantity);
        this.feedingEffect = feedingEffect;
    }

    /**
     * Returns the effect of the food
     */
    public AbstractEffect getFeedingEffect() {
        return this.feedingEffect;
    }

    /**
     * Applies the effects of the food. This method will be used by all food types
     */
    public void applyFeedingEffect() {
        this.feedingEffect.apply();
    }

    /**
     *  Uses the food by applying its effects and decreasing the number of uses
     *  If no uses are left the potion will not apply the effect and will throw an error
     *
     * @param inputs the context in which the item is used
     */
    @Override
    public void useItem(ItemUsageContext inputs) {
        if (super.isEmpty()) {
            throw new ConsumedException();
        }
        applyFeedingEffect();
        this.quantity--;
    }
}
