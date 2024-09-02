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
    public AttackPotion(int quantity) {
        super("Defense Potion", 2, 3, quantity, 25);
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
        context.player.getComponent(CombatStatsComponent.class).addStrength(this.effectAmount);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Revert the defense boost
                context.player.getComponent(CombatStatsComponent.class).addStrength(-effectAmount);
            }
        }, DURATION);
    }
}
