package com.csse3200.game.components.quests;

import com.badlogic.gdx.utils.Null;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** An abstract Quest class that contains the design for Quest classes that store quest
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
     * questDialogue is a dict that relates
     * DialogueKey(String npcName, Integer ProgressionLevel)
     * to a dialogue map relevant to the npc
     */
    private final Map<DialogueKey,String[]> questDialogue;
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
    private boolean isActive;

    /** Constructor design for implementing subclasses. */
    public AbstractQuest(String questName, String questDescription, List<Task> tasks, Boolean isAchievement,
                         Boolean isSecretQuest, Map<DialogueKey,String[]> dialogue) {
        this.questName = questName;
        this.questDescription = questDescription;
        this.tasks = tasks;
        this.isAchievement = isAchievement;
        this.isSecretQuest = isSecretQuest;
        this.isActive = true;
        this.questDialogue = dialogue;
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
        if (currentTaskIndex == getNumQuestTasks()) {
            return "QUEST NOT COMPLETED";
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
        } if (currentTaskIndex == getNumQuestTasks()) {
            return "QUEST NOT COMPLETED";
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
    public int getNumTasksToComplete() {
        return tasks.size() - currentTaskIndex;
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

    /** Returns the current quest dialogue for the given npc */
    @Null
    public String[] getDialogue(String npcName) {
        if (!npcName.isEmpty() && !questDialogue.isEmpty()) {
            return questDialogue.get(new DialogueKey(npcName, getProgression()));
        }
        return null;
    }
}
