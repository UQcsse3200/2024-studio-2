package com.csse3200.game.inventory.items.food;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.inventory.items.ConsumableItem;
import com.csse3200.game.inventory.items.ItemUsageContext;

/**
 * The AbstractFood class manages the amount of hunger points (provided from food items) is added to the hunger bar
 * This classes applies the effect of the nutrition points towards the animal hunger bar
 */
public class AbstractFood extends ConsumableItem {
    /**
     * The feeding effect that food can apply on animals
     */
    protected int feedingAmount;
    protected CombatStatsComponent playerStats;

    /**
     * Constructs a AbstractFood with the specified uses
     *
     * @param name the name of the item
     * @param itemCode the item code
     * @param limit the stack limit of the item
     * @param quantity the initial quantity for this item
     */
    protected AbstractFood(String name, int itemCode, int limit, int quantity, int feedingAmount, CombatStatsComponent playerStats) {
        super(name, itemCode, limit, quantity);
        this.feedingAmount = feedingAmount;
        this.playerStats = playerStats;
    }

    /**
     * Returns the effect of the food
     */
    public int getFeedingEffect() {
        return this.feedingAmount;
    }

    /**
     * Applies the effects of the food. This method will be used by all food type
     */
    public void applyFeedingEffect() {
        if (this.playerStats.getHunger() + this.feedingAmount <= 100) {
            this.playerStats.addHunger(this.feedingAmount);
        } else {
            int remainder = this.feedingAmount - (this.playerStats.getHunger() + this.feedingAmount - 100);
            this.playerStats.addHunger(remainder);
        }
    }

    /**
     *  Uses the food by applying its effects and decreasing the number of uses
     *  If no uses are left the potion will not apply the effect and will throw an error
     *
     * @param inputs the context in which the item is used
     */
    @Override
    public void useItem(ItemUsageContext inputs) {
        if (!super.isEmpty()) {
            applyFeedingEffect();
            System.out.printf("Player has fed animal by %d points\n", feedingAmount);
        }
        super.useItem(inputs);
    }
}
