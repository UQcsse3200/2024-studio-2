package com.csse3200.game.components.combat;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;

/**
 * Models the attributes of both an entity in combat.
 */
public class CombatStatsModel extends Component {
    private int health;
    private int strength;
    private int defense;
    private int speed;
    private int fatigue;
    private int buffs;

    public CombatStatsModel(CombatStatsComponent stats) {
        this.health = stats.getHealth();
        this.strength = stats.getStrength();
        this.defense = stats.getDefense();
        this.speed = stats.getSpeed();
        this.fatigue = 0;
    }

    /**
     * @return the current health of this entity.
     */
    public int getCurrentHealth() {
        return this.health;
    }

    /**
     * @return the current speed of this entity.
     */
    public int getCurrentStrength() {
        return this.strength;
    }

    /**
     * @return the current defense stat of this entity.
     */
    public int getCurrentDefense() {
        return this.defense;
    }

    /**
     * @return the current speed of this entity.
     */
    public int getCurrentSpeed() {
        return this.speed;
    }

    /**
     * @return the current fatigue of this entity.
     */
    public int getCurrentFatigue() {
        return this.fatigue;
    }

    public void attackThisEntity(int attackDamage) {
        this.health -= attackDamage;
    }

    /**
     * Performs an attack on this entity based on the following formula:
     *
     * damage = ((m1 * m2) / m3) * (A / D)
     *
     * where:
     * m1 is a damage multiplier for the attacker's active buffs or de-buffs,
     * m2 is a damage multiplier for the attacker's fatigue,
     * m3 is a defense multiplier for attack-receiver's move (0.35 for guard, 0.25 for counter, 0.2 otherwise),
     * A is the attacker's strength stat, and
     * D is the attack-receiver's defense stat.
     *
     * @param m1
     * @param m2
     * @param m3
     * @param A
     * @param D
     */
    //public void attackThisEntity(int m1, int m2, int m3, int A, int D) {
    //
    //}
}
