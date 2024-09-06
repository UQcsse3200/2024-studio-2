package com.csse3200.game.components.combat.move;

import com.csse3200.game.entities.Entity;

public class CounterMove extends CombatMove {
    public CounterMove(String moveName, int staminaCost) {
        super(moveName, staminaCost);
    }

    @Override
    public void execute(Entity attacker, Entity target) {
    }
}
