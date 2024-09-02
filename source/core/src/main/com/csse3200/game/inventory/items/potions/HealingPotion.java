package com.csse3200.game.inventory.items.potions;

import com.csse3200.game.inventory.items.ItemUsageContext;
import com.csse3200.game.components.CombatStatsComponent;


/**
 * The {@code HealingPotion} class represents a specific type of potion that heals a player or game entity.
 *
 * <p>
 * This class extends the {@link AbstractPotion} abstract class and provides an implementation of the
 *  method to apply its healing effects. A healing potion contains one or more
 *  objects that determine how much health is restored when the potion is used.
 * </p>
 *
 * <p>
 * When a  is created, it is initialized with a specific quantity (number of uses)
 * and a list of effects.  method is responsible for iterating through these effects
 * and applying them when the potion is consumed.
 * </p>
 *
 * <p>
 * The {@code HealingPotion} is typically used in the context of gameplay where players consume the potion
 * to restore health. The specific effects of the potion are defined by theinstances
 * passed to the constructor.
 * </p>
 *
 * @see AbstractPotion
 */

public class HealingPotion extends AbstractPotion{
    private final static String path = "images/Healthpotion.png";

    /**
     * Constructs a new {@code HealingPotion} with the specified quantity and a default healing effect.
     *
     * @param quantity the number of uses this potion has
     */
    public HealingPotion(int quantity, CombatStatsComponent playerStat) {
        super("Health Potion", 2, 3, quantity, 25, playerStat);
        this.setTexturePath(path);
        this.setDescription("This is a health potion");
    }

    /**
     * Applies the effects of this potion. This method must be implemented by subclasses
     * to define how the specific effects of the potion are applied
     */
    public void applyHealingEffect() {
        if (this.playerStats.getHealth() + this.effectAmount <= 100) {
            this.playerStats.addHealth(this.effectAmount);
        } else {
            int remainder = this.effectAmount - (this.playerStats.getHealth() + this.effectAmount - 100);
            this.playerStats.addHealth(remainder);
        }
    }

    /**
     * Uses the potion by applying its effects and decreasing its number of uses.
     * If no uses are left, the potion is marked as empty.
     *
     * @param inputs the context in which the item is used, typically passed from the game engine
     */
    @Override
    public void useItem(ItemUsageContext inputs) {
        if (!super.isEmpty()) {
            applyHealingEffect();
            System.out.printf("Player has %d defense points\n", this.playerStats.getHealth());
            System.out.printf("Player has healed animal by %d points\n", this.effectAmount);
        }
        super.useItem(inputs);
    }
}