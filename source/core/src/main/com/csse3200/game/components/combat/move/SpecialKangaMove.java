package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.CombatStatsComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpecialKangaMove extends SpecialMove {
    private static final Logger logger = LoggerFactory.getLogger(SpecialKangaMove.class);

    public SpecialKangaMove(String moveName, int staminaCost) {
        super(moveName, staminaCost);
    }

    /**
     * Applies debuffs (confusion and bleeding) to the player.
     *
     * @param targetStats combat stats of the target (player).
     */
    @Override
    protected void applyDebuffs(CombatStatsComponent targetStats) {
        targetStats.addStatusEffect(CombatStatsComponent.StatusEffect.CONFUSION);
        targetStats.addStatusEffect(CombatStatsComponent.StatusEffect.BLEEDING);
        logger.info("Kanga inflicted confusion and bleeding on the player.");
    }

    /**
     * Buffs Kanga's strength and defense after the special move.
     *
     * @param attackerStats combat stats of the attacker (Kanga).
     */
    @Override
    protected void applyBuffs(CombatStatsComponent attackerStats) {
        attackerStats.addStrength(15);
        attackerStats.addDefense(10);
        logger.info("Kanga increased its strength and defense.");
    }
}