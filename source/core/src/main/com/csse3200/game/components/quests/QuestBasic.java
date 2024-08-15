package com.csse3200.game.components.quests;

import java.util.List;

/** A basic Quest class that stores quest and subtask progression (# of
 * subtasks completed), descriptions and hints. **/
public class QuestBasic {

    private final String questName;

    private final List<Task> tasks;

    private int currentTaskIndex = 0;

    public QuestBasic(String questName, List<Task> tasks) {
        this.questName = questName;
        this.tasks = tasks;
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

    public String getCurrentTaskDescription() {
        if (isQuestCompleted()) {
            return "QUEST COMPLETED";
        }
        return tasks.get(currentTaskIndex).getDescription();
    }

    public String getCurrentTaskHint() {
        if (isQuestCompleted()) {
            return "QUEST COMPLETED";
        }
        return tasks.get(currentTaskIndex).getHint();
    }

    public void progressQuest() {
        if (!isQuestCompleted()) {
                currentTaskIndex++;
        }
    }
}

