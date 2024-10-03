package com.csse3200.game.components.quests;


import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/**
 * Represents an achievement.
 * An achievement has a name, description, completion status, visibility status, type, and an icon path.
 * Implements the {@link Json.Serializable} interface for serialization and deserialization of achievement data.
 */
public class Achievement {
    private String questName;
    private String questDescription;
    private boolean completed;
    private boolean seen;
    private AchievementType type;
    private String iconPath;

    /**
     * Default constructor for the Achievement class used for json.
     */
    public Achievement(){}


    /**
     * Constructs an Achievement with the specified parameters.
     * @param questName The name of the quest.
     * @param questDescription A brief description of the quest.
     * @param completed Indicates whether the achievement is completed.
     * @param seen Indicates whether the achievement has been seen.
     * @param type The type of achievement.
     * @param iconPath The file path to the icon representing the achievement.
     */
    public Achievement(String questName, String questDescription, Boolean completed, Boolean seen, AchievementType type, String iconPath) {
        this.questName = questName;
        this.questDescription = questDescription;
        this.completed = completed;
        this.seen = seen;
        this.type = type;
        this.iconPath = iconPath;
    }

    /**
     * Getter method for the quest name.
     * @return String representation of the quest's name.
     */
    public String getQuestName() {
        return questName;
    }

    /**
     * Getter method for the quest description.
     * @return String representation of the quest's description
     */
    public String getQuestDescription() {
        return questDescription;
    }

    /**
     * Checks if the current achievement has been completed
     * @return True if it has been completed, False otherwise
     */
    public boolean isCompleted() {
        return this.completed;
    }

    /**
     * Complete the current achievement.
     */
    public void complete() {
        this.completed = true;
    }

    /**
     * Checks if the current achievement has been seen
     * @return True if it has been completed, False otherwise
     */
    public boolean isSeen() {
        return this.seen;
    }

    /**
     * Notes the achievement has been seen
     */
    public void setSeen() {
        this.seen = true;
    }

    /**
     * Gets the type of the achievement.
     * @return The achievement type.
     */
    public AchievementType getType() {
        return this.type;
    }

    /**
     * Gets the icon path for the achievement.
     * @return The file path to the icon representing the achievement.
     */
    public String getPath() {
        return this.iconPath;
    }

    /**
     * Returns a string representation of the achievement.
     * @return A string containing all the achievement properties.
     */
    @Override
    public String toString() {
        return "Achievement{" +
                "questName='" + questName + '\'' +
                ", questDescription='" + questDescription + '\'' +
                ", completed=" + completed +
                ", seen=" + seen +
                ", type=" + type +
                ", iconPath=" + iconPath +
                '}';
    }

    /**
     * Represents the type of achievement.
     */
    public enum AchievementType {
        ITEM, ENEMY, ADVANCEMENT
    }
}
