package com.csse3200.game.components.quests;


import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.components.mainmenu.MainMenuDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Achievement implements Json.Serializable {
    private String questName;
    private String questDescription;
    private boolean completed;
    private boolean seen;
    private AchievementType type;

    public Achievement(){}

    public Achievement(String questName, String questDescription, Boolean completed, Boolean seen, AchievementType type) {
        this.questName = questName;
        this.questDescription = questDescription;
        this.completed = completed;
        this.seen = seen;
        this.type = type;
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

    public enum AchievementType{
        ITEM,ENEMY,ADVANCEMENT
    }

    public AchievementType getType() {
        return this.type;
    }

    @Override
    public void write(Json json) {
        json.writeValue("questName", questName);
        json.writeValue("questDescription", questDescription);
        json.writeValue("completed", completed);
        json.writeValue("seen", seen);
        json.writeValue("type", type.name());
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        this.questName = jsonData.getString("questName");
        this.questDescription = jsonData.getString("questDescription");
        this.completed = jsonData.getBoolean("completed");
        this.seen = jsonData.getBoolean("seen");
        this.type = AchievementType.valueOf(jsonData.getString("type"));
    }
    @Override
    public String toString() {
        return "Achievement{" +
                "questName='" + questName + '\'' +
                ", questDescription='" + questDescription + '\'' +
                ", completed=" + completed +
                ", seen=" + seen +
                ", type=" + type +
                '}';
    }
}
