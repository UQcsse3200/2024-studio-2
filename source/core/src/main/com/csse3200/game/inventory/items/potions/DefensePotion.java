package com.csse3200.game.inventory.items.potions;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.inventory.items.ItemUsageContext;

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

    /**
     * Constructs a new {@code HealingPotion} with the specified quantity and a default healing effect.
     *
     * @param quantity the number of uses this potion has
     */
    public DefensePotion(int quantity, CombatStatsComponent playerStat) {
        super("Defense Potion", 2, 3, quantity, 25, playerStat);
        this.setTexturePath(path);
        this.setDescription("This is a defense potion");
    }

    /**
     * Applies the effects of this potion. This method must be implemented by subclasses
     * to define how the specific effects of the potion are applied. These effects will be reverted once two minutes
     * in game time have passed
     */
    public void applyDefenseEffect() {
        int revert = this.effectAmount;
        this.playerStats.addDefense(this.effectAmount);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Revert the defense boost
                playerStats.addDefense(-(revert));
                System.out.println("Defense effect has been reverted");
            }
        }, DURATION);
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
            applyDefenseEffect();
            System.out.printf("Player has increase the animal's defense by %d points\n", this.effectAmount);
        }
        super.useItem(inputs);
    }
}
