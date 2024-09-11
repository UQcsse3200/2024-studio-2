package com.csse3200.game.gamestate.data;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.components.quests.DialogueKey;
import com.csse3200.game.components.quests.QuestBasic;
import com.csse3200.game.components.quests.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class QuestSave implements Json.Serializable {
    private Logger logger = LoggerFactory.getLogger(QuestSave.class);
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
        logger.info("step 1 for real");
        ArrayList<QuestBasic> newQuests = new ArrayList<>();
        for (JsonValue quest : jsonData.child) {
            logger.info("step 1");
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

            logger.info("step 2");
            Iterator<JsonValue> taskCompletionList;

            if(quest.get("taskCompletionTriggers").isNull()) {
                taskCompletionList = null;
            } else {
                taskCompletionList = quest.get("taskCompletionTriggers").iterator();
            }

            logger.info("step 3");



            List<Task> newTasks = new ArrayList<>();

            Map<DialogueKey, String[]> newDialogues;

            if(dialogueList != null) {
                newDialogues = new HashMap<>();
            } else {
                newDialogues = null;
            }

            logger.info("step 4");
            List<String> newTriggers;

            String[] finalTriggers = null;

            if(taskCompletionList != null) {
                newTriggers = new ArrayList<>();
            } else {
                newTriggers = null;
            }

            logger.info("step 5");
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

            logger.info("step 6");

            if(dialogueList != null) {
                while (dialogueList.hasNext()) {
                    //TODO: fill with functional loading for NPC integration
                    dialogueList.next();
                }
            }


            logger.info("step 7");
            if(taskCompletionList != null) {
                while (taskCompletionList.hasNext()) {
                    newTriggers.add(taskCompletionList.next().toString());
                }
                finalTriggers = newTriggers.toArray(new String[newTriggers.size()]);
            }

            logger.info("step 8");
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
