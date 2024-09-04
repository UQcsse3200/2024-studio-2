package com.csse3200.game.inventory.items.potions;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.inventory.items.ItemUsageContext;
import com.csse3200.game.services.GameTime;

import java.util.Timer;
import java.util.TimerTask;

/**
 * The {@code DefensePotion} class represents a specific type of potion that temporarily increases the defense a
 * player.
 *
 * <p>
 * This class extends the {@link AbstractPotion} abstract class and provides an implementation of the
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
 * @see AbstractPotion
 */
public class DefensePotion extends AbstractPotion{
    private final static String path = "images/potiontexture/defense.png";
    /**
     * default time of potion effect
     */
    private static final long DURATION = 120000;
    private GameTime gameTime;
    private long effectStartTime;

    /**
     * Constructs a new {@code HealingPotion} with the specified quantity and a default healing effect.
     *
     * @param quantity the number of uses this potion has
     */
    public DefensePotion(int quantity, GameTime gameTime) {
        super("Defense Potion", 2, 3, quantity, 25);
        this.setTexturePath(path);
        this.setDescription("This is a defense potion");
        this.gameTime = gameTime;
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
        // Record the time when the effect started
        effectStartTime = gameTime.getTime();
    }

    /**
     * Updates the potion state, checking if the effect duration has elapsed.
     */
    public void update(ItemUsageContext context) {
        if (gameTime.getTime() - effectStartTime >= DURATION) {
            CombatStatsComponent stats = context.player.getComponent(CombatStatsComponent.class);
            stats.setDefense(stats.getDefense() - this.effectAmount);
        }
    }
}
