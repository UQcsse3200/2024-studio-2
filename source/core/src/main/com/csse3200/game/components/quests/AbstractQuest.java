package com.csse3200.game.components.quests;

import java.util.List;

/** A basic Quest class that stores quest and subtask progression (# of
 * subtasks completed), descriptions and hints. **/
public class AbstractQuest {

    private String questName;

    /**
     * A description of the task.
     */
    private String questDescription;




    public String getQuestName() {
        return questName;
    }


    /** Returns the description of the current subtask being completed
     * for a test or "QUEST COMPLETED" if the quest is completed.*/

    /**
     * Returns the hint for the current subtask being completed
     * for a test or "QUEST COMPLETED" if the quest is completed.
     */


    /**
     * Returns a description of the quest.
     */
    public String getQuestDescription() {
        return questDescription;
    }

}
