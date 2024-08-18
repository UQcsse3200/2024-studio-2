package com.csse3200.game.quests;


public class questHidden extends AbstractQuest {
    /**A description of the task. */
    private final String questDescription = "BASIC QUEST";
    /** The number of subtasks completed. */
    private int numTasksCompleted=0;
    /** The number of subtasks a quest entails. */
    private final int numQuestTasks=1;
    /** taskArray indexing corresponds to number of tests completed
     * and each entry consists of the task to be completed during this step. */
    private final String[] taskArray = new String[]{"steps", "QUEST COMPLETED"};
    /** taskDescriptions indexing corresponds to number of tests completed
     * and each entry consists of a subtask description. */
    private final String[] taskDescriptions = new String[]{"default", "QUEST COMPLETED"};
    /** taskHints indexing corresponds to number of tests completed
     * and each entry consists of a substring hint to be given to NPCs*/
    private final String[] taskHints = new String[]{"default", "QUEST COMPLETED"};

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

    /** Updates the quest progression information.
     * @param taskName
     * */
    public void taskCompleted(String taskName){
        if (numTasksCompleted < numQuestTasks &&
                this.taskArray[this.numTasksCompleted].equals(taskName)) {
            this.numTasksCompleted++;
        }
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
