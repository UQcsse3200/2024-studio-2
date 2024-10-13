package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.CombatStatsComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The SleepMove class represents a defensive move where the entity takes a rest, restoring a percentage of
 * both hunger and health. This move is generally used to recover and does not target or attack opponents.
 */
public class SleepMove extends CombatMove {
    private static final Logger logger = LoggerFactory.getLogger(SleepMove.class);

    /**
     * Constructor to initialize the SleepMove with its name and hunger cost.
     *
     * @param moveName    the name of the sleep move.
     * @param hungerCost the hunger cost required to execute the sleep move.
     */
    public SleepMove(String moveName, int hungerCost) {
        super(moveName, hungerCost);
    }

    /**
     * Executes the sleep move, restoring 25% of the user's maximum hunger and 10% of their maximum health.
     * Health will not be restored if the Poisoned status effect is applied.
     *
     * @param attackerStats the combat stats of the entity performing the sleep move.
     */
    @Override
    public void execute(CombatStatsComponent attackerStats) {
        if (attackerStats != null) {
            attackerStats.addHunger((int) (0.25 * attackerStats.getMaxHunger()));
            if (!attackerStats.hasStatusEffect(CombatStatsComponent.StatusEffect.POISONED)) {
                attackerStats.addHealth((int) (0.1 * attackerStats.getMaxHealth()));
            }
            logger.info("{} sleeps: increased hunger to {} and health to {}.",
                    attackerStats.isPlayer() ? "PLAYER" : "ENEMY",
                    attackerStats.getHunger(),
                    attackerStats.getHealth());
        } else {
            logger.error("Entity does not have CombatStatsComponent");
        }
    }

    /**
     * Executes the sleep move, ignoring the target's stats since it is a self-healing action.
     *
     * @param attackerStats the combat stats of the entity performing the sleep move.
     * @param targetStats   the combat stats of the target (ignored for sleep moves).
     */
    @Override
    public void execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats) {
        execute(attackerStats);
    }

    /**
     * Executes the sleep move, ignoring whether the target is guarded or not since it is a self-healing action.
     *
     * @param attackerStats   the combat stats of the entity performing the sleep move.
     * @param targetStats     the combat stats of the target (ignored for sleep moves).
     * @param targetIsGuarded whether the target is guarding (ignored for sleep moves).
     */
    @Override
    public void execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats, boolean targetIsGuarded) {
        execute(attackerStats);
    }

    /**
     * Executes the sleep move, ignoring the number of hits landed since it is a self-healing action.
     *
     * @param attackerStats   the combat stats of the entity performing the sleep move.
     * @param targetStats     the combat stats of the target (ignored for sleep moves).
     * @param targetIsGuarded whether the target is guarding (ignored for sleep moves).
     * @param numHitsLanded   the number of hits landed (ignored for sleep moves).
     */
    @Override
    public void execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats, boolean targetIsGuarded,
                        int numHitsLanded) {
        execute(attackerStats);
    }
}
