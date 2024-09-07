package com.csse3200.game.components.combat.move;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AttackMove extends CombatMove {
    private static final Logger logger = LoggerFactory.getLogger(AttackMove.class);

    public AttackMove(String moveName, int staminaCost) {
        super(moveName, staminaCost);
    }

    @Override
    public void execute(Entity attacker, Entity target) {
        CombatStatsComponent attackerStats = attacker.getComponent(CombatStatsComponent.class);
        CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);

        if (attackerStats != null && targetStats != null) {
            /*
            The formula for determining the damage of an attack is as follows:
                damage = ((m1*m2)/m3) * (A/D)
            */
            int m1 = 1; // damage multiplier for user's active buffs or debuffs (1.0 by default)
            int m2 = 1; // damage multiplier for user's fatigue
            int m3 = 1; // defense multiplier for opponent's move (0.35 for guard, 0.25 for counter, 0.2 otherwise)
            int A = attackerStats.getStrength(); // user's strength stat
            int D = targetStats.getDefense(); // opponent's defense stat

            int damage = ((m1*m2)/m3) * (A/D);

            targetStats.setHealth(targetStats.getHealth() - damage);

            logger.info("{} uses {} on {} dealing {} damage.", attacker, moveName, target, damage);
        } else {
            logger.error("Either attacker or target does not have CombatStatsComponent.");
        }
    }
}
