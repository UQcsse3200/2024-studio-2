package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.CombatStatsComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpecialKangaMove extends CombatMove {
    private static final Logger logger = LoggerFactory.getLogger(SpecialKangaMove.class);

    public SpecialKangaMove(String moveName, int staminaCost) {
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
                applyDebuffsToPlayer(targetStats);
            } else {
                logger.info("Player blocked Kanga's special move with guard!");
            }

            applyBuffsToKanga(attackerStats);
            logger.info("Kanga used the special move '{}'!", getMoveName());

            attackerStats.addStamina(-getStaminaCost());

        } else {
            logger.error("Either attacker or target does not have CombatStatsComponent.");
        }
    }

    /**
     * Applies debuffs (confusion and bleeding) to the player.
     *
     * @param targetStats combat stats of the target (player).
     */
    private void applyDebuffsToPlayer(CombatStatsComponent targetStats) {
        // Apply confusion
        targetStats.addStatusEffect(CombatStatsComponent.StatusEffect.CONFUSION);
        logger.info("Kanga inflicted confusion on the player.");

        // Apply bleeding
        targetStats.addStatusEffect(CombatStatsComponent.StatusEffect.BLEEDING);
        logger.info("Kanga inflicted bleeding on the player.");
    }

    /**
     * Buffs Kanga's strength and defense after the special move.
     *
     * @param attackerStats combat stats of the attacker (Kanga).
     */
    private void applyBuffsToKanga(CombatStatsComponent attackerStats) {
        // Increase Kanga's strength by 15
        attackerStats.addStrength(15);
        logger.info("Kanga increased its strength by 15.");

        // Increase Kanga's defense by 10
        attackerStats.addDefense(10);
        logger.info("Kanga increased its defense by 10.");
    }
}
