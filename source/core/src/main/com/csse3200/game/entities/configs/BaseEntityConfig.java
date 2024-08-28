package com.csse3200.game.entities.configs;

/**
 * Defines a basic set of properties stored in entities config files to be loaded by Entity Factories.
 */
public class BaseEntityConfig {
    public int health = 1; // Remain public as per the base game
    public int baseAttack = 0; // Remain public as per the base game
    protected String animalName;
    protected String[] baseHint;
    protected String spritePath;
    protected float animationSpeed = 0.1f;
    protected String[] soundPath;
    public int hunger = 100;
    public int strength = 0;
    public int defense = 0;
    public int speed = 1;
    public int experience = 0;

    protected BaseEntityConfig() {}

    /**
     * Gets the path to the sprite image used for rendering the entity.
     *
     * @return the sprite path as a String.
     */
    public String getSpritePath() {
        return this.spritePath;
    }

    /**
     * Gets the speed at which the entity's animation plays.
     *
     * @return the animation speed as a float.
     */
    public float getAnimationSpeed() {
        return this.animationSpeed;
    }

    /**
     * Gets the array of file paths to sound effects associated with the entity.
     *
     * @return an array of sound paths as String[].
     */
    public String[] getSoundPath() {
        return this.soundPath;
    }

    /**
     * Gets the health of the entity.
     *
     * @return the health as an int.
     */
    public int getHealth() {
        return this.health;
    }

    /**
     * Gets the name of the animal or entity.
     *
     * @return the animal name as a String.
     */
    public String getAnimalName() {
        return animalName;
    }

    /**
     * Gets the base attack value of the entity.
     *
     * @return the base attack value as an int.
     */
    public int getBaseAttack () {
        return this.baseAttack;
    }

    /**
     * Gets the array of hints related to the entity.
     *
     * @return an array of hints as String[].
     */
    public String[] getBaseHint() {
        return baseHint;
    }

}
