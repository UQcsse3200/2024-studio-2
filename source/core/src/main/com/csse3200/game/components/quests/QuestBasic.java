package com.csse3200.game.components.quests;

import java.util.List;

/** A basic Quest class that stores quest and subtask progression (# of
 * subtasks completed), descriptions and hints. **/
public class QuestBasic {

    private final String questName;

    /**A description of the task. */
    private final String questDescription;

    /** taskArray indexing corresponds to number of tests completed
     * and each entry consists of the task to be completed during this step. */
    private final List<Task> tasks;

    private int currentTaskIndex = 0;

    private final boolean isAchievement;

    private final boolean isSecretQuest;

    private boolean isFailed = false;

    public QuestBasic(String questName,String questDescription, List<Task> tasks, Boolean isAchievement, Boolean isSecretQuest) {
        this.questName = questName;
        this.tasks = tasks;
        this.questDescription = questDescription;
        this.isAchievement = isAchievement;
        this.isSecretQuest = isSecretQuest;
    }

    public String getQuestName() {
        return questName;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public boolean isQuestCompleted() {
        return currentTaskIndex >= tasks.size() && tasks.stream().allMatch(Task::isCompleted);
    }

    public int getProgression() {
        return currentTaskIndex;
    }

    /** Returns the description of the current subtask being completed
     * for a test or "QUEST COMPLETED" if the quest is completed.*/
    public String getCurrentTaskDescription() {
        if (isQuestCompleted()) {
            return "QUEST COMPLETED";
        }
        return tasks.get(currentTaskIndex).getDescription();
    }

    /** Returns the hint for the current subtask being completed
     * for a test or "QUEST COMPLETED" if the quest is completed.*/
    public String getCurrentTaskHint() {
        if (isQuestCompleted()) {
            return "QUEST COMPLETED";
        }
        return tasks.get(currentTaskIndex).getHint();
    }

    public void progressQuest() {
        if (!isQuestCompleted() && !isFailed) {
                currentTaskIndex++;
        }
    }

    public boolean isFailed(){
        return this.isFailed;
    }

    public void failQuest(){
        this.isFailed = true;
    }

    /** taskDescriptions indexing corresponds to number of tests completed
     * and each entry consists of a subtask description. */
    private final String[] taskDescriptions = new String[]{"default", "QUEST COMPLETED"};

    /** taskHints indexing corresponds to number of tests completed
     * and each entry consists of a substring hint to be given to NPCs*/
    private final String[] taskHints = new String[]{"default", "QUEST COMPLETED"};


    /** Returns a description of the quest.*/
    public String getQuestDescription(){
        return questDescription;
    }

    /** Returns the number of tasks to be completed for an individual test.*/
    public int getNumTasksCompleted(){
        return (tasks.size() - 1) - currentTaskIndex;
    }

    /** Returns the number of tasks for a quest.*/
    public int getNumQuestTasks(){
        return tasks.size();
    }

    public boolean isAchievement(){
        return isAchievement;
    }

    public boolean isSecret(){
        return isSecretQuest;
    }
}

