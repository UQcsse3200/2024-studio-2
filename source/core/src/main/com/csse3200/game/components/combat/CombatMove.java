package com.csse3200.game.components.combat;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.BaseEnemyEntityConfig;

public abstract class CombatMove {
    protected String moveName;
    protected int damage;
    protected int energyCost;

    public CombatMove(String moveName, int damage, int energyCost) {
        this.moveName = moveName;
        this.damage = damage;
        this.energyCost = energyCost;
    }

    public abstract void execute(Entity attacker, Entity target);

    public String getMoveName() {
        return moveName;
    }

    public int getDamage() {
        return damage;
    }

    public int getEnergyCost() {
        return energyCost;
    }
}
