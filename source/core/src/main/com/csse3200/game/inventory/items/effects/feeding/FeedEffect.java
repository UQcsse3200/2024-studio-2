package com.csse3200.game.inventory.items.effects.feeding;

import com.csse3200.game.inventory.items.effects.AbstractEffect;

public class FeedEffect {
    /**
     * The amount of nutrition points this effect will restore from hunger when applied
     */
    private int feedingAmount;

    /**
     * Constructs a new {@code FoodEffect} with the specified feeding amount
     *
     * @param feedingAmount the amount of the nutrition to restore when the effect is applied
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
        System.out.printf("Player has fed animal by %d points\n", feedingAmount);
    }

}
