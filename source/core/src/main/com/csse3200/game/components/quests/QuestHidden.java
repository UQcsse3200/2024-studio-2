package com.csse3200.game.components.quests;


public class QuestHidden {
    private final String questName;
    private final String questDescription;
    private boolean completed = false;

    public QuestHidden(String questName, String questDescription) {
        this.questName = questName;
        this.questDescription = questDescription;
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



}
