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
     * @return an array of {@link StatsChange} representing the changes to combat stats
     *         resulting from the move, such as health or hunger adjustments.
     */
    @Override
    public StatsChange[] execute(CombatStatsComponent attackerStats) {
        StatsChange[] statsChanges = new StatsChange[1];
        if (attackerStats != null) {
            int initialHunger = attackerStats.getHunger();
            int initialHealth = attackerStats.getHealth();
            attackerStats.addHunger((int) (Math.max(0.25 * attackerStats.getMaxHunger(), 1)));
            // Healing from Sleep is disabled when Poisoned
            int healthChange = (attackerStats.hasStatusEffect(CombatStatsComponent.StatusEffect.POISONED)) ?
                    0 : (int) (0.1 * attackerStats.getMaxHealth());
            attackerStats.addHealth(healthChange);
            logger.info("{} sleeps: increased hunger to {} and health to {}.",
                    attackerStats.isPlayer() ? "PLAYER" : "ENEMY",
                    attackerStats.getHunger(),
                    attackerStats.getHealth());
            statsChanges[0] = new StatsChange(Math.min(healthChange, attackerStats.getMaxHealth() - initialHealth),
                    attackerStats.getHunger() - initialHunger);
        } else {
            logger.error("Entity does not have CombatStatsComponent");
        }
        return statsChanges;
    }

    /**
     * Executes the sleep move, ignoring the target's stats since it is a self-healing action.
     *
     * @param attackerStats the combat stats of the entity performing the sleep move.
     * @param targetStats   the combat stats of the target (ignored for sleep moves).
     * @return an array of {@link StatsChange} representing the changes to combat stats
     *         resulting from the move, such as health or hunger adjustments.
     */
    @Override
    public StatsChange[] execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats) {
        return execute(attackerStats);
    }

    /**
     * Executes the sleep move, ignoring whether the target is guarded or not since it is a self-healing action.
     *
     * @param attackerStats   the combat stats of the entity performing the sleep move.
     * @param targetStats     the combat stats of the target (ignored for sleep moves).
     * @param targetIsGuarded whether the target is guarding (ignored for sleep moves).
     * @return an array of {@link StatsChange} representing the changes to combat stats
     *         resulting from the move, such as health or hunger adjustments.
     */
    @Override
    public StatsChange[] execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats, boolean targetIsGuarded) {
        return execute(attackerStats);
    }

    /**
     * Executes the sleep move, ignoring the number of hits landed since it is a self-healing action.
     *
     * @param attackerStats   the combat stats of the entity performing the sleep move.
     * @param targetStats     the combat stats of the target (ignored for sleep moves).
     * @param targetIsGuarded whether the target is guarding (ignored for sleep moves).
     * @param numHitsLanded   the number of hits landed (ignored for sleep moves).
     * @return an array of {@link StatsChange} representing the changes to combat stats
     *         resulting from the move, such as health or hunger adjustments.
     */
    @Override
    public StatsChange[] execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats, boolean targetIsGuarded,
                        int numHitsLanded) {
        return execute(attackerStats);
    }
}
