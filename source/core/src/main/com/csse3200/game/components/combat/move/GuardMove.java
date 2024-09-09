package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.CombatStatsComponent;

public class GuardMove extends CombatMove {
    public GuardMove(String moveName, int staminaCost) {
        super(moveName, staminaCost);
    }

    @Override
    public void execute(CombatStatsComponent attackerStats) {
        attackerStats.addStamina(-(this.getStaminaCost()));
    }

    @Override
    public void execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats) {
        execute(attackerStats);
    }

    @Override
    public void execute(CombatStatsComponent attackerStats, CombatStatsComponent targetStats, boolean targetIsGuarded) {
        execute(attackerStats);
    }
}
