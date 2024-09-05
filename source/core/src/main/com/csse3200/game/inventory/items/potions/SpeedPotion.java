package com.csse3200.game.inventory.items.potions;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.inventory.items.ItemUsageContext;
import com.csse3200.game.services.GameTime;

public class SpeedPotion extends AbstractPotion{
    private final static String path = "images/potiontexture/speed.png";
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
    public SpeedPotion(int quantity, GameTime gameTime) {
        super("Defense Potion", 2, 3, quantity, 25);
        this.setTexturePath(path);
        this.setDescription("This is a speed potion");
        this.gameTime = gameTime;
    }

    public GameTime getGameTime() {
        return gameTime;
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
        // Record the time when the effect started
        effectStartTime = gameTime.getTime();
    }

    /**
     * Checks if the duration of potion usage is expired
     * @param context the ItemUsageContext
     * @return true if it is expired
     */
    public boolean isExpired(ItemUsageContext context) {
        return (gameTime.getTime() - effectStartTime >= DURATION);
    }

    /**
     * Updates the potion state, checking if the effect duration has elapsed.
     */
    public void update(ItemUsageContext context) {
        if (isExpired(context)) {
            CombatStatsComponent stats = context.player.getComponent(CombatStatsComponent.class);
            stats.setDefense(stats.getSpeed() - this.effectAmount);
        }
    }
}

