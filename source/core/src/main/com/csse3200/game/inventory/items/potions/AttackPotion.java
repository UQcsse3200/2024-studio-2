package com.csse3200.game.inventory.items.potions;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.inventory.items.ItemUsageContext;

import java.util.Timer;
import java.util.TimerTask;

public class AttackPotion extends  AbstractPotion{
    private final static String path = "images/potiontexture/attack.png";
    /**
     * default time of potion effect
     */
    private static final long DURATION = 120000;

    /**
     * Constructs a new {@code HealingPotion} with the specified quantity and a default healing effect.
     *
     * @param quantity the number of uses this potion has
     */
    public AttackPotion(int quantity, CombatStatsComponent playerStat) {
        super("Defense Potion", 2, 3, quantity, 25, playerStat);
        this.setTexturePath(path);
        this.setDescription("This is a defense potion");
    }

    /**
     * Applies the effects of this potion. This method must be implemented by subclasses
     * to define how the specific effects of the potion are applied. These effects will be reverted once two minutes
     * in game time have passed
     */
    public void applyAttackEffect() {
        int revert = this.effectAmount;
        this.playerStats.addStrength(this.effectAmount);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Revert the defense boost
                playerStats.addStrength(-(revert));
                System.out.println("Attack effect has been reverted");
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
            applyAttackEffect();
            System.out.printf("Player has increase the animal's attack by %d points\n", this.effectAmount);
        }
        super.useItem(inputs);
    }
}
