package com.csse3200.game.components.quests;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.gamestate.GameState;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * Manages, tracks and updates the quests within the game.
 * Handles the storage and retrieval of quests and integrates with the event system.
 */
public class QuestManager extends Component {
    /** Map to store quests. */
    private final HashMap<String, QuestBasic> quests;
     /** Map to store achievements. */
     private final HashMap<String, QuestHidden> achievements;
    /** Event service to handle global events. */
    /** Logger for logging quest related attributes. */
    private static final Logger logger = LoggerFactory.getLogger(QuestManager.class);
    /** Sound effect for quest completion. */
    private final Sound questComplete = ServiceLocator.getResourceService().getAsset("sounds/QuestComplete.wav", Sound.class);
    /** Map of relevant quests. As of Sprint 1 the String[] should contain only one quest as only one is accessed*/
    private final Map<String, String[]> relevantQuests;

    private final Entity player;

    /**Constructs questManager instance */
    public QuestManager(Entity player) {
        this.quests = new HashMap<>();
        this.achievements = new HashMap<>();
        this.player = player;
        this.relevantQuests = Map.of(
                "Cow", new String[]{"2 Task Quest"}
        );
        if(GameState.quests.quests.isEmpty()) {
            testQuests();
        }
        loadQuests();
    }

    /**
     * Sets up the tasks for the quests and dialogues.
     */
    private void testQuests() {

        //creates test tasks
        Task stepsTask = new Task("steps", "Take your first steps", "Just start moving!", 1, 0, false, false);
        Task attackTask = new Task("attack", "Swing your first sword", "Just Attack!", 1, 0, false, false);
        Task testKangaTask = new Task("spawnKangaBoss", "He is Coming...", "RUN", 1, 0, false, false);

        //creates single task quest
        List<Task> tasks = List.of(stepsTask);
        QuestBasic firstStepsQuest = new QuestBasic("First Steps","Take your first steps in this world!", tasks, false,null,null, false, false, 0);

        GameState.quests.quests.add(firstStepsQuest);

        //creates 2 task quest
        String[] test2StepTextProg1 = new String[]{"Welcome to Animal Kingdom!", "Here let me help with your quest...","Press Spacebar!"};
        String[] test2StepTextProg2 = new String[]{"Yippeee!", "You completed your Quest!"};

        Map<DialogueKey, String[]> test2TaskQuestDialogue = Map.of(
                new DialogueKey("Cow", 1), test2StepTextProg1,
                new DialogueKey("Cow", 2), test2StepTextProg2
        );

        String[] test2StepCompletionTriggers = new String[]{"","spawnKangaBoss"};
        List<Task> tasks1 = List.of(stepsTask, attackTask);
        QuestBasic twoTaskQuest = new QuestBasic("2 Task Quest", "Move then Attack for a Test Quest", tasks1, false, test2TaskQuestDialogue,test2StepCompletionTriggers, false, false, 0);

        GameState.quests.quests.add(twoTaskQuest);

        // Creates test quest that requires completion of 2 task quest
        List<Task> tasks3 = List.of(testKangaTask,stepsTask, attackTask);
        QuestBasic finalQuest = new QuestBasic("Final Boss","Complete quest 1 and 2 to summon the boss", tasks3, false,null,null, false, false, 0);

        GameState.quests.quests.add(finalQuest);
    }

    /**
     * Subscribes to event notifications for tasks quest.
     * @param quest The quest related to the quests.
     */
    private void subscribeToQuestEvents(QuestBasic quest) {
        for (Task task : quest.getTasks()) {
            player.getEvents().addListener(task.getTaskName(),
                    () -> progressQuest(quest.getQuestName(), task.getTaskName()));
        }
    }

    /**
     * Adds a listener for the achievement, which completes the achievement when triggered.
     * @param achievement The achievement being listened to.
     */
    private void subscribeToAchievementEvents(QuestHidden achievement) {
        player.getEvents().addListener(achievement.getQuestName(), () -> this.completeAchievement(achievement.getQuestName()));
    }
    /**
     * Adds a new quest to the manager.
     * @param quest The quest to be added.
     */

    public void addQuest(QuestBasic quest) {
        quests.put(quest.getQuestName(), quest);
        subscribeToQuestEvents(quest);
    }

    /**
     * Add a achievement to the overall list of achievements.
     * @param achievement The achievement being added.
     */
    public void addAchievement(QuestHidden achievement) {
        achievements.put(achievement.getQuestName(), achievement);
        subscribeToAchievementEvents(achievement);
    }

    /**
     * Automatically loads and registers all of the quests stored in GameState.
     * @see GameState
     */
    public void loadQuests() {
        for (QuestBasic quest : GameState.quests.quests) {
            addQuest(quest);
        }
    }

    /**
     * Gets a list of all quests in QuestManager.
     * @return A list of all quests.
     */
    public List<QuestBasic> getAllQuests() {
        return new ArrayList<>(quests.values());
    }

    /**
     * Returns a quest by name.
     * @param questName The name of the quest to get.
     * @return The quest with the name.
     */

    public QuestBasic getQuest(String questName) {
        return quests.get(questName);
    }

    /**
     * Get the class representation of an achievement.
     * @param achievementName The name of the achievement being got.
     * @return The class representation of the achievement.
     */
    public QuestHidden getAchievement(String achievementName) {
        return achievements.get(achievementName);
    }

    /**
     * Checks if quest is failed.
     * @param questName The name of the quest to fail.
     */

    public void failQuest(String questName) {
        QuestBasic quest = getQuest(questName);
        if (quest != null) {
            quest.failQuest();
        }
    }


    /**
     * Progresses the quest based on completion and updates the quest status.
     * @param questName The name of the quest.
     * @param taskName  The name of the task.
     */
    public void progressQuest(String questName, String taskName) {
        QuestBasic quest = getQuest(questName);
        if (quest == null || !canProgressQuest(quest, taskName)) {
            return;
        }

        Task currentTask = quest.getTasks().get(quest.getProgression());
        currentTask.handleEvent();
        //check if quest is failed or completed
        if (currentTask.isFailed()) {
            quest.failQuest();
            logger.info("{} failed!", quest.getQuestName());
        } else if (currentTask.isCompleted()) {
            completeTask(quest);
        }
    }

    /**
     * Determines if a quest can be progressed based on its current state and the provided task name.
     * @param quest    The quest to check for.
     * @param taskName The name of the task to validate.
     * @return true if the quest can be progressed
     */

    private boolean canProgressQuest(QuestBasic quest, String taskName) {
        return !quest.isQuestCompleted() &&
                !quest.isFailed() &&
                Objects.equals(taskName, quest.getTasks().get(quest.getProgression()).getTaskName());
    }

    /**
     * Completes the task of the updates the quest progression.
     * @param quest The quest to be completed.
     */

    private void completeTask(QuestBasic quest) {
        quest.progressQuest(player); //advance quest progression
        if (quest.isQuestCompleted()) {
            handleQuestCompletion(quest);
        } else {
            logger.info("Progress: {}/{}", quest.getProgression(), quest.getTasks().size());
        }
    }

    /**
     * Handle quest completion.
     * @param quest The quest that has been completed.
     */

    private void handleQuestCompletion(QuestBasic quest) {
        if (!quest.isSecret()) {
            questComplete.play();
            player.getEvents().trigger("questCompleted");
            player.getEvents().trigger(quest.getQuestName());
            logger.info("{} completed!", quest.getQuestName());
        }
    }

    /** Returns all the dialogue for all quests for the given npc
     * In sprint 2 will return a struct containing all dialogue for (String questName : npcRelevantQuests)
     * Need to have null checks for npcName being in npcRelevantQuests
     * */
    public String[] getDialogue(String npcName) {
        String[] npcRelevantQuests = relevantQuests.get(npcName);
        //retrieve NPC dialogue
        if (npcRelevantQuests != null) {
            String singleRelevantQuest = npcRelevantQuests[0];
            QuestBasic quest = quests.get(singleRelevantQuest);
            if (quest != null) {
                return quest.getDialogue(npcName);
            }
        }
        return new String[]{};
    }

    /**
     * Completes the achievement by changing the state of the achievement and triggering an achievement popup
     * @param achievementName The name of the achievement being completed
     */
    public void completeAchievement(String achievementName) {
        QuestHidden achievement = achievements.get(achievementName);
        if (achievement != null && !achievement.isCompleted()) {
            achievement.complete();
            questComplete.play();
            player.getEvents().trigger("achievementCompleted");
            logger.info("{} Completed!", achievement.getQuestName());
        }
    }

}
