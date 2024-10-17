package com.csse3200.game.components.combat.move;

import com.badlogic.gdx.math.MathUtils;
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
     * Constructs the SpecialWaterMove with the given move name and hunger cost.
     *
     * @param moveName    the name of the special move.
     * @param hungerCost the hunger cost required to perform the special move.
     */
    public SpecialWaterMove(String moveName, int hungerCost) {
        super(moveName, hungerCost);
    }

    /**
     * Applies a random status effect to the target player after the move is executed
     * Also apply debuff which decreases Player's strength by 20 and defense by 10.
     *
     * @param targetStats combat stats of the target (player) that will be affected by the debuffs.
     */
    @Override
    protected void applyDebuffs(CombatStatsComponent targetStats) {
        // Applies debuffs to target's stats
        targetStats.addStrength(-20);
        targetStats.addDefense(-10);

        int rand = (int) (MathUtils.random() * 2);
        CombatStatsComponent.StatusEffect statusEffect = switch (rand) {
            case 0 -> CombatStatsComponent.StatusEffect.CONFUSED;
            case 1 -> CombatStatsComponent.StatusEffect.POISONED;
            default -> throw new IllegalStateException("Unexpected value: " + rand);
        };
        targetStats.addStatusEffect(statusEffect);
        logger.info("Status effect {} applied to the {}", statusEffect.name(), targetStats.isPlayer() ? "PLAYER" : "ENEMY");
    }

    /**
     * Buffs Water Boss's strength and defense stats after the special move.
     * This method increases Water Boss's strength by 10 and defense by 25.
     *
     * @param attackerStats combat stats of Kanga, who is performing the special move.
     */
    @Override
    protected void applyBuffs(CombatStatsComponent attackerStats) {
        attackerStats.addStrength(10);
        attackerStats.addDefense(25);
        logger.info("{} increased its strength to {} and defense to {}.",
                attackerStats.isPlayer() ? "PLAYER" : "ENEMY",
                attackerStats.getStrength(),
                attackerStats.getDefense());
    }
}
