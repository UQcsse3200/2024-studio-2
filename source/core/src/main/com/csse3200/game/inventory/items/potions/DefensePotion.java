package com.csse3200.game.inventory.items.potions;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.inventory.items.ItemUsageContext;
import com.csse3200.game.inventory.items.TimedUseItem;
import com.csse3200.game.services.GameTime;

/**
 * The {@code DefensePotion} class represents a specific type of potion that temporarily increases the defense a
 * player.
 *
 * <p>
 * This class extends the {@link TimedUseItem} abstract class and provides an implementation of the
 *  method to apply its defense effects. A defense potion contains one or more
 *  objects that determine how much defense is increase when the potion is used.
 * </p>
 *
 * <p>
 * When a  is created, it is initialized with a specific quantity (number of uses)
 * </p>
 *
 * <p>
 * The {@code DefensePotion} is typically used in the context of gameplay where players consume the potion
 * to temporarily increase defense. The specific effects of the potion are defined by the instances
 * passed to the constructor.
 * </p>
 *
 * @see TimedUseItem
 */
public class DefensePotion extends TimedUseItem {
    private final static String path = "images/potiontexture/defense.png";
    private final static long duration = 120000;
    private final static String msg = "Cannot use defense potion on map";

    /**
     * Constructs a new {@code HealingPotion} with the specified quantity and a default healing effect.
     *
     * @param quantity the number of uses this potion has
     */
    public DefensePotion(int quantity) {
        super("Defense Potion", 53, 3, quantity, 25, duration, msg);
        this.setTexturePath(path);
        this.setDescription("This is a defense potion");
    }

    /**
     * Uses the potion by applying its effects and decreasing its number of uses.
     * If no uses are left, the potion is marked as empty.
     *
     * @param context the context in which the item is used (contains a Player to use on)
     */
    @Override
    public void useItem(ItemUsageContext context) {
        super.useItem(context);
        context.player.getComponent(CombatStatsComponent.class).addDefense(this.effectAmount);
    }

    /**
     * Updates the potion state, checking if the effect duration has elapsed.
     */
    @Override
    public void update(ItemUsageContext context) {
        //if (isExpired(context)) {
            CombatStatsComponent stats = context.player.getComponent(CombatStatsComponent.class);
            stats.setDefense(stats.getDefense() - this.effectAmount);
        //}
    }
}
