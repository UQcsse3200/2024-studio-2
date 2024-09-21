package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.CombatStatsComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The SpecialAirMove class represents Air boss's special combat move, which inflicts debuffs
 * on the player and buffs Air boss's own stats. This move is unique to Air boss and impacts both
 * the target and the attacker.
 */
public class SpecialAirMove extends SpecialMove {
    private static final Logger logger = LoggerFactory.getLogger(SpecialAirMove.class);

    /**
     * Constructs the SpecialAirMove with the given move name and stamina cost.
     *
     * @param moveName    the name of the special move.
     * @param staminaCost the stamina cost required to perform the special move.
     */
    public SpecialAirMove(String moveName, int staminaCost) {
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
        targetStats.addStrength(-20);
        targetStats.addDefense(-20);

        // Inflicting CONFUSION status effect, which causes the target to use a random move for 2 turns
        targetStats.addStatusEffect(CombatStatsComponent.StatusEffect.CONFUSION);
        // Inflicting BLEEDING status effect, which causes the target to lose health for 3 turns
        targetStats.addStatusEffect(CombatStatsComponent.StatusEffect.SHOCKED);

        logger.info("{} inflicted CONFUSION and SHOCKED.", targetStats.isPlayer() ? "PLAYER" : "ENEMY");
    }

    /**
     * Buffs attacker's strength and defense stats after the special move.
     *
     * @param attackerStats combat stats of the attacker, who is performing the special move.
     */
    @Override
    protected void applyBuffs(CombatStatsComponent attackerStats) {
        attackerStats.addStrength(25);
        attackerStats.addDefense(25);
        logger.info("{} increased its strength to {} and defense to {}.",
                attackerStats.isPlayer() ? "PLAYER" : "ENEMY",
                attackerStats.getStrength(),
                attackerStats.getDefense());
    }
}
