package com.csse3200.game.components.combat;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.BaseEnemyEntityConfig;

public class AttackMove extends CombatMove {
    public AttackMove(String moveName, int damage, int energyCost) {
        super(moveName, damage, energyCost);
    }

    @Override
    public void execute(Entity attacker, Entity target) {

    }
}
