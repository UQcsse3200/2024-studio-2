package com.csse3200.game.components.combat;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.BaseEnemyEntityConfig;

public class DodgeMove extends CombatMove {
    public DodgeMove(String moveName, int energyCost) {
        super(moveName, 0, energyCost);
    }

    @Override
    public void execute(Entity attacker, Entity target) {
    }
}
