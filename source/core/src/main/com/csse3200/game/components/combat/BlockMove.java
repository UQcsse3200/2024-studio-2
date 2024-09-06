package com.csse3200.game.components.combat;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.BaseEnemyEntityConfig;

public class BlockMove extends CombatMove {
    private int blockAmount;

    public BlockMove(String moveName, int blockAmount, int energyCost) {
        super(moveName, 0, energyCost);
        this.blockAmount = blockAmount;
    }

    @Override
    public void execute(Entity attacker, Entity target) {
    }
}
