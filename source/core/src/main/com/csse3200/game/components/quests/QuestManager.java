package com.csse3200.game.components.quests;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Null;
import com.csse3200.game.components.Component;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.eventservice.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class QuestManager extends Component {
    private final HashMap<String, AbstractQuest> quests;
    private final EventService eventService = ServiceLocator.getEventService();
    private static final Logger logger = LoggerFactory.getLogger(QuestManager.class);
    private final Sound questComplete = ServiceLocator.getResourceService().getAsset("sounds/QuestComplete.wav", Sound.class);
    private final Map<String, String[]> relevantQuests;


    public QuestManager() {
        this.quests = new HashMap<>();

        // This will load from files
        this.relevantQuests = Map.of("cow", new String[]{"2 Task Quest"});

        // Manual test Quests

        // Initialise tasks
        Task stepsTask = new Task("steps", "Take your first steps", "Just start moving!", 1);
        Task attackTask = new Task("attack", "Swing your first sword", "Just Attack!", 1);

        // Initialise First Steps Test Quest
        List<Task> tasks = List.of(stepsTask);
        QuestBasic firstStepsQuest = new QuestBasic("First Steps","Take your first steps in this world!", tasks, false,false,null);
        addQuest(firstStepsQuest);


        // Initialise 2 Step Test Quest
        ArrayList<String[]> test2StepTextProg1 = new ArrayList<>();

        test2StepTextProg1.add(new String[]{"Welcome to Animal Kingdom!", "Here let me help with your quest.."});
        test2StepTextProg1.add(new String[]{"Press Spacebar!"});
        ArrayList<String[]> test2StepTextProg2 = new ArrayList<>();
        test2StepTextProg2.add(new String[]{"Yippeee!", "You completed your Quest!"});

        Map<DialogueKey,ArrayList<String[]>> test2TaskQuestDialogue = Map.of(new DialogueKey("Cow",1),test2StepTextProg1, new DialogueKey("Cow", 2),test2StepTextProg2);
        List<Task> tasks1 = List.of(stepsTask,attackTask);
        QuestBasic twoTaskQuest = new QuestBasic("2 Task Quest","Move then Attack for a Test Quest", tasks1, false,false,test2TaskQuestDialogue);
        addQuest(twoTaskQuest);
    }

    private void subscribeToQuestEvents(QuestBasic quest) {
        for (Task task : quest.getTasks()) {
            eventService.globalEventHandler.addListener(task.getTaskName(), () -> this.progressQuest(quest.getQuestName(),task.getTaskName()));
        }
    }

    public void addQuest(QuestBasic quest) {
        quests.put(quest.getQuestName(), quest);
        subscribeToQuestEvents(quest);
    }

    public List<AbstractQuest> getAllQuests(){
        return new ArrayList<>(this.quests.values());
    }

    public AbstractQuest getQuest(String questName) {
        return quests.get(questName);
    }

    public void failQuest(String questName){
        getQuest(questName).failQuest();
    }

    public void progressQuest(String questName, String taskName) {
        AbstractQuest quest = quests.get(questName);
        if (quest != null && !quest.isQuestCompleted() && !quest.isFailed()) {
            Task currentTask = quest.getTasks().get(quest.getProgression());
            if (Objects.equals(taskName, currentTask.getTaskName())) {
            currentTask.handleEvent();
            if (currentTask.isFailed()) {
                quest.failQuest();
            }
            else if (currentTask.isCompleted()) {
                quest.progressQuest();
                if (currentTask.isCompleted() && quest.isQuestCompleted()) {
                    if(!quest.isAchievement() && !quest.isSecret()) {
                        questComplete.play();
                        eventService.globalEventHandler.trigger("questCompleted");
                        eventService.globalEventHandler.trigger(questName);
                        logger.info("{} completed!", quest.getQuestName());
                    }

                } else {
                    logger.info("Progress: {}/{}", quest.getProgression(), quest.getTasks().size());
                }
            }
            }
       }
    }

    /** Returns all the dialogue for all quests for the given npc */
    @Null
    public ArrayList<String[]> getDialogue(String npcName){
        String[] npcRelevantQuests = relevantQuests.get(npcName);
        ArrayList<String[]> npcDialogue = new ArrayList<>();
        for (String questName : npcRelevantQuests) {
            AbstractQuest quest = quests.get(questName);
            if (quest != null) {
                ArrayList<String[]> dialogue = quest.getDialogue(npcName);
                if (dialogue != null) {
                    npcDialogue.addAll(dialogue);
            }
        }
        }
        return npcDialogue;
    }
}
