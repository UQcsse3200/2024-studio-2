package com.csse3200.game.inventory.items.potions;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.inventory.items.ConsumableItem;
import com.csse3200.game.inventory.items.ItemUsageContext;

import java.util.List;

/**
 * The {@code AbstractPotion} class serves as abstract base class for all-potion types items in the game
 * A potion is a type of consumable item that can apply one or more effects to a player's animal
 *
 * <p>
 * This class provides common functionality for managing and applying effects, as well as handling
 * the number of uses as potion has. Specific potion types (e.g., Healing Potion) should extend create their own class
 * to apply effects
 * <p></p>
 *
 * <p>
 * The {@code AbstractPotion} class also inherits the general properties of a consumable item from
 * the {@link ConsumableItem} class, such as the number of uses and whether the item is consumed upon use.
 * </p>
 *
 * @see ConsumableItem
 */

public abstract class AbstractPotion extends ConsumableItem {

    /**
     * A list of possible effects that this potion can apply when used.
     */
    protected int effectAmount;
    protected CombatStatsComponent playerStats;

    /**
     * Constructs a new {@code Potion} with the specified quantity and a list of possible effects.
     *
     * @param quantity    the number of times this potion can be used
     *
     * potion can apply
     */
    protected AbstractPotion(String name, int itemCode, int limit, int quantity, int effectAmount, CombatStatsComponent playerStats) {
        super(name, itemCode, limit, quantity);
        this.effectAmount = effectAmount;
        this.playerStats = playerStats;
    }

    /**
     * returns the effect amount value
      * @return effectAmount - the amount added to the stats
     */
    public int getEffectAmount() {
        return this.effectAmount;
    }

//    /**
//     * Applies the effects of this potion. This method must be implemented by subclasses
//     * to define how the specific effects of the potion are applied
//     */
//    public abstract void applyEffect();

//    /**
//     * Uses the potion by applying its effects and decreasing its number of uses.
//     * If no uses are left, the potion is marked as empty.
//     *
//     * @param inputs the context in which the item is used, typically passed from the game engine
//     */
//    @Override
//    public void useItem(ItemUsageContext inputs) {
//        if (!super.isEmpty()) {
//            return;
//        }
//        super.useItem(inputs);
//    }
}
