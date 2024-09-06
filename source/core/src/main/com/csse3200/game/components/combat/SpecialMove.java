package com.csse3200.game.components.combat;

import com.csse3200.game.entities.Entity;

public class SpecialMove extends CombatMove {
    public SpecialMove(String moveName, int staminaCost) {
        super(moveName, staminaCost);
    }

    @Override
    public void execute(Entity attacker, Entity target) {
    }
}
