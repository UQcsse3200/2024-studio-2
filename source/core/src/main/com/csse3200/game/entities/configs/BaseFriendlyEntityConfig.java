package com.csse3200.game.entities.configs;

import java.util.Map;

/**
 * Defines a basic set of properties stored in entities config files to be loaded by Entity Factories.
 */
public class BaseFriendlyEntityConfig extends BaseEntityConfig {
    private int health = 100;
    private int hunger = 100;
    private int baseAttack = 0;
    private int strength = 0;
    private int speed = 1;
    private int experience = 100;
    private int stamina = 100;

    public final Map<Integer, String[]> hints = null;
    public int hintLevel = 0;
    public int currentHint = 0;
    protected String animalName = "";

    protected String[] baseHint;
    protected String spritePath;
    protected final float animationSpeed = 0.1f;
    protected String[] soundPath;

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
    public String[] getBaseHint() {
        return baseHint;
    }

    /**
     * Sets the base hint messages for this entity.
     *
     * @param baseHint an array of String containing the new base hints.
     */
    public void setBaseHint(String[] baseHint) {
        this.baseHint = baseHint;
    }

    public void restartCurrentHint() {
        if (hints != null) {
            this.currentHint = 0;
        }
    }

    public void setHealth(int health) {
        this.health = health;
    }

    // Getter and setter for hunger
    public int getHunger() {
        return hunger;
    }

    public void setHunger(int hunger) {
        this.hunger = hunger;
    }

    public void setBaseAttack(int baseAttack) {
        this.baseAttack = baseAttack;
    }

    // Getter and setter for strength
    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    // Getter and setter for defense
    public int getDefense() {
        return this.defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    // Getter and setter for speed
    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    // Getter and setter for experience
    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    // Getter and setter for stamina.
    public int getStamina() { return stamina; }

    public void setStamina(int stamina) { this.stamina = stamina; }
}
