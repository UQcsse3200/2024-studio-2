package com.csse3200.game.components.combat;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.BaseEnemyEntityConfig;

public class SpecialMove extends CombatMove {
    private int specialEffect;

    public SpecialMove(String moveName, int specialEffect, int energyCost) {
        super(moveName, 0, energyCost);
        this.specialEffect = specialEffect;
    }

    @Override
    public void execute(Entity attacker, Entity target) {
    }
}
