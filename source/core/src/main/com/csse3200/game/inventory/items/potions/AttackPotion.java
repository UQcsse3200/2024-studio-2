package com.csse3200.game.inventory.items.potions;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.inventory.items.ItemUsageContext;
import com.csse3200.game.inventory.items.TimedUseItem;
import com.csse3200.game.services.GameTime;

public class AttackPotion extends TimedUseItem {
    private final static String path = "images/potiontexture/attack.png";
    private final static long duration = 120000;

    /**
     * Constructs a new {@code HealingPotion} with the specified quantity and a default healing effect.
     *
     * @param quantity the number of uses this potion has
     */
    public AttackPotion(int quantity) {
        super("Attack Potion", 54, 3, quantity, 25, duration);
        this.setTexturePath(path);
        this.setDescription("This is a attack potion");
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
    }

    /**
     * Updates the potion state, checking if the effect duration has elapsed.
     * @param context the context in which the item is used (contains a Player to use on)
     */
    @Override
    public void update(ItemUsageContext context) {
        if (this.gameTime.getTime() - this.effectStartTime >= this.getDuration()) {
            CombatStatsComponent stats = context.player.getComponent(CombatStatsComponent.class);
            stats.setStrength(stats.getStrength() - this.effectAmount);
        }
    }
}
