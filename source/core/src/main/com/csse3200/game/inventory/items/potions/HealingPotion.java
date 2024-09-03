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
    public HealingPotion(int quantity) {
        super("Health Potion", 2, 3, quantity, 25);
        this.setTexturePath(path);
        this.setDescription("This is a health potion");
    }

    /**
     * Uses the potion by healing the player a certain amount.
     *
     * @param context the context in which the item is used (contains a player entity)
     */
    @Override
    public void useItem(ItemUsageContext context) {
        super.useItem(context);
        context.player.getComponent(CombatStatsComponent.class).addHealth(this.effectAmount);
    }
}