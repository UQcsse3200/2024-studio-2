package com.csse3200.game.entities.configs;

/**
 * Defines a basic set of properties stored in entities config files to be loaded by Entity Factories.
 */
public class BaseEntityConfig {
    protected int health = 100;
    protected int hunger = 100;
    protected int defense = 0;
    protected int speed = 1;
    protected int experience = 10;
    protected boolean isPlayer = false;
    protected boolean isEnemy;
    protected String spritePath;
    protected float animationSpeed = 0.1f;
    protected String[] soundPath;
    protected BaseEntityConfig() {}
    public BaseEntityConfig(boolean isEnemy, String spritePath, String[] soundPath) {
        this.isEnemy = isEnemy;
        this.spritePath = spritePath;
        this.soundPath = soundPath;
        
    }

    /**
     * Returns the path to the sprite image for this entity.
     *
     * @return the sprite path as a String.
     */
    public String getSpritePath() {
        return this.spritePath;
    }

    /**
     * Checks if the entity is friendly.
     *
     * @return true if the entity is friendly (isEnemy == 0), false otherwise.
     */
    public boolean isFriendly() {
        return isEnemy;
    }

    /**
     * Sets the entity's friendly status.
     *
     * @param friendly true if the entity should be friendly (sets isEnemy to 0), false if the entity should be an enemy (sets isEnemy to 1).
     */
    public void setFriendly(boolean friendly) {
        isEnemy = friendly;
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

