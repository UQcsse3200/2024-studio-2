package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.CombatStatsComponent;

public class SleepMove extends CombatMove {
    public SleepMove(String moveName, int staminaCost) {
        super(moveName, staminaCost);
    }

    @Override
    public void execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats) {
    }
}
