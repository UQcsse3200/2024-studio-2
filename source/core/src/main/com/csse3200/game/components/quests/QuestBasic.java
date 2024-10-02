package com.csse3200.game.components.quests;

import java.util.List;

/** A basic Quest class that stores quest and subtask progression (# of
 * subtasks completed), descriptions and hints. **/
public class QuestBasic extends AbstractQuest {
    /** A basic constructor class for basic quests that covers achievements, hidden quests, dialogue
     *  and completion triggers (messages to send on completion). */
    public QuestBasic(String questName, String questDescription, List<Task> tasks,
                      Boolean isSecretQuest, List<DialogueKey> dialogue,
                      String[] taskCompletionTriggers, boolean active, boolean failed,
                      int currentTaskIndex, String[] followQuests) {
        super(questName, questDescription, tasks, isSecretQuest, dialogue,
                taskCompletionTriggers, active, failed, currentTaskIndex, followQuests);
    }
}