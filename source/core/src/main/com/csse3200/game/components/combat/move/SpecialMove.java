package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.CombatStatsComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SpecialMove extends CombatMove {
    private static final Logger logger = LoggerFactory.getLogger(SpecialMove.class);

    public SpecialMove(String moveName, int staminaCost) {
        super(moveName, staminaCost);
    }

    @Override
    public void execute(CombatStatsComponent attackerStats) {
        logger.error("Special move needs more arguments.");
    }

    @Override
    public void execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats) {
        execute(attackerStats, targetStats, false);
    }

    @Override
    public void execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats, boolean targetIsGuard,
                        int numHitsLanded) {
        execute(attackerStats, targetStats, false);
    }

    /**
     * Executes the special Kanga move. Buffs Kanga's strength and defense, confuses the player,
     * and applies a bleeding status effect to the player.
     *
     * @param attackerStats combat stats of the attacker (Kanga).
     * @param targetStats combat stats of the target (player).
     * @param targetIsGuarded whether the target is guarded.
     */
    @Override
    public void execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats, boolean targetIsGuarded) {
        if (attackerStats != null && targetStats != null) {
            if (!targetIsGuarded) {
                applyDebuffs(targetStats);
            } else {
                logger.info("Player blocked the special move with guard!");
            }

            applyBuffs(attackerStats);
            attackerStats.addStamina(-getStaminaCost());

            logger.info("Entity used the special move '{}'!", getMoveName());

        } else {
            logger.error("Either attacker or target does not have CombatStatsComponent.");
        }
    }

    // Abstract methods to be implemented by each special move
    protected abstract void applyDebuffs(CombatStatsComponent targetStats);
    protected abstract void applyBuffs(CombatStatsComponent attackerStats);
}
