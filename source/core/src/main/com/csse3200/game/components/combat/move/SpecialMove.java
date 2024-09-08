package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.CombatStatsComponent;

public class SpecialMove extends CombatMove {
    public SpecialMove(String moveName, int staminaCost) {
        super(moveName, staminaCost);
    }

    @Override
    public void execute(CombatStatsComponent attackerStats) {
    }

    @Override
    public void execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats) {
    }

    @Override
    public void execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats, boolean targetIsGuarded) {
    }
}
