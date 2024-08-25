package com.csse3200.game.components.quests;

import java.util.List;
import java.util.Map;

/** A basic Quest class that stores quest and subtask progression (# of
 * subtasks completed), descriptions and hints. **/
public class QuestBasic extends AbstractQuest {

    public QuestBasic(String questName, String questDescription, List<Task> tasks, Boolean isAchievement, Boolean isSecretQuest, Map<DialogueKey, String[]> dialogue) {
        super(questName, questDescription, tasks, isAchievement, isSecretQuest, dialogue);
    }

}