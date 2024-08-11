package com.csse3200.game.quests;

/** A basic Quest class that stores quest and subtask progression (# of
 * subtasks completed), descriptions and hints. **/
public class QuestBasic{
    /**A description of the task. */
    private final String questDescription = "";
    /** The number of subtasks completed. */
    private int numTasksCompleted=0;
    /** The number of subtasks a quest entails. */
    private final int numQuestTasks=0;
    /** taskDescriptions indexing corresponds to number of tests completed
     * and each entry consists of a subtask description. */
    private final String[] taskDescriptions = new String[]{"QUEST COMPLETED"};
    /** taskHints indexing corresponds to number of tests completed
    * and each entry consists of a substring hint to be given to NPCs*/
    private final String[] taskHints = new String[]{"QUEST COMPLETED"};

    /** Returns true if a quest has been completed (the end condition
    * of an individual quest). */
    public boolean isQuestCompleted(){
        return numTasksCompleted==numQuestTasks;
    }

    /** Returns a description of the quest.*/
    public String getQuestDescription(){
        return questDescription;
    }

    /** Returns the number of tasks to be completed for an individual test.*/
    public int getNumTasksCompleted(){
        return numTasksCompleted;
    }

    /** Updates the quest progression information.*/
    public void taskCompleted(){
        this.numTasksCompleted++;
    }

    /** Returns the number of subtasks for a quest.*/
    public int getNumQuestTasks(){
        return numQuestTasks;
    }

    /** Returns the description of the current subtask being completed
    * for a test or "QUEST COMPLETED" if the quest is completed.*/
    public String getTaskDescriptions(){
        return taskDescriptions[numTasksCompleted];
    }

    /** Returns the hint for the current subtask being completed
    * for a test or "QUEST COMPLETED" if the quest is completed.*/
    public String getTaskHint(){
        return taskHints[numTasksCompleted];
    }
}
