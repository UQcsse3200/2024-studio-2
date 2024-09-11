package com.csse3200.game.gamestate.data;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.components.quests.DialogueKey;
import com.csse3200.game.components.quests.QuestBasic;
import com.csse3200.game.components.quests.Task;

import java.util.*;

public class QuestSave implements Json.Serializable {
    public List<QuestBasic> quests = new ArrayList<>();

    @Override
    public void write(Json json) {
        json.writeArrayStart("quests");
        for(QuestBasic element : quests) {
            json.writeValue(element);
        }
        json.writeArrayEnd();
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        ArrayList<QuestBasic> newQuests = new ArrayList<>();
        for (JsonValue quest : jsonData.child) {
            Iterator<JsonValue> taskList;

            if(quest.get("tasks").has("items")) {
                taskList = quest.get("tasks").get("items").iterator();
            } else {
                taskList = quest.get("tasks").iterator();
            }

            Iterator<JsonValue> dialogueList;
            if(quest.get("taskCompletionTriggers").isNull()) {
                dialogueList = null;
            } else {
                dialogueList = quest.get("questDialogue").iterator();
            }

            Iterator<JsonValue> taskCompletionList;

            if(quest.get("taskCompletionTriggers").isNull()) {
                taskCompletionList = null;
            } else {
                taskCompletionList = quest.get("taskCompletionTriggers").iterator();
            }

            List<Task> newTasks = new ArrayList<>();

            Map<DialogueKey, String[]> newDialogues;

            if(dialogueList != null) {
                newDialogues = new HashMap<>();
            } else {
                newDialogues = null;
            }

            List<String> newTriggers;

            String[] finalTriggers = null;

            if(taskCompletionList != null) {
                newTriggers = new ArrayList<>();
            } else {
                newTriggers = null;
            }

            while (taskList.hasNext()) {
                JsonValue taskValue = taskList.next();
                Task task = new Task(taskValue.getString("taskName"),
                        taskValue.getString("description"),
                        taskValue.getString("hint"),
                        taskValue.getInt("requiredTriggers"),
                        taskValue.getInt("triggerCount"),
                        taskValue.getBoolean("completed"),
                        taskValue.getBoolean("failed"));
                newTasks.add(task);
            }

            if(dialogueList != null) {
                while (dialogueList.hasNext()) {
                    //TODO: fill with functional loading for NPC integration
                    dialogueList.next();
                }
            }

            if(taskCompletionList != null) {
                while (taskCompletionList.hasNext()) {
                    newTriggers.add(taskCompletionList.next().toString());
                }
                finalTriggers = newTriggers.toArray(new String[newTriggers.size()]);
            }

            QuestBasic nextQuest = new QuestBasic(quest.getString("questName"),
                    quest.getString("questDescription"),
                    newTasks,
                    quest.getBoolean("isSecretQuest"),
                    newDialogues,
                    finalTriggers,
                    quest.getBoolean("isActive"),
                    quest.getBoolean("isFailed"),
                    quest.getInt("currentTaskIndex"));
            newQuests.add(nextQuest);
        }
        quests = newQuests;
    }
}
