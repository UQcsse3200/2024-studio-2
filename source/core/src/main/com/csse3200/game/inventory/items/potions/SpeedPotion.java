package com.csse3200.game.inventory.items.potions;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.inventory.items.ItemUsageContext;
import com.csse3200.game.inventory.items.TimedUseItem;

public class SpeedPotion extends TimedUseItem {
    private static final String PATH = "images/potiontexture/speed.png";
    private static final long DURATION = 120000;
    private static final String MSG = "Cannot use speed potion during combat";

    /**
     * Constructs a new {@code HealingPotion} with the specified quantity and a default healing effect.
     *
     * @param quantity the number of uses this potion has
     */
    public SpeedPotion(int quantity) {
        super("Speed Potion", 52, 3, quantity, 25, DURATION, MSG);
        this.setTexturePath(PATH);
        this.setDescription("This is a speed potion");
        this.onlyMapItem = true;
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
        context.player.getComponent(CombatStatsComponent.class).addSpeed(this.effectAmount);
    }

    /**
     * Updates the potion state, checking if the effect duration has elapsed.
     */
    @Override
    public void update(ItemUsageContext context) {
            CombatStatsComponent stats = context.player.getComponent(CombatStatsComponent.class);
            stats.setSpeed(stats.getSpeed() - this.effectAmount);
    }
}

