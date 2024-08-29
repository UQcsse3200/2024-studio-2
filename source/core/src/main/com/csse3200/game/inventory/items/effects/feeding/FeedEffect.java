package com.csse3200.game.inventory.items.effects.feeding;

import com.csse3200.game.inventory.items.effects.AbstractEffect;

public class FeedEffect implements AbstractEffect {
    /**
     * The amount of nutrition points this effect will restore from hunger when applied
     */
    protected int feedingAmount;

    /**
     * Constructs a new {@code FoodEffect} with the specified feeding amount
     */
    public FeedEffect(int feedingAmount) {
        this.feedingAmount = feedingAmount;
    }

    /**
     * Applies the feeding effect by increasing the health of the target by the specified healing amount.
     * the specific implementation of how the hunger bar increases would be defined in this method
     */
    @Override
    public void apply() {
        // Add more logic here by talking to the player team (team 1)
        System.out.printf("Player has fed animal by %d points\n", feedingAmount);
    }

    /**
     * Returns the description of the feeding effect including the amount restored to hunger bar
     */
    @Override
    public String getDescription() {
        return "Animal as been fed" + feedingAmount + " by player";
    }

}
