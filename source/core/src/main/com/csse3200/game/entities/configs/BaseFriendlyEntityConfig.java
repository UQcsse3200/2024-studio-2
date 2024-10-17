package com.csse3200.game.entities.configs;

import java.util.Map;

/**
 * Defines a basic set of properties stored in entities config files to be loaded by Entity Factories.
 */
public class BaseFriendlyEntityConfig extends BaseEntityConfig {
    private int baseAttack = 0;
    private int strength = 0;
    private int level = 1;
    private Boolean isBoss = false;
    private Map<Integer, String[]> hints;
    public int hintLevel = 0;
    public int currentHint = 0;
    protected String animalName = "";
    protected String[][] baseHint;
    protected String spritePath;
    protected String[] soundPath;
    private float itemProbability = 0;
    protected BaseFriendlyEntityConfig() {}

    /**
     * Retrieves the hints associated with the current hint level.
     *
     * @return an array of Strings containing hints for the current level.
     */
    public String[] getStringHintLevel() {
        return hints.get(hintLevel);
    }

    /**
     * Increments the current hint level if more levels are available.
     * Resets the current hint index after incrementing.
     */
    public void incrementHintLevel() {
        if (hints != null && hintLevel < (hints.size() - 1)) {
            hintLevel = hintLevel + 1;
            restartCurrentHint();
        }
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
     * Sets the sprite path for this entity.
     *
     * @param spritePath the path to the sprite image.
     */
    public void setSpritePath(String spritePath) {
        this.spritePath = spritePath;
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
     * Sets the sound paths for this entity.
     *
     * @param soundPath an array of String representing the new sound paths.
     */
    public void setSoundPath(String[] soundPath) {
        this.soundPath = soundPath;
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
    public int getBaseAttack() {
        return this.baseAttack;
    }

    /**
     * Returns the base hint messages for this entity.
     *
     * @return an array of String containing the base hints.
     */
    public String[][] getBaseHint() {
        return baseHint;
    }

    /**
     * Restarts the current hint by resetting the current hint and line indices.
     */
    public void restartCurrentHint() {
        if (hints != null) {
            this.currentHint = 0;
        }
    }

    /**
     * Sets the health of the entity.
     *
     * @param health the new health value to set.
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * Retrieves the current hunger level of the entity.
     *
     * @return the current hunger level.
     */
    public int getHunger() {
        return hunger;
    }

    /**
     * Sets the hunger level of the entity.
     *
     * @param hunger the new hunger level to set.
     */
    public void setHunger(int hunger) {
        this.hunger = hunger;
    }

    /**
     * Sets the base attack value for the entity.
     *
     * @param baseAttack the new base attack value to set.
     */
    public void setBaseAttack(int baseAttack) {
        this.baseAttack = baseAttack;
    }

    /**
     * Retrieves the strength attribute of the entity.
     *
     * @return the current strength value.
     */
    public int getStrength() {
        return strength;
    }

    /**
     * Sets the strength attribute of the entity.
     *
     * @param strength the new strength value to set.
     */
    public void setStrength(int strength) {
        this.strength = strength;
    }

    /**
     * Retrieves the defense attribute of the entity.
     *
     * @return the current defense value.
     */
    public int getDefense() {
        return this.defense;
    }

    /**
     * Sets the defense attribute of the entity.
     *
     * @param defense the new defense value to set.
     */
    public void setDefense(int defense) {
        this.defense = defense;
    }

    /**
     * Retrieves the speed attribute of the entity.
     *
     * @return the current speed value.
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Sets the speed attribute of the entity.
     *
     * @param speed the new speed value to set.
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Retrieves the experience points of the entity.
     *
     * @return the current experience points.
     */
    public int getExperience() {
        return experience;
    }

    /**
     * Sets the experience points for the entity.
     *
     * @param experience the new experience points to set.
     */
    public void setExperience(int experience) {
        this.experience = experience;
    }

    /**
     * Retrieves the item drop probability of the entity.
     *
     * @return the probability of an item drop occurring.
     */
    public float getItemProbability() {
        return itemProbability;
    }

    /**
     * Determines if the entity is a boss.
     *
     * @return {@code true} if the entity is a boss, {@code false} otherwise.
     */
    public Boolean isBoss() {
        return isBoss;
    }

    /**
     * Retrieves the current level of the entity.
     *
     * @return the current level.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Sets the level of the entity.
     *
     * @param level the new level to set.
     */
    public void setLevel(int level) {
        this.level = level;
    }
}
