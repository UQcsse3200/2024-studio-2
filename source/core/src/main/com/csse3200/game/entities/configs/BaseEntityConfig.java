package com.csse3200.game.entities.configs;

/**
 * Defines a basic set of properties stored in entities config files to be loaded by Entity Factories.
 */
public class BaseEntityConfig {
    public int health = 1;
    public int baseAttack = 0;
    public int baseDefense = 0;
    public float speed = 0;
    public int isEnemy = 0;
    public int hunger = 100;
    public int strength = 0;
    public int defense = 0;
    public int experience = 0;
    protected String animalName;
    protected String[] baseHint;
    protected String spritePath;
    protected float animationSpeed = 0.1f;
    protected String[] soundPath;

    protected BaseEntityConfig() {}

    /**
     * Returns the path to the sprite image for this entity.
     *
     * @return the sprite path as a String.
     */
    public String getSpritePath() {
        return this.spritePath;
    }

    /**
     * Returns the animation speed for this entity.
     *
     * @return the animation speed as a float.
     */
    public float getAnimationSpeed() {
        return this.animationSpeed;
    }

    /**
     * Returns the paths to the sound files associated with this entity.
     *
     * @return an array of String representing the sound paths.
     */
    public String[] getSoundPath() {
        return this.soundPath;
    }

    /**
     * Returns the health value of this entity.
     *
     * @return the health value as an int.
     */
    public int getHealth() {
        return this.health;
    }

    /**
     * Returns the name of the animal for this entity.
     *
     * @return the animal name as a String.
     */
    public String getAnimalName() {
        return this.animalName;
    }

    /**
     * Returns the base attack value of this entity.
     *
     * @return the base attack value as an int.
     */
    public int getBaseAttack() { return this.baseAttack; }

    /**
     * Returns the base hint messages for this entity.
     *
     * @return an array of String containing the base hints.
     */
    public String[] getBaseHint() {
        return baseHint;
    }
}
