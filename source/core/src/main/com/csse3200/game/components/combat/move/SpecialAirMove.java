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
     * Applies a random status effect to the target player after the move is executed
     *
     * Also apply debuff which decreases Player's strength by 30 and defense by 25.
     *
     * @param targetStats combat stats of the target (player) that will be affected by the debuffs.
     */
    @Override
    protected void applyDebuffs(CombatStatsComponent targetStats) {
        // Applies debuffs to target's stats
        targetStats.addStrength(-30);
        targetStats.addDefense(-25);

        int rand = (int) (Math.random() * 2);
        CombatStatsComponent.StatusEffect statusEffect = switch (rand) {
            case 0 -> CombatStatsComponent.StatusEffect.CONFUSION;
            case 1 -> CombatStatsComponent.StatusEffect.SHOCKED;
            default -> throw new IllegalStateException("Unexpected value: " + rand);
        };
        targetStats.addStatusEffect(statusEffect);
        logger.info("Status effect {} applied to the {}", statusEffect.name(), targetStats.isPlayer() ? "PLAYER" : "ENEMY");
    }

    /**
     * Buffs Air Boss's strength and defense stats after the special move.
     *
     * This method increases Water Boss's strength by 25 and defense by 25.
     *
     * @param attackerStats combat stats of Kanga, who is performing the special move.
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
