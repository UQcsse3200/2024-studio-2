package com.csse3200.game.inventory.items.effects.healing;

import com.csse3200.game.inventory.items.effects.AbstractEffect;

/**
 * The {@code HealingEffect} class represents an effect that heals a player or game entity by a specified amount.
 *
 * <p>
 * This class implements the {@link AbstractEffect} interface, providing the functionality to heal an entity
 * and a description of the effect. The healing amount is specified when creating an instance of this class.
 * </p>
 *
 * <p>
 * When the {@link #apply()} method is called, the effect will increase the health of the target by the
 * specified healing amount.
 * </p>
 */

public class HealEffect implements AbstractEffect {

    /**
     * The amount of health that this effect will restore when applied.
     */
    private int healingAmount;

    /**
     * Constructs a new {@code HealingEffect} with the specified healing amount.
     *
     * @param healingAmount the amount of health to restore when the effect is applied
     */
    public HealEffect(int healingAmount) {
        this.healingAmount = healingAmount;
    }

    /**
     * Applies the healing effect by increasing the health of the target by the specified healing amount.
     * The specific implementation of how health is increased would be defined in this method.
     */
    @Override
    public void apply() {
        // Insert Healing Logic Here
        System.out.println("Player healed by " + healingAmount + " points.");
    }

    /**
     * Returns a description of the healing effect, including the amount of health restored.
     *
     * @return a string describing the healing effect
     */
    @Override
    public String getDescription() {
        return "Heals the player by " + healingAmount + " points.";
    }
}

