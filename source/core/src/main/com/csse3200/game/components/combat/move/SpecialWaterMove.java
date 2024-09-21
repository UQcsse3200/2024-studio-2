package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.CombatStatsComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The SpecialWaterMove class represents Water boss's special combat move, which inflicts debuffs
 * on the player and buffs Water boss's own stats. This move is unique to Water boss and impacts both
 * the target and the attacker.
 */
public class SpecialWaterMove extends SpecialMove {
    private static final Logger logger = LoggerFactory.getLogger(SpecialWaterMove.class);

    /**
     * Constructs the SpecialWaterMove with the given move name and stamina cost.
     *
     * @param moveName    the name of the special move.
     * @param staminaCost the stamina cost required to perform the special move.
     */
    public SpecialWaterMove(String moveName, int staminaCost) {
        super(moveName, staminaCost);
    }

    /**
     * Applies debuffs and status effect(s) to the target player after the move is executed.
     *
     * @param targetStats combat stats of the target (player) that will be affected by the debuffs.
     */
    @Override
    protected void applyDebuffs(CombatStatsComponent targetStats) {
        // Applies debuffs to target's stats
        targetStats.addStrength(-15);
        targetStats.addDefense(-15);

        // Inflicting CONFUSION status effect, which causes the target to use a random move for 2 turns
        targetStats.addStatusEffect(CombatStatsComponent.StatusEffect.CONFUSION);
        // Inflicting POISONED status effect, which causes the target to lose health for 3 turns
        targetStats.addStatusEffect(CombatStatsComponent.StatusEffect.POISONED);

        logger.info("{} inflicted CONFUSION and POISONED.", targetStats.isPlayer() ? "PLAYER" : "ENEMY");
    }

    /**
     * Buffs attacker's strength and defense stats after the special move.
     *
     * @param attackerStats combat stats of the attacker, who is performing the special move.
     */
    @Override
    protected void applyBuffs(CombatStatsComponent attackerStats) {
        attackerStats.addStrength(20);
        attackerStats.addDefense(15);
        logger.info("{} increased its strength to {} and defense to {}.",
                attackerStats.isPlayer() ? "PLAYER" : "ENEMY",
                attackerStats.getStrength(),
                attackerStats.getDefense());
    }
}
