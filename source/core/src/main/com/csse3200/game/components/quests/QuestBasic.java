package com.csse3200.game.components.quests;

import java.util.List;

/** A basic Quest class that stores quest and subtask progression (# of
 * subtasks completed), descriptions and hints. **/
public class QuestBasic extends AbstractQuest {

    public QuestBasic(String questName,String questDescription, List<Task> tasks, Boolean isAchievement, Boolean isSecretQuest) {
        super(questName, questDescription, tasks, isAchievement, isSecretQuest);
    }


}

