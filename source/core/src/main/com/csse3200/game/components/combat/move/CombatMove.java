package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.CombatStatsComponent;

public abstract class CombatMove {
    protected String moveName;
    protected int staminaCost;

    public CombatMove(String moveName, int staminaCost) {
        this.moveName = moveName;
        this.staminaCost = staminaCost;
    }

    public abstract void execute(CombatStatsComponent attacker);

    public abstract void execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats);

    public abstract void execute(
            CombatStatsComponent attackerStats, CombatStatsComponent targetStats, boolean targetIsGuarded);

    public abstract void execute(
            CombatStatsComponent attackerStats, CombatStatsComponent targetStats, boolean targetIsGuarded,
            int numHitsLanded);

    public String getMoveName() {
        return moveName;
    }

    public int getStaminaCost() {
        return staminaCost;
    }
}
