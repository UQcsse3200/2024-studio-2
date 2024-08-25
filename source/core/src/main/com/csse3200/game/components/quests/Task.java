package com.csse3200.game.components.quests;


/**Handles the tasks for each quest*/
public class Task {
    private final String taskName;
    private final String description;
    private final String hint;
    private final int requiredTriggers;
    private int triggerCount;
    private boolean completed;
    private boolean failed = false;

    /**
     * Constructs a new Task with specific details.
     * @param taskName          The name of the task.
     * @param description       A description of what the task involves.
     * @param hint              A hint  related to completing the task.
     * @param requiredTriggers  The number of triggers required to complete task.
     */

    public Task(String taskName, String description, String hint, int requiredTriggers) {
        this.taskName = taskName;
        this.description = description;
        this.hint = hint;
        this.requiredTriggers = requiredTriggers;
        this.triggerCount = 0;
        this.completed = false;
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
