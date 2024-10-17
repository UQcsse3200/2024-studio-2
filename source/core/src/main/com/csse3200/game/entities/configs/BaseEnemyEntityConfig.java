package com.csse3200.game.entities.configs;

/**
 * Defines a basic set of properties stored in entities config files to be loaded by Entity Factories.
 */
public class BaseEnemyEntityConfig extends BaseEntityConfig {
    private int baseAttack;
    private int strength;
    private int level;

    /**
     * Returns the health value of this entity.
     *
     * @return the health value as an int.
     */
    public int getHealth() {
        return this.health;
    }

    /**
     * Sets the health value for this entity.
     *
     * @param health the new health value.
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * Returns the hunger value of this entity.
     *
     * @return the hunger value as an int.
     */
    public int getHunger() {
        return this.hunger;
    }

    /**
     * Sets the hunger value for this entity.
     *
     * @param hunger the new hunger value.
     */
    public void setHunger(int hunger) {
        this.hunger = hunger;
    }

    /**
     * Returns the base attack value of this entity.
     *
     * @return the base attack value as an int.
     */
    public int getBaseAttack() {
        return this.baseAttack;
    }

    /**
     * Sets the base attack value for this entity.
     *
     * @param baseAttack the new base attack value.
     */
    public void setBaseAttack(int baseAttack) {
        this.baseAttack = baseAttack;
    }
    
    /**
     * Returns the defense value of this entity.
     *
     * @return the defense value as an int.
     */
    public int getDefense() {
        return this.defense;
    }

    /**
     * Sets the defense value for this entity.
     *
     * @param defense the new defense value.
     */
    public void setDefense(int defense) {
        this.defense = defense;
    }

    /**
     * Returns the speed value of this entity.
     *
     * @return the speed value as an int.
     */
    public int getSpeed() {
        return this.speed;
    }

    /**
     * Sets the speed value for this entity.
     *
     * @param speed the new speed value.
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Returns the experience value of this entity.
     *
     * @return the experience value as an int.
     */
    public int getExperience() {
        return this.experience;
    }

    /**
     * Sets the experience value for this entity.
     *
     * @param experience the new experience value.
     */
    public void setExperience(int experience) {
        this.experience = experience;
    }
    
    /**
     * Returns the level value for this entity.
     *
     * @return the level value as an int.
     */
    public int getLevel() { return level; }

    /**
     * Sets the level value for this entity.
     *
     * @param level the new level value.
     */
    public void setLevel(int level) { this.level = level; }
}
