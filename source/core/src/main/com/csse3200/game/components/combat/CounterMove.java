package com.csse3200.game.components.combat;

import com.csse3200.game.entities.Entity;

public class CounterMove extends CombatMove {
    public CounterMove(String moveName, int energyCost) {
        super(moveName, 0, energyCost);
    }

    @Override
    public void execute(Entity attacker, Entity target) {
    }
}
