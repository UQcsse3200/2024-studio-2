package com.csse3200.game.entities.configs;


/**
 * Defines a basic set of properties stored in entities config files to be loaded by Entity Factories.
 */
public class BaseEnemyEntityConfig extends BaseEntityConfig {
    public int health;
    public int hunger;
    public int baseAttack;
    public int strength;
    public int defense;
    public float speed;
    public int experience;

    /**
     * Returns the health value of this entity.
     *
     * @return the health value as an int.
     */
    public int getHealth() {
        return this.health;
    }

    /**
     * Returns the base attack value of this entity.
     *
     * @return the base attack value as an int.
     */
    public int getBaseAttack() { return this.baseAttack; }
}
