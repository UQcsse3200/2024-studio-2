package com.csse3200.game.components.stats;

/** A simple class to hold values for stats to be tracked through the game.
 *
 */
//public class Stat implements Json.Serializable {
public class Stat {


    /** The name of the stat, used as the string within the events system
     */
    private String statName;

    /** The description of the stat, this is how the stat will be displayed in menus/logs
     */
    private String statDescription;
    /** the Current value of the stat
     */
    private int current;

    /** The max value of the stat, if it has one
     */
    private Integer statMax;

    /** True if the stat has a max, false otherwise
     */
    private boolean hasMax;
    /** The type of stat
     */
    private StatType type;

    /**
     * Create no arg function for serialisation
     */
    public Stat(){}

    /**
     * Constructor for a stat
     */
    public Stat(String statName, String description, int current, Integer statMax, Boolean hasMax, StatType type) {
        this.statName = statName;
        this.statDescription = description;
        this.current = current;
        this.statMax = statMax;
        this.hasMax = hasMax;
        this.type = type;
    }

    /**
     * Getter for the statName
     * @return string statName
     */
    public String getStatName() {
        return statName;
    }

    /**
     * Getter for the description
     * @return string description
     */
    public String getStatDescription() {
        return statDescription;
    }

    /**
     * Getter for the current stat value
     * @return int current
     */
    public int getCurrent() {
        return current;
    }

    /**
     * Getter for this.max
     *
     * @return the value of max, or -1 if hasMax == false
     */
    public Integer getStatMax() {
        if (this.hasMax) {
            return statMax;
        } else {
            return -1;
        }
    }

    /**
     * Returns whether the stat has a Max value
     * @return boolean hasMax
     */
    public boolean hasMax() {
        return hasMax;
    }

    /** Sets the current value of the stat to the max of the arg or maxValue
     *
     * @param value the new value to set the stat to
     */
    private void setCurrent(int value) {
        if (this.hasMax) {
            this.current = Math.min(value, this.statMax);
        } else {
            this.current = value;
        }
    }


    /**
     * Increase the stat by the given value to a max of max if the stat has one
     * @param value amount to increase the stat by
     */
    private void addValue(int value) {
        if (this.hasMax) {
            this.current = Math.min(this.current + value, this.statMax);
        } else {
            this.current += value;
        }
    }

    /**
     * Reduce a stat by the given amount to a minimum of 0
     * @param value the amount
     */
    private void subtractValue(int value) {
        this.current = Math.max(this.current - value, 0);
    }


    /**
     * Event handling method that is called when an event is triggered
     * @param operation the operation to be used to change the stat
     * @param value the amount to change by
     */
    public void update(String operation, int value) {
        switch (operation) {
            case "set" -> this.setCurrent(value);
            case "add" -> this.addValue(value);
            case "subtract" -> this.subtractValue(value);
        }
    }

    /**
     * Define types for end game stats
     * */
    public enum StatType{
        ITEM,ENEMY,PLAYER,LAND_ENEMY,WATER_ENEMY,AIR_ENEMY,FOOD_ITEM,POTION_ITEM,
        PLAYER_MINIGAME,PLAYER_COMBAT
    }

    public Stat.StatType getType() {
        return this.type;
    }


    /**
     * Convert stats json to a human-readable string
     *
     * @return human-readable stats string
     */
    @Override
    public String toString() {
        return "Stat{" +
                "statName='" + statName + '\'' +
                ", statDescription='" + statDescription + '\'' +
                ", statCurrent=" + current +
                ", statMax=" + statMax +
                ", statHasMax=" + hasMax +
                ", type=" + type +
                '}';
    }
}