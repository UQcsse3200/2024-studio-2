package com.csse3200.game.components.quests;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.components.Component;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.eventservice.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class QuestManager extends Component {
    private final HashMap<String, QuestBasic> quests;
    private final EventService eventService = ServiceLocator.getEventService();
    private static final Logger logger = LoggerFactory.getLogger(QuestManager.class);
    private final Sound questComplete = ServiceLocator.getResourceService().getAsset("sounds/QuestComplete.wav", Sound.class);

    public QuestManager() {
        this.quests = new HashMap<>();

        // Manual test Quests
        Task stepsTask = new Task("steps", "Take your first steps", "Just start moving!", 1);
        Task attackTask = new Task("attack", "Swing your first sword", "Just Attack!", 1);
        List<Task> tasks = List.of(stepsTask);
        List<Task> tasks1 = List.of(stepsTask,attackTask);
        QuestBasic twoTaskQuest = new QuestBasic("2 Task Quest","Move then Attack for a Test Quest", tasks1, false,false);
        QuestBasic firstStepsQuest = new QuestBasic("First Steps","Take your first steps in this world!", tasks, false,false);
        addQuest(twoTaskQuest);
        addQuest(firstStepsQuest);
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

    public QuestBasic getQuest(String questName) {
        return quests.get(questName);
    }

    public void failQuest(String questName){
        getQuest(questName).failQuest();
    }

    public void progressQuest(String questName, String taskName) {
        QuestBasic quest = quests.get(questName);
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
}
