package com.csse3200.game.components.quests;


/** Handles the tasks for each quest*/
public class Task {
    /** The name of the task.*/
    private final String taskName;
    /** A description of what the task involves. */
    private final String description;
    /** A hint  related to completing the task. */
    private final String hint;
    /** The number of triggers required to complete task.*/
    private final int requiredTriggers;
    /** Number of times a task has been triggered. */
    private int triggerCount;
    /** True if the task is completed. */
    private boolean completed;
    /** True if the task has failed. */
    private boolean failed = false;

    /**
     * Constructs a new Task with specific details.
     * @param taskName          The name of the task.
     * @param description       A description of what the task involves.
     * @param hint              A hint  related to completing the task.
     * @param requiredTriggers  The number of triggers required to complete task.
     */

    public Task(String taskName, String description, String hint, int requiredTriggers,
                int triggerCount, boolean completed, boolean failed) {
        this.taskName = taskName;
        this.description = description;
        this.hint = hint;
        this.requiredTriggers = requiredTriggers;
        this.triggerCount = triggerCount;
        this.completed = completed;
        this.failed = failed;
    }

    /** Returns the task name. */

    public String getTaskName() {
        return taskName;
    }

    /** Returns the description for tasks. */

    public String getDescription() {
        return description;
    }

    /** Returns the hint for tasks. */
    public String getHint() {
        return hint;
    }

    /** Checks if quest is completed. */
    public boolean isCompleted() {
        return completed;
    }

    /** Returns how many times task is triggered. */
    public int getTriggerCount() {
        return triggerCount;
    }

    /** Returns the number of triggers required to complete this task. */
    public int getRequiredTriggers() {
        return requiredTriggers;
    }

    /** Checks if task is failed.  */
    public boolean isFailed() {
        return failed;
    }

    /** Marks the task as failed. */
    public void failTask() {
        this.failed = true;
    }

    /** Handles an event related to the task. */
    public void handleEvent() {
        if (!completed && !failed) {
            triggerCount++;
            if (triggerCount >= requiredTriggers) {
                completed = true;
            }
        }
    }



}
