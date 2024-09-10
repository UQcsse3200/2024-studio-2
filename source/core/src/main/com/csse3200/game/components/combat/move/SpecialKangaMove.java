package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.CombatStatsComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The SpecialKangaMove class represents Kanga's special combat move, which inflicts debuffs
 * on the player and buffs Kanga's own stats. This move is unique to Kanga and impacts both
 * the target and the attacker.
 */
public class SpecialKangaMove extends SpecialMove {
    private static final Logger logger = LoggerFactory.getLogger(SpecialKangaMove.class);

    /**
     * Constructs the SpecialKangaMove with the given move name and stamina cost.
     *
     * @param moveName    the name of the special Kanga move.
     * @param staminaCost the stamina cost required to perform the special Kanga move.
     */
    public SpecialKangaMove(String moveName, int staminaCost) {
        super(moveName, staminaCost);
    }

    /**
     * Applies debuffs (confusion and bleeding) to the target player after the move is executed.
     *
     * This method applies two status effects to the target: CONFUSION and BLEEDING.
     *
     * @param targetStats combat stats of the target (player) that will be affected by the debuffs.
     */
    @Override
    protected void applyDebuffs(CombatStatsComponent targetStats) {
        targetStats.addStatusEffect(CombatStatsComponent.StatusEffect.CONFUSION);
        targetStats.addStatusEffect(CombatStatsComponent.StatusEffect.BLEEDING);
        logger.info("Kanga inflicted confusion and bleeding on the player.");
    }

    /**
     * Buffs Kanga's strength and defense stats after the special move.
     *
     * This method increases Kanga's strength by 15 and defense by 10, making Kanga stronger and more resilient.
     *
     * @param attackerStats combat stats of Kanga, who is performing the special move.
     */
    @Override
    protected void applyBuffs(CombatStatsComponent attackerStats) {
        attackerStats.addStrength(15);
        attackerStats.addDefense(10);
        logger.info("Kanga increased its strength and defense.");
    }
}
