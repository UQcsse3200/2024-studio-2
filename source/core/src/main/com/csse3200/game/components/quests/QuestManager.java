package com.csse3200.game.components.quests;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.components.Component;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.eventservice.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class QuestManager extends Component {
    private final HashMap<String, QuestBasic> quests;
    private final EventService eventService = ServiceLocator.getEventService();
    private static final Logger logger = LoggerFactory.getLogger(QuestManager.class);
    private final Sound questComplete = ServiceLocator.getResourceService().getAsset("sounds/QuestComplete.wav", Sound.class);
    private final Map<String, String[]> relevantQuests;

    public QuestManager() {
        this.quests = new HashMap<>();
        this.relevantQuests = Map.of(
                "cow", new String[]{"2 Task Quest"}
        );
        testQuests();
    }

    private void testQuests() {
        Task stepsTask = new Task("steps", "Take your first steps", "Just start moving!", 1);
        Task attackTask = new Task("attack", "Swing your first sword", "Just Attack!", 1);



        List<Task> tasks = List.of(stepsTask);
        QuestBasic firstStepsQuest = new QuestBasic("First Steps","Take your first steps in this world!", tasks, false,false,null);
        addQuest(firstStepsQuest);


        // Initialise 2 Step Test Quest
        ArrayList<String[]> test2StepTextProg1 = new ArrayList<>();
        test2StepTextProg1.add(new String[]{"Welcome to Animal Kingdom!", "Here let me help with your quest.."});
        test2StepTextProg1.add(new String[]{"Press Spacebar!"});
        ArrayList<String[]> test2StepTextProg2 = new ArrayList<>();
        test2StepTextProg2.add(new String[]{"Yippeee!", "You completed your Quest!"});

        Map<DialogueKey, ArrayList<String[]>> test2TaskQuestDialogue = Map.of(
                new DialogueKey("Cow", 1), test2StepTextProg1,
                new DialogueKey("Cow", 2), test2StepTextProg2
        );
        List<Task> tasks1 = List.of(stepsTask, attackTask);
        QuestBasic twoTaskQuest = new QuestBasic("2 Task Quest", "Move then Attack for a Test Quest", tasks1, false, false, test2TaskQuestDialogue);
        addQuest(twoTaskQuest);
    }

    private void subscribeToQuestEvents(QuestBasic quest) {
        for (Task task : quest.getTasks()) {
            eventService.globalEventHandler.addListener(task.getTaskName(),
                    () -> progressQuest(quest.getQuestName(), task.getTaskName()));
        }
    }

    public void addQuest(QuestBasic quest) {
        quests.put(quest.getQuestName(), quest);
        subscribeToQuestEvents(quest);
    }

    public List<QuestBasic> getAllQuests() {
        return new ArrayList<>(quests.values());
    }

    public QuestBasic getQuest(String questName) {
        return quests.get(questName);
    }

    public void failQuest(String questName) {
        QuestBasic quest = getQuest(questName);
        if (quest != null) {
            quest.failQuest();
        }
    }

    public void progressQuest(String questName, String taskName) {
        QuestBasic quest = getQuest(questName);
        if (quest == null || !canProgressQuest(quest, taskName)) {
            return;
        }

        Task currentTask = quest.getTasks().get(quest.getProgression());
        currentTask.handleEvent();

        if (currentTask.isFailed()) {
            quest.failQuest();
            logger.info("{} failed!", quest.getQuestName());
        } else if (currentTask.isCompleted()) {
            completeTask(quest);
        }
    }

    private boolean canProgressQuest(QuestBasic quest, String taskName) {
        return !quest.isQuestCompleted() &&
                !quest.isFailed() &&
                Objects.equals(taskName, quest.getTasks().get(quest.getProgression()).getTaskName());
    }

    private void completeTask(QuestBasic quest) {
        quest.progressQuest();
        if (quest.isQuestCompleted()) {
            handleQuestCompletion(quest);
        } else {
            logger.info("Progress: {}/{}", quest.getProgression(), quest.getTasks().size());
        }
    }

    private void handleQuestCompletion(QuestBasic quest) {
        if (!quest.isAchievement() && !quest.isSecret()) {
            questComplete.play();
            eventService.globalEventHandler.trigger("questCompleted");
            eventService.globalEventHandler.trigger(quest.getQuestName());
            logger.info("{} completed!", quest.getQuestName());
        }
    }

    /** Returns all the dialogue for all quests for the given npc */
    public ArrayList<String[]> getDialogue(String npcName) {
        String[] npcRelevantQuests = relevantQuests.get(npcName);
        ArrayList<String[]> npcDialogue = new ArrayList<>();
        if (npcRelevantQuests != null) {
            for (String questName : npcRelevantQuests) {
                QuestBasic quest = quests.get(questName);
                if (quest != null) {
                    ArrayList<String[]> dialogue = quest.getDialogue(npcName);
                    if (dialogue != null) {
                        npcDialogue.addAll(dialogue);
                    }
                }
            }
        }
        return npcDialogue;
    }
}
