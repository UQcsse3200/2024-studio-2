package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.stats.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents an attack move in combat. This class handles the execution of the attack and the
 * damage calculation based on various factors such as hunger, status effects, and the target's defense.
 */
public class AttackMove extends CombatMove {
    private static final Logger logger = LoggerFactory.getLogger(AttackMove.class);

    /**
     * Constructor for the AttackMove.
     *
     * @param moveName    the name of the move.
     * @param hungerCost the amount of hunger required to perform the move.
     */
    public AttackMove(String moveName, int hungerCost) {
        super(moveName, hungerCost);
    }

    /**
     * Execute the attack with only the attacker stats. Logs an error as more arguments are required
     * for this method to be functional.
     *
     * @param attackerStats combat stats of the attacker.
     */
    @Override
    public StatsChange[] execute(CombatStatsComponent attackerStats) {
        logger.error("Attack move needs more arguments.");
        return new StatsChange[0];
    }

    /**
     * Execute the attack move, applying damage to the target.
     *
     * @param attackerStats combat stats of the attacker.
     * @param targetStats   combat stats of the target.
     */
    @Override
    public StatsChange[] execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats) {
        return execute(attackerStats, targetStats, false);
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
    public StatsChange[] execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats, boolean targetIsGuarded) {
        return execute(attackerStats, targetStats, targetIsGuarded, 1);
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
    public StatsChange[] execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats, boolean targetIsGuarded,
                        int numHitsLanded) {
        StatsChange[] statsChanges = new StatsChange[numHitsLanded];
        for (int hitNumber = 0; hitNumber < numHitsLanded; hitNumber++) {
            if (attackerStats != null && targetStats != null) {
                int damage = calculateDamage(attackerStats, targetStats, targetIsGuarded, hitNumber);
                logger.info("{} attacks for {} damage.",
                        attackerStats.isPlayer() ? "PLAYER" : "ENEMY",
                        damage);
                targetStats.setHealth(targetStats.getHealth() - damage);
                // For multi-hit attacks, only consume hunger once
                int hungerChange = (hitNumber == 0) ? -this.getHungerCost() : 0;
                attackerStats.addHunger(hungerChange);
                statsChanges[hitNumber] = new StatsChange(-damage, hungerChange);
            } else {
                logger.error("Either attacker or target does not have CombatStatsComponent.");
            }
        }
        return statsChanges;
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

        double m1 = calculateStatusMultiplier(targetStats.hasStatusEffect(CombatStatsComponent.StatusEffect.SHOCKED));
        double m2 = calculateHungerMultiplier(attackerStats.getHunger());
        double m3 = calculateGuardMultiplier(targetIsGuarded,
                targetStats.hasStatusEffect(CombatStatsComponent.StatusEffect.BLEEDING));
        double m4 = calculateMultiHitMultiplier(hitNumber);
        int level = 1; // Level of the user.
        int strength = attackerStats.getStrength(); // user's strength stat
        int defense = targetStats.getDefense(); // opponent's defense stat

        double result = m1 * m2 * m3 * m4 * ((((level + 2) * ((double) strength / defense)) / 2) + 2);
        damage = (int) Math.floor(result);

        return damage;
    }

    /**
     * Calculates the buff/de-buff multiplier based on the user's current status effects.
     *
     * @param targetIsShocked true if the Shocked status effect is applied (Attacks are de-buffed by 30%)
     * @return status effect multiplier.
     */
    private double calculateStatusMultiplier(boolean targetIsShocked) {
        if (targetIsShocked) {
            return 0.7;
        }
        return 1;
    }

    /**
     * Calculates the hunger multiplier based on the user's current hunger.
     *
     * @param userHunger current hunger level (0 - 100).
     * @return hunger multiplier.
     */
    private double calculateHungerMultiplier(int userHunger) {
        if (userHunger >= 50) {
            return 1;
        } else {
            return Math.exp(((double) userHunger / 50) - 1);
        }
    }

    /**
     * Calculates the guard multiplier based on whether the target is guarding.
     *
     * @param targetIsGuarded true if the target is guarding.
     * @param targetIsBleeding true if Bleeding status effect is applied (Guard will block 30% damage instead of 50%)
     * @return guard multiplier.
     */
    private double calculateGuardMultiplier(boolean targetIsGuarded, boolean targetIsBleeding) {
        if (targetIsGuarded) {
            if (targetIsBleeding) {
                return 0.7;
            }
            return 0.5;
        }
        return 1;
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
