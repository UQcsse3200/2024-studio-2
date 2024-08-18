package com.csse3200.game.quests;

public abstract class AbstractQuest {
    private String questDescription;
    /** The number of subtasks completed. */
    private int numTasksCompleted;
    /** The number of subtasks a quest entails. */
    private int numQuestTasks;
    /** taskArray indexing corresponds to number of tests completed
     * and each entry consists of a the task to be completed during this step. */
    private String[] taskArray;
    /** taskDescriptions indexing corresponds to number of tests completed
     * and each entry consists of a subtask description. */
    private String[] taskDescriptions;
    /** taskHints indexing corresponds to number of tests completed
     * and each entry consists of a substring hint to be given to NPCs*/
    private String[] taskHints;
    /** Returns true if a quest has been completed (the end condition
     * of an individual quest). */
    public abstract boolean isQuestCompleted();

    /** Returns a description of the quest.*/
    public abstract String getQuestDescription();


    /** Returns the number of tasks to be completed for an individual test.*/
    public abstract int getNumTasksCompleted();

    /** Updates the quest progression information.
     * @param taskName
     * */
    public abstract void taskCompleted(String taskName);

    /** Returns the number of subtasks for a quest.*/
    public abstract int getNumQuestTasks();

    /** Returns the description of the current subtask being completed
     * for a test or "QUEST COMPLETED" if the quest is completed.*/
    public abstract String getTaskDescriptions();

    /** Returns the hint for the current subtask being completed
     * for a test or "QUEST COMPLETED" if the quest is completed.*/
    public abstract String getTaskHint();
}

