package com.csse3200.game.components.quests;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.player.PlayerInventoryDisplay;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.gamestate.GameState;
import com.csse3200.game.gamestate.data.QuestSave;
import com.csse3200.game.inventory.Inventory;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.entities.DialogueBoxService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Manages, tracks and updates the quests within the game.
 * Handles the storage and retrieval of quests and integrates with the event system.
 */
public class QuestManager extends Component {
    /** Map to store quests. */
    private final HashMap<String, QuestBasic> quests;
     /** Map to store achievements. */
     private final HashMap<String, QuestHidden> achievements;

    /** Logger for logging quest related attributes. */
    private static final Logger logger = LoggerFactory.getLogger(QuestManager.class);
    /** Sound effect for quest completion. */
    private final Sound questComplete = ServiceLocator.getResourceService().getAsset("sounds/QuestComplete.wav", Sound.class);
    private final Sound achievementComplete = ServiceLocator.getResourceService().getAsset("sounds/achievement-sound.mp3", Sound.class);
    /** Map of relevant quests. As of Sprint 1 the String[] should contain only one quest as only one is accessed*/
    private final Map<String, String[]> relevantQuests;

    private final Entity player;

    private final DialogueBoxService dialogueBoxService;



    /**Constructs questManager instance */
    public QuestManager(Entity player) {
        this.quests = new HashMap<>();
        this.achievements = new HashMap<>();
        this.player = player;
        this.relevantQuests = Map.of(
                "Cow", new String[]{"2 Task Quest"}
        );
        this.dialogueBoxService = ServiceLocator.getDialogueBoxService();
    }

    /**
     * Sets up the tasks for the quests and dialogues.
     */

    private Task[] createTasks() {
        Task stepsTask = new Task("steps", "Take your first steps", "Just start moving!", 1, 0, false, false);
        Task attackTask = new Task("attack", "Swing your first sword", "Just Attack!", 1, 0, false, false);
        Task testKangaTask = new Task("spawnKangaBoss", "He is Coming...", "RUN", 1, 0, false, false);
        Task talkToGuide = new Task("talkToGuide", "Talk to the cow", "Speak with the Guide to start your journey.", 1, 0, false, false);
        Task followCowsTeachings = new Task("followCowsTeachings", "Complete further quests", "Complete first steps and 2 step quest or a combat quest", 1, 0, false, false);
        Task collectPotions = new Task("item collection task successful", "Collect Potions", "Collect 5 defense potions scattered around the kingdom.", 1, 0, false, false);
        Task listenAdvice = new Task("listenToGuide", "Visit cow again", "Go visit the cow!", 1, 0, false, false);
        Task exploreWild = new Task("exploration", "Explore and ask around", "Ask other animals about Kanga!", 1, 1, false, false);
        Task retrieveWeapon = new Task("retrieveWeapon", "Complete the minigame", "Play the snake minigame!", 1, 0, false, false);

        return new Task[] {
                stepsTask, attackTask, testKangaTask, talkToGuide, followCowsTeachings,
                collectPotions, listenAdvice, exploreWild, retrieveWeapon
        };
    }

    /**Sets up the dialogue for quests. */
    private Map<DialogueKey, String[]> createQuestDialogues() {

        String[] cowInitialDialogue = {
                "Moo there, adventurer! Welcome to the kingdom.",
                "We’ll be your guides but before you can roam free you must complete the first steps and 2 step quests."
        };
        String[] cowAdviceDialogue = {
                "Heads up! This world is controlled by the Kanga - the most powerful animal in the kingdom."
        };
        String[] potionDialogue = {
                "I need five potions! They’re scattered around. Keep your eyes peeled."
        };
        String[] listenDialogue = {
                "Heads up! This world is controlled by the Kanga - the most powerful animal in the kingdom."
        };
        String[] exploreDialogue = {
                "Oh the Kanga? Yeah, he was once a sweet little joey. Hard to imagine he’d go to the dark side.",
                "Word on the street is he snapped after his wife and daughter died in the flood.",
                "Rumour has it, a peacock pushed them off the boat! No wonder he’s on a rampage.",
                "That peacock? The first animal Kanga defeated. After that, it was duel city. Kanga beat all the top animals. Now? No one dares to mess with him."
        };

        return Map.of(new DialogueKey("Cow", 1), cowInitialDialogue,
                new DialogueKey("Cow", 2), cowAdviceDialogue,
                new DialogueKey("Cow", 3), potionDialogue,
                new DialogueKey("Cow", 4), listenDialogue
        );

    }

    /**
     * Adds quests to the game's quest list by creating new `QuestBasic` instances.
     * @param tasks An array of objects that represent the tasks to be added to the quest.
     * @param guideQuestDialogues A map where the keys instance and the values
     *                             are arrays of strings representing the dialogues associated with the quest.
     */
    private void addQuests(Task[] tasks, Map<DialogueKey, String[]> guideQuestDialogues) {
        // Add new tasks to a quest
        List<Task> firstStepsTasks = new ArrayList<>(List.of(tasks[0]));
        QuestBasic firstStepsQuest = new QuestBasic("First Steps", "Take your first steps in this world!", firstStepsTasks, false, null, null, false, false, 0);
        GameState.quests.quests.add(firstStepsQuest);

        List<Task> talkingQuest = new ArrayList<>(List.of(tasks[3]));
        QuestBasic guideQuest = new QuestBasic("Guide's Intro", "Follow the guide's teachings to start your journey.", talkingQuest, false, guideQuestDialogues, null, false, false, 0);
        addQuest(guideQuest);
        GameState.quests.quests.add(guideQuest);

        List<Task> followQuest = new ArrayList<>(List.of(tasks[4]));
        QuestBasic guideQuest2 = new QuestBasic("Teachings", "Follow the cow's teachings and complete further quests.", followQuest, false, guideQuestDialogues, null, false, false, 0);
        addQuest(guideQuest2);
        GameState.quests.quests.add(guideQuest2);

        List<Task> potionQuest = new ArrayList<>(List.of(tasks[5]));
        QuestBasic guideQuest3 = new QuestBasic("Potion Collection", "Collect 5 defense potions scattered around the kingdom.", potionQuest, false, guideQuestDialogues, null, false, false, 0);
        addQuest(guideQuest3);
        GameState.quests.quests.add(guideQuest3);

        List<Task> listenQuest = new ArrayList<>(List.of(tasks[6]));
        QuestBasic guideQuest4 = new QuestBasic("Guide's Advice", "Listen to the guide's advice to progress further.", listenQuest, false, null, null, false, false, 0);
        addQuest(guideQuest4);
        GameState.quests.quests.add(guideQuest4);

        List<Task> exploreQuest = new ArrayList<>(List.of(tasks[7]));
        QuestBasic guideQuest5 = new QuestBasic("Exploration", "Explore the kingdom and gather information about Kanga.", exploreQuest, false, null, null, false, false, 0);
        addQuest(guideQuest5);
        GameState.quests.quests.add(guideQuest5);

        List<Task> retrieveQuest = new ArrayList<>(List.of(tasks[8]));
        QuestBasic guideQuest6 = new QuestBasic("Weapon Retrieval", "Retrieve a weapon by completing the snake minigame.", retrieveQuest, false, null, null, false, false, 0);
        addQuest(guideQuest6);
        GameState.quests.quests.add(guideQuest6);

        String[] test2StepCompletionTriggers = {"", "spawnKangaBoss"};
        String[] test2StepTextProg1 = {"Welcome to Animal Kingdom!", "Here let me help with your quest...", "Press Spacebar!"};
        String[] test2StepTextProg2 = {"Yippeee!", "You completed your Quest!"};
        List<Task> twoTaskQuestTasks = new ArrayList<>(List.of(tasks[0], tasks[1]));
        Map<DialogueKey, String[]> test2TaskQuestDialogues = Map.of(
                new DialogueKey("Cow", 1), test2StepTextProg1,
                new DialogueKey("Cow", 2), test2StepTextProg2
        );
        QuestBasic twoTaskQuest = new QuestBasic("2 Task Quest", "Move then Attack for a Test Quest", twoTaskQuestTasks, false, test2TaskQuestDialogues, test2StepCompletionTriggers, false, false, 0);
        GameState.quests.quests.add(twoTaskQuest);

        // Create 2 task quest
        List<Task> finalQuestTasks = new ArrayList<>(List.of(tasks[2], tasks[0], tasks[1]));
        QuestBasic finalQuest = new QuestBasic("Final Boss", "Complete quest 1 and 2 to summon the boss", finalQuestTasks, false, null, null, false, false, 0);
        GameState.quests.quests.add(finalQuest);

        // test achievement for functionality, remove on @theboah's merge
        QuestHidden questHidden = new QuestHidden("steps", "these boots have seen everything");
        addAchievement(questHidden);
    }

    /** Creates all tests for quests and dialogues */
    private void testQuests() {
        Task[] tasks = createTasks();
        Map<DialogueKey, String[]> questDialogues = createQuestDialogues();
        addQuests(tasks, questDialogues);

    }

    /** Setup potion collection task listener.
     * Note: limitation on item collection - 1 item collection per kingdom
     *      w completion string "item collection task successful"  */
    private void setupPotionsTask() {
        Inventory inventory = entity.getComponent(PlayerInventoryDisplay.class).getInventory();
        inventory.questItemListen("Defense Potion", 5);
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
        if(GameState.quests == null) {
            GameState.quests = new QuestSave();
        }
        if(GameState.quests.quests.isEmpty()) {
            testQuests();
        }
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

        if (questName.equals("First Steps")) {
            setupPotionsTask();
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



    /**
     * Completes the achievement by changing the state of the achievement and triggering an achievement popup
     * @param achievementName The name of the achievement being completed
     */
    public void completeAchievement(String achievementName) {
        QuestHidden achievement = achievements.get(achievementName);
        if (achievement != null && !achievement.isCompleted()) {
            achievement.complete();
            achievementComplete.play();
            player.getEvents().trigger("achievementCompleted");
            logger.info("{} Completed!", achievement.getQuestName());
        }
    }


}
