package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.CombatStatsComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The SpecialMove class defines an abstract base for all special combat moves in the game.
 * Special moves generally buff the attacker's stats and may debuff the target's stats.
 * Each special move is responsible for defining specific debuffs and buffs via abstract methods.
 */
public abstract class SpecialMove extends CombatMove {
    private static final Logger logger = LoggerFactory.getLogger(SpecialMove.class);

    /**
     * Constructs a SpecialMove with the specified name and hunger cost.
     *
     * @param moveName    the name of the special move.
     * @param hungerCost the hunger cost required to perform the special move.
     */
    public SpecialMove(String moveName, int hungerCost) {
        super(moveName, hungerCost);
    }

    /**
     * Special moves generally require additional arguments, so this method provides
     * a default error message if invoked with insufficient parameters.
     *
     * @param attackerStats the combat stats of the attacker.
     * @return an array of {@link StatsChange} representing the changes to combat stats
     *         resulting from the move, such as health or hunger adjustments.
     */
    @Override
    public StatsChange[] execute(CombatStatsComponent attackerStats) {
        logger.error("Special move needs more arguments.");
        return new StatsChange[0];
    }

    /**
     * Executes the special move with the attacker and target. By default, this calls
     * {@link #execute(CombatStatsComponent, CombatStatsComponent, boolean)} with the target
     * not being guarded.
     *
     * @param attackerStats the combat stats of the attacker.
     * @param targetStats   the combat stats of the target.
     * @return an array of {@link StatsChange} representing the changes to combat stats
     *         resulting from the move, such as health or hunger adjustments.
     */
    @Override
    public StatsChange[] execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats) {
        return execute(attackerStats, targetStats, false);
    }

    /**
     * Executes the special move, treating the target as unguarded.
     *
     * @param attackerStats   the combat stats of the attacker.
     * @param targetStats     the combat stats of the target.
     * @param targetIsGuarded whether the target is guarded.
     * @param numHitsLanded   the number of hits landed (not used in this implementation).
     * @return an array of {@link StatsChange} representing the changes to combat stats
     *         resulting from the move, such as health or hunger adjustments.
     */
    @Override
    public StatsChange[] execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats, boolean targetIsGuarded,
                        int numHitsLanded) {
        return execute(attackerStats, targetStats, false);
    }

    /**
     * Executes the special move, applying buffs to the attacker and debuffs to the target if unguarded.
     * If the target is guarded, debuffs are not applied.
     *
     * @param attackerStats   the combat stats of the attacker.
     * @param targetStats     the combat stats of the target.
     * @param targetIsGuarded whether the target is guarded.
     * @return an array of {@link StatsChange} representing the changes to combat stats
     *         resulting from the move, such as health or hunger adjustments.
     */
    @Override
    public StatsChange[] execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats, boolean targetIsGuarded) {
        StatsChange[] statsChanges = new StatsChange[1];
        if (attackerStats != null && targetStats != null) {
            logger.info("{} used a special move.",
                    attackerStats.isPlayer() ? "PLAYER" : "ENEMY");

            if (!targetIsGuarded) {
                applyDebuffs(targetStats);
            } else {
                logger.info("{} blocked the special move with guard.",
                        targetStats.isPlayer() ? "PLAYER" : "ENEMY");
            }

            applyBuffs(attackerStats);
            attackerStats.addHunger(-getHungerCost());
            statsChanges[0] = new StatsChange(0, -getHungerCost());
        } else {
            logger.error("Either attacker or target does not have CombatStatsComponent.");
        }
        return statsChanges;
    }

    /**
     * Abstract method to apply debuffs to the target. Each special move must implement this
     * to define specific debuffs to be applied.
     *
     * @param targetStats the combat stats of the target.
     */
    protected abstract void applyDebuffs(CombatStatsComponent targetStats);

    /**
     * Abstract method to apply buffs to the attacker. Each special move must implement this
     * to define specific buffs to be applied.
     *
     * @param attackerStats the combat stats of the attacker.
     */
    protected abstract void applyBuffs(CombatStatsComponent attackerStats);
}
