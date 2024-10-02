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
     * Applies a random status effect to the target player after the move is executed
     *
     * Also apply debuff which decreases Player's strength by 15 and defense by 10.
     *
     * @param targetStats combat stats of the target (player) that will be affected by the debuffs.
     */
    @Override
    protected void applyDebuffs(CombatStatsComponent targetStats) {
        // Applies debuffs to target's stats
        targetStats.addStrength(-15);
        targetStats.addDefense(-15);

        int rand = (int) (Math.random() * 2);
        CombatStatsComponent.StatusEffect statusEffect = switch (rand) {
            case 0 -> CombatStatsComponent.StatusEffect.CONFUSED;
            case 1 -> CombatStatsComponent.StatusEffect.BLEEDING;
            default -> throw new IllegalStateException("Unexpected value: " + rand);
        };
        targetStats.addStatusEffect(statusEffect);
        logger.info("Status effect {} applied to the {}", statusEffect.name(), targetStats.isPlayer() ? "PLAYER" : "ENEMY");
    }

    /**
     * Buffs Kanga's strength and defense stats after the special move.
     *
     * This method increases Kanga's strength by 15 and defense by 10.
     *
     * @param attackerStats combat stats of Kanga, who is performing the special move.
     */
    @Override
    protected void applyBuffs(CombatStatsComponent attackerStats) {
        attackerStats.addStrength(15);
        attackerStats.addDefense(10);
        logger.info("{} increased its strength to {} and defense to {}.",
                attackerStats.isPlayer() ? "PLAYER" : "ENEMY",
                attackerStats.getStrength(),
                attackerStats.getDefense());
    }
}