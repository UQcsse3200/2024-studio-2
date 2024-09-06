package com.csse3200.game.components.combat;

import com.csse3200.game.entities.Entity;

public class GuardMove extends CombatMove {
    public GuardMove(String moveName, int staminaCost) {
        super(moveName, staminaCost);
    }

    @Override
    public void execute(Entity attacker, Entity target) {
    }
}
