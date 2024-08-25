package com.csse3200.game.inventory.items.potions.healingpotion;

import com.csse3200.game.inventory.items.effects.AbstractEffect;
import com.csse3200.game.inventory.items.effects.healing.HealEffect;
import com.csse3200.game.inventory.items.potions.AbstractPotion;

import java.util.List;



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
    public HealingPotion(String name, int itemCode, int limit, int quantity) {
        super(name, itemCode, limit, quantity, List.of(new HealEffect (50)));
        this.setTexturePath(path);
        this.setDescription("This is a health potion");
    }

    /**
     * Applies the effects of the healing potion by iterating through the list of effects
     * and invoking their {@link AbstractEffect#apply()} method. This method is called each time
     * the potion is used, reducing the number of uses remaining.
     */
    @Override
    public void applyEffect() {
        for (AbstractEffect effect : possibleEffects) {
            effect.apply(); // Apply each effect in the list
        }
    }
}