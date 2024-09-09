package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.CombatStatsComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuardMove extends CombatMove {
    private static final Logger logger = LoggerFactory.getLogger(AttackMove.class);

    public GuardMove(String moveName, int staminaCost) {
        super(moveName, staminaCost);
    }

    @Override
    public void execute(CombatStatsComponent attackerStats) {
        if (attackerStats != null) {
            logger.info("Guard Move: {} using {} stamina.", this.getMoveName(), this.getStaminaCost());

            attackerStats.addStamina(-(this.getStaminaCost()));
        } else {
            logger.error("Entity does not have CombatStatsComponent.");
        }
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
