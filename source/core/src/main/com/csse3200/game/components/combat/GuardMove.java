package com.csse3200.game.components.combat;

import com.csse3200.game.entities.Entity;

public class GuardMove extends CombatMove {
    private int guardAmount;

    public GuardMove(String moveName, int guardAmount, int energyCost) {
        super(moveName, 0, energyCost);
        this.guardAmount = guardAmount;
    }

    @Override
    public void execute(Entity attacker, Entity target) {
    }
}
