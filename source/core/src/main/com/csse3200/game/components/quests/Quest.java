package com.csse3200.game.components.quests;

import com.csse3200.game.entities.Entity;

import java.util.List;

/** A basic Quest class that stores quest and subtask progression (# of
 * subtasks completed), descriptions and hints. **/
public class Quest {
    /** A basic constructor class for basic quests that covers achievements, hidden quests, dialogue
     *  and completion triggers (messages to send on completion). */
    /**
     * The name of the quest.
     * */
    private final String questName;
    /**
     * A description of the task.
     */
    private final String questDescription;
    /**
     * taskArray indexing corresponds to number of tests completed
     * and each entry consists of the task to be completed during this step.
     */
    private final List<Task> tasks;
    /**
     * questDialogue is a dict that relates
     * DialogueKey(String npcName, Integer ProgressionLevel)
     * to a dialogue map relevant to the npc
     */
    private List<DialogueKey> questDialogue;
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
    /** Triggers for task completion. */
    private final String[] taskCompletionTriggers;

    private String[] followQuests;

    /** Constructor design for implementing subclasses. */
    Quest(String questName, String questDescription, List<Task> tasks,
                 List<DialogueKey> dialogue, String[] taskCompletionTriggers, boolean active, boolean failed,
                 int currentTaskIndex, String[] followQuests)
    {
        this.questName = questName;
        this.questDescription = questDescription;
        this.tasks = tasks;
        this.isActive = active;
        this.isFailed = failed;
        this.currentTaskIndex = currentTaskIndex;
        this.questDialogue = dialogue;
        this.taskCompletionTriggers = taskCompletionTriggers;
        this.followQuests = followQuests;

    }

    /** Returns quest name. */
    public String getQuestName() {
        return questName;
    }
    /** Returns task array for quest subtasks. */
    public List<Task> getTasks() {
        return this.tasks;
    }

    /** Returns true if quest is completed. */
    public boolean isQuestCompleted() {
        return currentTaskIndex >= tasks.size();
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
        }
        if (currentTaskIndex == getNumQuestTasks()) {
            return "QUEST NOT COMPLETED";
        }
        return tasks.get(currentTaskIndex).getHint();
    }

    /** Progress (increments) number of quest subtasks completed. */
    public boolean progressQuest(Entity player) {
        boolean questCompletionTrack = false;
        if (!isQuestCompleted() && !isFailed) {
            if(taskCompletionTriggers.length != 0){
                player.getEvents().trigger(taskCompletionTriggers[currentTaskIndex]);
            }
            currentTaskIndex++;
        }
        if(isQuestCompleted()){
            if(this.isActive) {
                questCompletionTrack = true;
            }
            this.isActive = false;
            if(taskCompletionTriggers.length != 0){
                player.getEvents().trigger(taskCompletionTriggers[taskCompletionTriggers.length - 1]);
            }
        }
        return questCompletionTrack;
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

    /** Returns true if the quest is active */
    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) { this.isActive = active; }

    public List<DialogueKey> getQuestDialogue() { return questDialogue; }

    public String[] getFollowQuests() { return followQuests; }
}
