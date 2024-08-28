package com.csse3200.game.components.quests;

import com.csse3200.game.entities.Entity;

import java.util.List;
import java.util.Map;

/** A basic Quest class that stores quest and subtask progression (# of
 * subtasks completed), descriptions and hints. **/
public class QuestBasic extends AbstractQuest {
    /** A basic constructor class for basic quests that covers achievements, hidden quests, dialogue
     *  and completion triggers (messages to send on completion). */
    public QuestBasic(Entity player, String questName, String questDescription, List<Task> tasks, Boolean isSecretQuest, Map<DialogueKey, String[]> dialogue, String[] taskCompletionTriggers) {
        super(player, questName, questDescription, tasks, isSecretQuest, dialogue,taskCompletionTriggers);
    }

    /** A constructor class for a progression quest to construct non-achievement quests w
     * ith no dialogue or completion triggers (messages to send on completion). */
    public QuestBasic(Entity player, String questName, String questDescription, List<Task> tasks) {
        super(player, questName, questDescription, tasks, false, null, null);
    }

}