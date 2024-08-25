package com.csse3200.game.components.quests;


public class QuestHidden extends AbstractQuest {
    /**A description of the task. */
    private final String questName;

    /**
     * A description of the task.
     */
    private final String questDescription;

    private boolean completed = false;

    public QuestHidden(String questName, String questDescription) {
        this.questName = questName;
        this.questDescription = questDescription;
    }

    public String getQuestName() {
        return questName;
    }

    public String getQuestDescription() {
        return questDescription;
    }
    /** Returns the description of the current subtask being completed
     * for a test or "QUEST COMPLETED" if the quest is completed.*/

    /**
     * Returns the hint for the current subtask being completed
     * for a test or "QUEST COMPLETED" if the quest is completed.
     */

    public boolean isCompleted() {
        return this.completed;
    }

    public void complete() {
        this.completed = true;
    }



}
