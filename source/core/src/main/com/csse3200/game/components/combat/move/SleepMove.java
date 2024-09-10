package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.CombatStatsComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SleepMove extends CombatMove {
    private static final Logger logger = LoggerFactory.getLogger(SleepMove.class);

    public SleepMove(String moveName, int staminaCost) {
        super(moveName, staminaCost);
    }

    @Override
    public void execute(CombatStatsComponent attackerStats) {
        if (attackerStats != null) {
            // Restore 25% of the user's stamina and 10% of their health
            attackerStats.addStamina((int) (0.25 * attackerStats.getMaxStamina()));
            attackerStats.addHealth((int) (0.1 * attackerStats.getMaxHealth()));
            logger.info("Sleep increased stamina to {} and health to {}", attackerStats.getStamina(),
                    attackerStats.getHealth());
        } else {
            logger.error("Entity does not have CombatStatsComponent");
        }
    }

    @Override
    public void execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats) {
        execute(attackerStats);
    }

    @Override
    public void execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats, boolean targetIsGuarded) {
        execute(attackerStats);
    }
}
