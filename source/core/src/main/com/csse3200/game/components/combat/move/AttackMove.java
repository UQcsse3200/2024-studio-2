package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.CombatStatsComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AttackMove extends CombatMove {
    private static final Logger logger = LoggerFactory.getLogger(AttackMove.class);

    public AttackMove(String moveName, int staminaCost) {
        super(moveName, staminaCost);
    }

    /**
     * The attacker damages the target with an attack move. Stamina is used in doing so.
     *
     * @param attackerStats combat stats of the attacker.
     * @param targetStats combat stats of the target.
     */
    @Override
    public void execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats) {

        if (attackerStats != null && targetStats != null) {

            int damage = calculateDamage(attackerStats, targetStats);

            targetStats.setHealth(targetStats.getHealth() - damage);

            attackerStats.addStamina(-(this.staminaCost));

            //logger.info("{} uses {} on {} dealing {} damage.", attacker, moveName, target, damage);
        } else {
            logger.error("Either attacker or target does not have CombatStatsComponent.");
        }
    }

    /**
     * Calculates the damage to be inflicted on the target with respect to the attacker's and target's stats.
     * The formula for determining the damage of an attack is as follows:
     * damage = floor(m1 * m2 * m3 * m4 * ( (((Lvl+2) * (At / Df)) / 2) + 2)
     * See wiki - Combat System for more details.
     *
     * @param attackerStats combat stats of the attacker.
     * @param targetStats combat stats of the target.
     * @return the damage to be inflicted on the target.
     */
    private int calculateDamage(CombatStatsComponent attackerStats, CombatStatsComponent targetStats) {
        int damage;

        double m1 = calculateStatusMultiplier();
        double m2 = calculateStaminaMultiplier(attackerStats.getStamina());
        double m3 = calculateGuardMultiplier(false); ///////////////////////////////////////////////////// REFACTOR execute() TO PASS IN boolean targetIsGuarded
        double m4 = calculateMultiHitMultiplier(1);
        int L = 1; // Level of the user.
        int A = attackerStats.getStrength(); // user's strength stat
        int D = targetStats.getDefense(); // opponent's defense stat

        // Have to cast to double to get floating point division.
        double result = m1 * m2 * m3 * m4 * ((((L + 2) * ((double) A / D)) / 2) + 2);
        damage = (int) Math.floor(result);

        return damage;
    }

    /**
     * Calculates the buff/de-buff multiplier used to calculate damage.
     * Dependent on user's current buff/de-buff status effects.
     * THIS FUNCTION IS FOR LATER IMPLEMENTATION AND IS NOT YET FUNCTIONAL.
     *
     * @return status effect multiplier.
     */
    private double calculateStatusMultiplier() {
        double multiplier;
        multiplier = 1;
        return multiplier;
    }

    /**
     * Calculates the stamina multiplier used to calculate damage based off the user's current stamina.
     *
     * @param userStamina current stamina level (0 - 100).
     * @return stamina multiplier.
     */
    private double calculateStaminaMultiplier(int userStamina) {
        double multiplier;

        if (userStamina >= 50) {
            multiplier = 1;
        } else {
            multiplier = Math.exp(((double) userStamina / 50) - 1);
        }

        return multiplier;
    }

    /**
     * Calculates the guard multiplier used to calculate the damage based off the selected move of the opponent.
     *
     * @param targetIsGuarded boolean on whether the target chose 'Guard' as their move.
     * @return guard multiplier.
     */
    private double calculateGuardMultiplier(boolean targetIsGuarded) {
        double multiplier;

        if (!targetIsGuarded) {
            multiplier = 1; // Default for un-guarded.
        } else {
            multiplier = 0.5;
        }

        return multiplier;
    }
    /**
     * Calculates the multi-hit multiplier used to calculate damage.
     * THIS FUNCTION IS FOR LATER IMPLEMENTATION AND IS NOT YET FUNCTIONAL.
     *
     * @param numHitsLanded number of hits landed in a multi-hit attack.
     * @return status effect multiplier.
     */
    private double calculateMultiHitMultiplier(int numHitsLanded) {
        double multiplier;
        multiplier = 1;
        return multiplier;
    }

}
