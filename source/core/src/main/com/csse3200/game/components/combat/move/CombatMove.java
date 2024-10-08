package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.CombatStatsComponent;

/**
 * Represents an abstract combat move in the game. Concrete implementations of this class
 * define specific combat actions such as attacks, defenses, or special abilities.
 */
public abstract class CombatMove {
    protected String moveName;   // The name of the move.
    protected int staminaCost;   // The stamina required to perform the move.

    /**
     * Constructor to initialize the move name and stamina cost.
     *
     * @param moveName    the name of the move.
     * @param staminaCost the amount of stamina required to execute the move.
     */
    protected CombatMove(String moveName, int staminaCost) {
        this.moveName = moveName;
        this.staminaCost = staminaCost;
    }

    /**
     * Executes the move with only the attacker's combat stats.
     *
     * @param attackerStats the combat stats of the attacker.
     */
    public abstract void execute(CombatStatsComponent attackerStats);

    /**
     * Executes the move with both the attacker's and the target's combat stats.
     *
     * @param attackerStats the combat stats of the attacker.
     * @param targetStats   the combat stats of the target.
     */
    public abstract void execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats);

    /**
     * Executes the move with both the attacker's and the target's combat stats,
     * taking into account whether the target is guarded.
     *
     * @param attackerStats   the combat stats of the attacker.
     * @param targetStats     the combat stats of the target.
     * @param targetIsGuarded whether the target is guarding, reducing the effectiveness of the attack.
     */
    public abstract void execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats, boolean targetIsGuarded);

    /**
     * Executes the move with both the attacker's and the target's combat stats,
     * accounting for whether the target is guarded and the number of hits landed in a multi-hit attack.
     *
     * @param attackerStats   the combat stats of the attacker.
     * @param targetStats     the combat stats of the target.
     * @param targetIsGuarded whether the target is guarding, reducing the effectiveness of the attack.
     * @param numHitsLanded   the number of hits landed during a multi-hit attack.
     */
    public abstract void execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats, boolean targetIsGuarded, int numHitsLanded);

    /**
     * Gets the name of the move.
     *
     * @return the name of the move.
     */
    public String getMoveName() {
        return moveName;
    }

    /**
     * Gets the stamina cost required to perform the move.
     *
     * @return the stamina cost.
     */
    public int getStaminaCost() {
        return staminaCost;
    }
}
