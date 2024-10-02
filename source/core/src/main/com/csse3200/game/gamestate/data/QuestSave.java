package com.csse3200.game.gamestate.data;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.components.quests.DialogueKey;
import com.csse3200.game.components.quests.Quest;
import com.csse3200.game.components.quests.Task;

import java.util.*;

public class QuestSave implements Json.Serializable {
    public List<Quest> quests = new ArrayList<>();

    @Override
    public void write(Json json) {
        json.writeArrayStart("quests");
        for(Quest element : quests) {
            json.writeValue(element);
        }
        json.writeArrayEnd();
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        ArrayList<Quest> newQuests = new ArrayList<>();
        for (JsonValue quest : jsonData.child) {
            Iterator<JsonValue> taskList;

            if(quest.get("tasks").has("items")) {
                taskList = quest.get("tasks").get("items").iterator();
            } else {
                taskList = quest.get("tasks").iterator();
            }

            Iterator<JsonValue> dialogueList;
            if(quest.get("questDialogue").isNull()) {
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

            ArrayList<DialogueKey> newDialogues;

            if(dialogueList != null) {
                newDialogues = new ArrayList<>();
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
                    JsonValue dialogue = dialogueList.next();
                    DialogueKey newKey;
                    String npc = dialogue.getString("npcName");

                    String[][] dialogueArray;
                    Iterator<JsonValue> dialogueIterator = dialogue.get("dialogue").iterator();
                    List<String[]> dialogueIteratorList = new ArrayList<>();

                    while(dialogueIterator.hasNext()) {
                        JsonValue dialogueItem = dialogueIterator.next();
                        Iterator<JsonValue> dialogueLineIterator = dialogueItem.iterator();
                        List<String> dialogueLines = new ArrayList<>();

                        while(dialogueLineIterator.hasNext()) {
                            JsonValue dialogueLine = dialogueLineIterator.next();
                            dialogueLines.add(dialogueLine.asString());
                        }

                        dialogueIteratorList.add(dialogueLines.toArray(new String[0]));
                    }

                    dialogueArray = dialogueIteratorList.toArray(new String[0][0]);
                    newKey = new DialogueKey(npc, dialogueArray);
                    newDialogues.add(newKey);
                }
            }
            if(taskCompletionList != null) {
                while (taskCompletionList.hasNext()) {
                    newTriggers.add(taskCompletionList.next().toString());
                }
                finalTriggers = newTriggers.toArray(new String[newTriggers.size()]);
            }

            Iterator<JsonValue> followList;
            if(!quest.has("followQuests")) {
                followList = null;
            } else {
                followList = quest.get("followQuests").iterator();
            }

            quest.get("followQuests");
            List<String> newFollows = new ArrayList<String>();
            if(followList != null) {
                while(followList.hasNext()) {
                    JsonValue follow = followList.next();
                    newFollows.add(follow.asString());
                }
            }


            Quest nextQuest = new Quest(quest.getString("questName"),
                    quest.getString("questDescription"),
                    newTasks,
                    quest.getBoolean("isSecretQuest"),
                    newDialogues,
                    finalTriggers,
                    quest.getBoolean("isActive"),
                    quest.getBoolean("isFailed"),
                    quest.getInt("currentTaskIndex"),
                    newFollows.toArray(new String[]{}));
            newQuests.add(nextQuest);
        }
        quests = newQuests;
    }
}