package com.csse3200.game.inventory.items.potions;

import com.csse3200.game.inventory.items.ConsumableItem;
import com.csse3200.game.inventory.items.ItemUsageContext;
import com.csse3200.game.inventory.items.effects.AbstractEffect;

import java.util.List;

/**
 * The {@code AbstractPotion} class serves as abstract base class for all-potion types items in the game
 * A potion is a type of consumable item that can apply one or more effects to a player's animal
 *
 * <p>
 * This class provides common functionality for managing and applying effects, as well as handling
 * the number of uses as potion has. Specific potion types (e.g., Healing Potion) should extend
 * this class and implement the {@link #applyEffect()} method to define how their specific effects
 * are applied
 * </p>
 *
 * <p>
 * Each potion has a list of possible effects, which are stored as {@link AbstractEffect} objects. These
 * can be applied when the potion is consumed, and the specific behaviour of these effects is defined
 * by the classes that implement the {@link AbstractEffect} interface.
 * </p>
 *
 * <p>
 * The {@code AbstractPotion} class also inherits the general properties of a consumable item from
 * the {@link ConsumableItem} class, such as the number of uses and whether the item is consumed upon use.
 * </p>
 *
 * @see ConsumableItem
 * @see AbstractEffect
 */

public abstract class AbstractPotion extends ConsumableItem {

    /**
     * A list of possible effects that this potion can apply when used.
     */
    protected List<AbstractEffect> possibleEffects;


    /**
     * Constructs a new {@code Potion} with the specified quantity and a list of possible effects.
     *
     * @param quantity    the number of times this potion can be used
     * @param possibleEffects a list of {@link AbstractEffect} objects representing the effects that this
     * potion can apply
     */
    protected AbstractPotion(String name, int itemCode, int limit, int quantity, List<AbstractEffect> possibleEffects) {
        super(name, itemCode, limit, quantity);
        this.possibleEffects = possibleEffects;

    }

    /**
     * Returns the list of possible effects that this potion can apply.
     *
     * @return a list of {@link AbstractEffect} objects representing the effects of this potion
     */
    public List<AbstractEffect> getPossibleEffects() {
        return possibleEffects;
    }


    /**
     * Set the list of possible effects for this potion.
     *
     * @param possibleEffects a list of {@link AbstractEffect} objects representing the new effects for this potion
     */
    public void setPossibleEffects(List<AbstractEffect> possibleEffects) {
        this.possibleEffects = possibleEffects;
    }

    /**
     * Applies the effects of this potion. This method must be implemented by subclasses
     * to define how the specific effects of the potion are applied
     */
    public abstract void applyEffect();

    /**
     * Uses the potion by applying its effects and decreasing its number of uses.
     * If no uses are left, the potion is marked as empty.
     *
     * @param inputs the context in which the item is used, typically passed from the game engine
     */
    @Override
    public void useItem(ItemUsageContext inputs) {
        if (this.quantity > 0) {
            applyEffect();
        }
        super.useItem(inputs);
    }
}
