package com.csse3200.game.inventory.items.effects;


/**
 * The {@code AbstractEffect} interface represents a generic effect that can be applied to a player or
 * game entity
 *
 * <p>
 * Implementing classes should define specific types of effects, such as healing, speed boosts, or damage
 * reduction. Each effect has an {@link #apply()} method that performs and a {@link #getDescription()}
 * method that provides a textual description of the effect.
 * </p>
 *
 * <p>
 * This interface allows for a flexible and extendable system where new types of effects can be created
 * by simply implementing this interface.
 * </p>
 */
public interface AbstractEffect {
    /**
     * Applies the effect. The specific behavior of this method is defined by the implementing class.
     */
    void apply();

    /**
     * Returns a description of the effect. This description is typically used for UI or debugging purposes.
     *
     * @return a string describing the effect
     */
    String getDescription();
}