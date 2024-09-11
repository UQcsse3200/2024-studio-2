package com.csse3200.game.components.stats;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.components.quests.Achievement;

/** A simple class to hold values for stats to be tracked through the game.
 *
 */
public class Stat implements Json.Serializable {

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
    private int max;

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
     * Constructor for stat with no max value e.g. snake high score
     */
    public Stat(String statName, String description) {
        this.statName = statName;
        this.statDescription = description;
        this.hasMax = false;
        this.current = 0;
    }
    /**
     * Constructor for stat with a max value e.g. number of bosses killed
     */
    public Stat(String statName, String description, int max) {
        this.statName = statName;
        this.statDescription = description;
        this.max = max;
        this.hasMax = true;
        this.current = 0;
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
    public int getMax() {
        if (this.hasMax) {
            return max;
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
        if (this.hasMax()){
            this.current = Math.min((this.current + value), this.max);
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
            this.current = Math.min(this.current + value, this.max);
        } else {
            this.current += value;
        }
    }


    /**
     * Reduce a stat by the given amount to a minimum of 0
     * @param value the amount
     */
    private void subtractValue(int value) {
        this.setCurrent(Math.max((this.current - value), 0));
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
        ITEM,ENEMY,ADVANCEMENT
    }

    public Stat.StatType getType() {
        return this.type;
    }

    /**
     * Perform json read and write actions on the end game stats config file
     *
     * @param json The config containing stats to be tracked
     */
    @Override
    public void write(Json json) {
        json.writeValue("statName", statName);
        json.writeValue("statDescription", statDescription);
        json.writeValue("statCurrent", current);
        json.writeValue("statMax", max);
        json.writeValue("statHasMax", hasMax);
        json.writeValue("type", type.name());
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        this.statName = jsonData.getString("statName");
        this.statDescription = jsonData.getString("statDescription");
        this.current = jsonData.getInt("statCurrent");
        this.max = jsonData.getInt("statMax");
        this.hasMax = jsonData.getBoolean("statHasMax");
        this.type = Stat.StatType.valueOf(jsonData.getString("type"));
    }
    @Override
    public String toString() {
        return "Achievement{" +
                "questName='" + statName + '\'' +
                ", questDescription='" + statDescription + '\'' +
                ", current=" + current +
                ", max=" + max +
                ", hasMax=" + hasMax +
                ", type=" + type +
                '}';
    }
}