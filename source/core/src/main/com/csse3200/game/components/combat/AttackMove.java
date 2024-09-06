package com.csse3200.game.components.combat;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AttackMove extends CombatMove {
    private static final Logger logger = LoggerFactory.getLogger(AttackMove.class);

    public AttackMove(String moveName, int damage, int energyCost) {
        super(moveName, damage, energyCost);
    }

    @Override
    public void execute(Entity attacker, Entity target) {
        CombatStatsComponent attackerStats = attacker.getComponent(CombatStatsComponent.class);
        CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);

        if (attackerStats != null && targetStats != null) {
            // Apply the attacker's strength and move damage to the target's health
            int totalDamage = Math.max(0, attackerStats.getStrength() + this.damage - targetStats.getDefense());

            // Subtract health from the target
            targetStats.addHealth(-totalDamage);

            logger.info("{} uses {} on {} dealing {} damage.", attacker, moveName, target, totalDamage);
        } else {
            logger.error("Either attacker or target does not have CombatStatsComponent.");
        }
    }
}
