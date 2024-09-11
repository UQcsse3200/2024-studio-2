package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.CombatStatsComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents an attack move in combat. This class handles the execution of the attack and the
 * damage calculation based on various factors such as stamina, status effects, and the target's defense.
 */
public class AttackMove extends CombatMove {
    private static final Logger logger = LoggerFactory.getLogger(AttackMove.class);

    /**
     * Constructor for the AttackMove.
     *
     * @param moveName    the name of the move.
     * @param staminaCost the amount of stamina required to perform the move.
     */
    public AttackMove(String moveName, int staminaCost) {
        super(moveName, staminaCost);
    }

    /**
     * Execute the attack with only the attacker stats. Logs an error as more arguments are required
     * for this method to be functional.
     *
     * @param attackerStats combat stats of the attacker.
     */
    @Override
    public void execute(CombatStatsComponent attackerStats) {
        logger.error("Attack move needs more arguments.");
    }

    /**
     * Execute the attack move, applying damage to the target.
     *
     * @param attackerStats combat stats of the attacker.
     * @param targetStats   combat stats of the target.
     */
    @Override
    public void execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats) {
        execute(attackerStats, targetStats, false);
    }

    /**
     * Execute the attack move, applying damage to the target while accounting for whether
     * the target is guarded.
     *
     * @param attackerStats   combat stats of the attacker.
     * @param targetStats     combat stats of the target.
     * @param targetIsGuarded true if the target is guarding, reducing the damage inflicted.
     */
    @Override
    public void execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats, boolean targetIsGuarded) {
        execute(attackerStats, targetStats, targetIsGuarded, 1);
    }

    /**
     * Execute the attack move, applying damage to the target with the option of multiple hits and
     * adjusting damage for guarded targets.
     *
     * @param attackerStats   combat stats of the attacker.
     * @param targetStats     combat stats of the target.
     * @param targetIsGuarded true if the target is guarding, reducing the damage inflicted.
     * @param numHitsLanded   the number of hits that successfully land during a multi-hit attack.
     */
    @Override
    public void execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats, boolean targetIsGuarded,
                        int numHitsLanded) {
        for (int hitNumber = 0; hitNumber < numHitsLanded; hitNumber++) {
            if (attackerStats != null && targetStats != null) {
                int damage = calculateDamage(attackerStats, targetStats, targetIsGuarded, hitNumber);
                logger.info("{} attacks for {} damage.",
                        attackerStats.isPlayer() ? "PLAYER" : "ENEMY",
                        damage);
                targetStats.setHealth(targetStats.getHealth() - damage);
                attackerStats.addStamina(-(this.getStaminaCost()));
            } else {
                logger.error("Either attacker or target does not have CombatStatsComponent.");
            }
        }
    }

    /**
     * Calculates the damage to be inflicted on the target based on the attacker’s and target's stats.
     * The formula is: damage = floor(m1 * m2 * m3 * m4 * ( (((Lvl+2) * (At / Df)) / 2) + 2)
     *
     * @param attackerStats   combat stats of the attacker.
     * @param targetStats     combat stats of the target.
     * @param targetIsGuarded true if the target is guarding, reducing the damage inflicted.
     * @param hitNumber       the number of hits landed in case of multi-hit attacks.
     * @return the damage to be inflicted on the target.
     */
    private int calculateDamage(
            CombatStatsComponent attackerStats, CombatStatsComponent targetStats, boolean targetIsGuarded, int hitNumber
    ) {
        int damage;

        double m1 = calculateStatusMultiplier();
        double m2 = calculateStaminaMultiplier(attackerStats.getStamina());
        double m3 = calculateGuardMultiplier(targetIsGuarded);
        double m4 = calculateMultiHitMultiplier(hitNumber);
        int L = 1; // Level of the user.
        int A = attackerStats.getStrength(); // user's strength stat
        int D = targetStats.getDefense(); // opponent's defense stat

        double result = m1 * m2 * m3 * m4 * ((((L + 2) * ((double) A / D)) / 2) + 2);
        damage = (int) Math.floor(result);

        return damage;
    }

    /**
     * Calculates the buff/de-buff multiplier based on the user's current status effects.
     * THIS FUNCTION IS NOT YET IMPLEMENTED.
     *
     * @return status effect multiplier.
     */
    private double calculateStatusMultiplier() {
        double multiplier;
        multiplier = 1;
        return multiplier;
    }

    /**
     * Calculates the stamina multiplier based on the user's current stamina.
     *
     * @param userStamina current stamina level (0 - 100).
     * @return stamina multiplier.
     */
    private double calculateStaminaMultiplier(int userStamina) {
        if (userStamina >= 50) {
            return 1;
        } else {
            return Math.exp(((double) userStamina / 50) - 1);
        }
    }

    /**
     * Calculates the guard multiplier based on whether the target is guarding.
     *
     * @param targetIsGuarded true if the target is guarding.
     * @return guard multiplier.
     */
    private double calculateGuardMultiplier(boolean targetIsGuarded) {
        return targetIsGuarded ? 0.5 : 1;
    }

    /**
     * Calculates the multi-hit multiplier based on the hit number in multi-hit attacks.
     * Multiplier = 1 + 0.25N where N is the number of additional hits.
     *
     * @param hitNumber hit number 1..4 in a multi-hit attack.
     * @return multi-hit multiplier.
     */
    private double calculateMultiHitMultiplier(int hitNumber) {
        return 1 + (0.25 * (hitNumber - 1));
    }
}
