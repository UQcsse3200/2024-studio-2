package com.csse3200.game.entities.configs;

import java.util.Map;

/**
 * Defines a basic set of properties stored in entities config files to be loaded by Entity Factories.
 */
public class BaseFriendlyEntityConfig extends BaseEntityConfig {
    public int health = 100;
    public int hunger = 100;
    public int baseAttack = 0;
    public int strength = 0;
    public int defense = 0;
    public int speed = 1;
    public int experience = 100;
    public int baseDefense = 0;
    public String animalName = "";
    public int isEnemy = 0;

    public Map<Integer, String[]> hints = null;
    public int hintLevel = 0;
    public int currentHint = 0;

    protected String[] baseHint;
    protected String spritePath;
    protected float animationSpeed = 0.1f;
    protected String[] soundPath;
    protected BaseFriendlyEntityConfig() {}
    public String[] getStringHintLevel() {
        return hints.get(hintLevel);
    }

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

    public void restartCurrentHint() {
        if (hints != null) {
            this.currentHint = 0;
        }
    }
}
