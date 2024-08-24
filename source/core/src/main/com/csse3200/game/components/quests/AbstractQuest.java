package com.csse3200.game.components.quests;

import java.util.List;

/** A abstract Quest class that contains the design for Quest classes that store quest
 *  and subtask progression (# of subtasks completed), descriptions and hints. **/
public abstract class AbstractQuest {
    /**
     * The name of the quest.
     * */
    protected final String questName;
    /**
     * A description of the task.
     */
    protected final String questDescription;
    /**
     * taskArray indexing corresponds to number of tests completed
     * and each entry consists of the task to be completed during this step.
     */
    protected final List<Task> tasks;
    /**
     * True if quest achievement has been unlocked.
     */
    protected final boolean isAchievement;
    /**
     * True if the quest is hidden (possible xp and levels).
     */
    protected final boolean isSecretQuest;
    /**
     * taskDescriptions indexing corresponds to number of tests completed
     * and each entry consists of a subtask description.
     */
    private final String[] taskDescriptions = new String[]{"default", "QUEST COMPLETED"};
    /**
     * taskHints indexing corresponds to number of tests completed
     * and each entry consists of a substring hint to be given to NPCs
     */
    private final String[] taskHints = new String[]{"default", "QUEST COMPLETED"};
    /**
     * Number of tasks completed for current quest.
     */
    private int currentTaskIndex = 0;
    /**
     * True if quest has failed.
     */
    private boolean isFailed = false;
    /**
     * True if quest is active (not completed or failed).
     */
    private boolean isActive = true;

    /** Constructor design for implementing subclasses. */
    public AbstractQuest(String questName, String questDescription, List<Task> tasks, Boolean isAchievement, Boolean isSecretQuest) {
        this.questName = questName;
        this.questDescription = questDescription;
        this.tasks = tasks;
        this.isAchievement = isAchievement;
        this.isSecretQuest = isSecretQuest;
        this.isActive = true;
    }

    /** Returns quest name. */
    public String getQuestName() {
        return questName;
    }
    /** Returns task array for quest subtasks. */
    public List<Task> getTasks() {
        return tasks;
    }
    /** Returns true if quest is completed. */
    public boolean isQuestCompleted() {
        return currentTaskIndex >= tasks.size() && tasks.stream().allMatch(Task::isCompleted);
    }
    /** Returns number of quest subtasks completed. */
    public int getProgression() {
        return currentTaskIndex;
    }

    /**
     * Returns the description of the current subtask being completed
     * for a test or "QUEST COMPLETED" if the quest is completed.
     */
    public String getCurrentTaskDescription() {
        if (isQuestCompleted()) {
            return "QUEST COMPLETED";
        }
        return tasks.get(currentTaskIndex).getDescription();
    }

    /**
     * Returns the hint for the current subtask being completed
     * for a test or "QUEST COMPLETED" if the quest is completed.
     */
    public String getCurrentTaskHint() {
        if (isQuestCompleted()) {
            return "QUEST COMPLETED";
        }
        return tasks.get(currentTaskIndex).getHint();
    }
    /** Progress (increments) number of quest subtasks completed. */
    public void progressQuest() {
        if (!isQuestCompleted() && !isFailed) {
            currentTaskIndex++;
        }
        if(isQuestCompleted()){
            this.isActive = false;
        }
    }
    /** Returns true if quest is failed. */
    public boolean isFailed() {
        return this.isFailed;
    }
    /** Earmarks quest as having failed.*/
    public void failQuest() {
        this.isActive = false;
        this.isFailed = true;
    }

    /**
     * Returns a description of the quest.
     */
    public String getQuestDescription() {
        return questDescription;
    }

    /**
     * Returns the number of tasks to be completed for an individual test.
     */
    public int getNumTasksCompleted() {
        return (tasks.size() - 1) - currentTaskIndex;
    }

    /**
     * Returns the number of tasks for a quest.
     */
    public int getNumQuestTasks() {
        return tasks.size();
    }
    /** Returns true if quest achievement has been unlocked. */
    public boolean isAchievement() {
        return isAchievement;
    }
    /** Returns true if the quest is secret (e.g. progression, XP, etc). */
    public boolean isSecret() {
        return isSecretQuest;
    }

    /** Returns true if the quest is active */
    public boolean isActive() {
        return isActive;
    }
}
