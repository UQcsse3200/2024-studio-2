package com.csse3200.game.entities.configs;

/**
 * Defines a basic set of properties stored in entities config files to be loaded by Entity Factories.
 */
public class BaseEntityConfig {
    public int health = 100;
    public int hunger = 100;
    public int baseAttack = 0;
    public int strength = 0;
    public int defense = 0;
    public int speed = 1;
    public int experience = 10;
    public boolean isPlayer = false;
    public int baseDefense = 0;
    public String animalName = "";
    public int isEnemy = 0;
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

}
