package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.CombatStatsComponent;

/**
 * Represents an abstract combat move in the game. Concrete implementations of this class
 * define specific combat actions such as attacks, defenses, or special abilities.
 */
public abstract class CombatMove {
    protected String moveName;   // The name of the move.
    protected int hungerCost;    // The hunger required to perform the move.

    /**
     * Constructor to initialize the move name and hunger cost.
     *
     * @param moveName   the name of the move.
     * @param hungerCost the amount of hunger required to execute the move.
     */
    protected CombatMove(String moveName, int hungerCost) {
        this.moveName = moveName;
        this.hungerCost = hungerCost;
    }

    /**
     * Executes the move with only the attacker's combat stats.
     *
     * @param attackerStats the combat stats of the attacker.
     * @return an array of {@link StatsChange} representing the changes to combat stats
     *         resulting from the move, such as health or hunger adjustments.
     */
    public abstract StatsChange[] execute(CombatStatsComponent attackerStats);

    /**
     * Executes the move with both the attacker's and the target's combat stats.
     *
     * @param attackerStats the combat stats of the attacker.
     * @param targetStats   the combat stats of the target.
     * @return an array of {@link StatsChange} representing the changes to combat stats
     *         resulting from the move, such as health or hunger adjustments.
     */
    public abstract StatsChange[] execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats);

    /**
     * Executes the move with both the attacker's and the target's combat stats,
     * taking into account whether the target is guarded.
     *
     * @param attackerStats   the combat stats of the attacker.
     * @param targetStats     the combat stats of the target.
     * @param targetIsGuarded whether the target is guarding, reducing the effectiveness of the attack.
     * @return an array of {@link StatsChange} representing the changes to combat stats
     *         resulting from the move, such as health or hunger adjustments.
     */
    public abstract StatsChange[] execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats, boolean targetIsGuarded);

    /**
     * Executes the move with both the attacker's and the target's combat stats,
     * accounting for whether the target is guarded and the number of hits landed in a multi-hit attack.
     *
     * @param attackerStats   the combat stats of the attacker.
     * @param targetStats     the combat stats of the target.
     * @param targetIsGuarded whether the target is guarding, reducing the effectiveness of the attack.
     * @param numHitsLanded   the number of hits landed during a multi-hit attack.
     * @return an array of {@link StatsChange} representing the changes to combat stats
     *         resulting from the move, such as health or hunger adjustments.
     */
    public abstract StatsChange[] execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats, boolean targetIsGuarded, int numHitsLanded);

    /**
     * Gets the name of the move.
     *
     * @return the name of the move.
     */
    public String getMoveName() {
        return moveName;
    }

    /**
     * Gets the hunger cost required to perform the move.
     *
     * @return the hunger cost.
     */
    public int getHungerCost() {
        return hungerCost;
    }

    /**
     * Represents changes to combat stats resulting from a move.
     */
    public static class StatsChange {
        private int healthChange = 0;
        private int hungerChange = 0;

        /**
         * Constructor to initialize the health and hunger changes.
         *
         * @param healthChange the change in health resulting from the move.
         * @param hungerChange the change in hunger resulting from the move.
         */
        public StatsChange(int healthChange, int hungerChange) {
            this.healthChange = healthChange;
            this.hungerChange = hungerChange;
        }

        /**
         * Gets the change in health.
         *
         * @return the health change.
         */
        public int getHealthChange() {
            return healthChange;
        }

        /**
         * Gets the change in hunger.
         *
         * @return the hunger change.
         */
        public int getHungerChange() {
            return hungerChange;
        }
    }
}
