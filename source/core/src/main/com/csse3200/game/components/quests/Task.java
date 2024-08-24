package com.csse3200.game.components.quests;

public class Task {
    private final String taskName;
    private final String description;
    private final String hint;
    private final int requiredTriggers;
    private int triggerCount;
    private boolean completed;
    private boolean failed = false;

    public Task(String taskName, String description, String hint, int requiredTriggers) {
        this.taskName = taskName;
        this.description = description;
        this.hint = hint;
        this.requiredTriggers = requiredTriggers;
        this.triggerCount = 0;
        this.completed = false;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getDescription() {
        return description;
    }

    public String getHint() {
        return hint;
    }

    public boolean isCompleted() {
        return completed;
    }

    public int getTriggerCount() {
        return triggerCount;
    }

    public int getRequiredTriggers() {
        return requiredTriggers;
    }

    public boolean isFailed() {
        return failed;
    }
    public void failTask() {
        this.failed = true;
    }

    public void handleEvent() {
        if (!completed && !failed) {
            triggerCount++;
            if (triggerCount >= requiredTriggers) {
                completed = true;
            }
        }
    }



}
