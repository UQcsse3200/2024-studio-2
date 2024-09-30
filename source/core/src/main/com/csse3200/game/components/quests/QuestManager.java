package com.csse3200.game.components.quests;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.inventory.InventoryComponent;
import com.csse3200.game.components.minigames.MiniGameMedals;
import com.csse3200.game.components.minigames.MiniGameNames;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.gamestate.GameState;
import com.csse3200.game.inventory.Inventory;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.DialogueBoxService;
import com.csse3200.game.GdxGameManager;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.csse3200.game.components.quests.AchievementManager.saveAchievements;


/**
 * Manages, tracks and updates the quests within the game.
 * Handles the storage and retrieval of quests and integrates with the event system.
 */
public class QuestManager extends Component {
    /** Map to store quests. */
    private final LinkedHashMap<String, QuestBasic> quests;
     /** Array to store achievements. */
     private final Array<Achievement> achievements;
    /** Logger for logging quest related attributes. */
    private static final Logger logger = LoggerFactory.getLogger(QuestManager.class);
    /** Sound effect for quest completion. */
    private final Sound questComplete = ServiceLocator.getResourceService().getAsset("sounds/QuestComplete.wav", Sound.class);
    private final Sound achievementComplete = ServiceLocator.getResourceService().getAsset("sounds/achievement-sound.mp3", Sound.class);
    /** Map of relevant quests. As of Sprint 1 the String[] should contain only one quest as only one is accessed*/
    private final Map<String, String[]> relevantQuests;

    private final Entity player;

//    private final DialogueBoxService dialogueBoxService;
//    private final List<DialogueKey> questDialogues;


    /**Constructs questManager instance */
    public QuestManager(Entity player) {
        this.quests = new LinkedHashMap<>();
        this.player = player;
        this.relevantQuests = Map.of(
                "Cow", new String[]{"2 Task Quest"}
        );
//        this.dialogueBoxService = ServiceLocator.getDialogueBoxService();
        AchievementManager achievementManager = new AchievementManager();
        this.achievements =  achievementManager.getAchievements();
        setupAchievements();
        // TODO: check if it works
        player.getEvents().addListener("defeatedEnemy",this::handleEnemyQuest);
        createQuestDialogues();

    }

    //change some description and things later on
    //do potions need 5 triggers???
    /**
     * Sets up the tasks for the quests and dialogues.
     */

    private Task[] createAnimalTasks() {
        Task talkToGuide = new Task(
                "talkToGuide",
                "Talk to the Guide",
                "Speak with the Guide to begin your adventure.",
                1, 0, false, false
        );

        Task collectPotions = new Task(
                "collectPotions",
                "Collect Potions",
                "Find 5 defense potions scattered throughout the kingdom.",
                1, 0, false, false
        );

        Task defeatEnemies = new Task(
                "defeatEnemies",
                "Defeat the Enemies",
                "Defeat the creatures threatening the kingdom.",
                1, 0, false, false
        );

        Task playSnakeMinigame = new Task(
                "playSnakeMinigame",
                "Play the Snake Minigame",
                "Complete the minigame to retrieve the Eucalyptus sword.",
                1, 0, false, false
        );

        Task defeatKangarooBoss = new Task(
                "defeatKangarooBoss",
                "Defeat the Kangaroo Boss",
                "Face the Kangaroo Boss to bring peace to the kingdom.",
                1, 0, false, false
        );

        return new Task[] {
                talkToGuide, collectPotions, defeatEnemies, playSnakeMinigame, defeatKangarooBoss
        };
    }

    private Task[] createWaterTasks() {
        Task talkToWaterSage = new Task(
                "talkToWaterSage",
                "Talk to the Water Sage",
                "Speak with the Water Sage to gain their trust and begin your quest.",
                1, 0, false, false
        );

        Task collectSeaPearls = new Task(
                "collectSeaPearls",
                "Collect Sea Pearls",
                "Find six Sea Pearls hidden around for the Water Sage's research.",
                1, 0, false, false
        );

        Task defeatSeaCreatures = new Task(
                "defeatSeaCreatures",
                "Defeat the Sea Creatures",
                "Show your strength by defeating the hostile sea creatures in the Water Kingdom.",
                1, 0, false, false
        );

        Task completeFlappyMinigame = new Task(
                "completeFlappyMinigame",
                "Complete the Flappy Bird Minigame",
                "Complete the Flappy Bird-inspired minigame to unlock the next stage of your quest!",
                1, 0, false, false
        );

        Task defeatWater = new Task(
                "defeatWater",
                "Defeat the Water Boss",
                "Prepare yourself and face the Water Boss to protect the kingdom.",
                1, 0, false, false
        );

        return new Task[] {
                talkToWaterSage, collectSeaPearls, defeatSeaCreatures, completeFlappyMinigame, defeatWater
        };

    }

    private Task[] createSkyTasks() {
        Task talkToCloudSage = new Task(
                "talkToCloudSage",
                "Talk to the Cloud Sage",
                "Speak with the Cloud Sage to begin your quest in the Sky Kingdom.",
                1, 0, false, false
        );

        Task collectSkyCrystals = new Task(
                "collectSkyCrystals",
                "Collect Sky Crystals",
                "Gather seven Sky Crystals for the Cloud Sage.",
                1, 0, false, false
        );

        Task defeatFlyingBeasts = new Task(
                "defeatFlyingBeasts",
                "Defeat the Flying Beasts",
                "Prove your bravery by facing the flying beasts that roam the skies.",
                1, 0, false, false
        );

        Task completeGame = new Task(
                "completeTempestMinigame",
                "Calm the Tempests Minigame",
                "Complete the minigame to go onto your next quest!",
                1, 0, false, false
        );

        Task defeatSkySeraph = new Task(
                "defeatSkySeraph",
                "Defeat the Sky Seraph",
                "Prepare yourself and confront the Sky Seraph in an epic battle!",
                1, 0, false, false
        );

        return new Task[] {
                talkToCloudSage, collectSkyCrystals, defeatFlyingBeasts, completeGame, defeatSkySeraph
        };
    }

    /**
     * Retrieves the map of quest dialogues.
     *
     * @return a map where the key is of type DialogueKey and the value is a 2D array of strings representing questName and Dialogue.
     */
//    public List<DialogueKey> getQuestDialogues() {
//        return this.questDialogues;
//    }

    //change names later on
    /**
     * Sets up the dialogue for quests.
     */

    //needs to be changed depending on biome
    private List<DialogueKey> createQuestDialogues() {

        List<DialogueKey> dialogues = new ArrayList<>();


        String[][] cowInitialDialogue = {
                {"Moo there adventurer, welcome to the Animal Kingdom! I’m your guide. To prove your worth, you’ll need to complete a few tasks. Are you ready?"}
        };


        String[][] potionDialogue = {
                {"I need five potions to help our people! They’re scattered throughout the kingdom. Can you find them?"}
        };


        String[][] defeatEnemiesDialogue = {
                {"Defeat the wild creatures that threaten the peace of our land."}
        };


        String[][] snakeMinigameDialogue = {
                {"Help the snake eat apples to train for your upcoming final battle!"}  // Alternate dialogue for the minigame
        };


        String[][] kangaBossDialogue = {
                {"Once you’ve completed your tasks, face the Kangaroo Boss and bring peace back to the kingdom."}
        };

        String[][] talkToWSageDialogue = {
                {"Greetings, brave adventurer! You’ve entered the Water Kingdom. To gain my trust, you must complete some essential tasks."}
        };

        String[][] seaPearlsDialogue = {
                {"I need six Sea Pearls for my research. They’re hidden in the underwater caverns. Can you help me?"}
        };

        String[][] skyEnemiesDialogue = {
                {"Watch out for hostile sea creatures! Defeat them to show your strength."}
        };

        String[][] secondMinigameDialogue = {
                {"Play the flappy bird minigame to unlock the next stage of your quest!"}
        };

        String[][] defeatSkyBossDialogue = {
                {"Once you’re ready, face the Water Leviathan and protect our kingdom. Good luck, brave adventurer!"}
        };

        String[][] talkToSSage = {
                {"Welcome, traveler of the skies! You’ve arrived in the Sky Kingdom. Complete these tasks to challenge the Sky Seraph."}
        };

        String[][] collectSkyCrystalsDialogue = {
                {"I need seven Sky Crystals that drift on the winds. Will you help me gather them?"}
        };

        String[][] defeatFlyingBeasts = {
                {"Face the flying beasts that roam the skies to prove your bravery."}
        };

        String[][] MinigameDialogue = {
                {"..."}
        };

        String[][] defeatWaterBossDialogue = {
                {"When you’re prepared, confront the Sky Seraph in an epic battle!"}
        };


        dialogues.add(new DialogueKey("Cow", cowInitialDialogue));
        dialogues.add(new DialogueKey("Cow", potionDialogue));
        dialogues.add(new DialogueKey("Cow", defeatEnemiesDialogue));
        dialogues.add(new DialogueKey("Cow", snakeMinigameDialogue));
        dialogues.add(new DialogueKey("Cow", kangaBossDialogue));



        return dialogues;
    }



    /** Setup item collection task listener (1 at a time).
     * Note: limitation on item collection - 1 item collection per kingdom
     *      w completion string "item collection task successful"
     * @param itemName the item to be collected
     * @param quantity the number of items needed for successful item completion task
     * @param completionTrigger the string to be triggered (task name) on successful item collection
     *      */
    private void setupItemCollectionsTask(String itemName, int quantity, String completionTrigger) {
        try {
            Inventory inventory = entity.getComponent(InventoryComponent.class).getInventory();
            inventory.questItemListen(itemName, quantity, completionTrigger);
        } catch (NullPointerException nullPointerException) {
            // Ignore - no inventory given
        }

    }


    private void setupAchievements(){
        // Init logbook listeners and handlers
        player.getEvents().addListener("addItem",this::handleItemAdvancement);
        player.getEvents().addListener("defeatedEnemy",this::handleEnemyAdvancement);
        player.getEvents().addListener("miniGame",this::handleMiniGameAdvancement);
        for (Achievement achievement : achievements) {
            subscribeToAchievementEvents(achievement);
        }
    }

    /**
     * Subscribes to mini game triggers and sends it as a specific achievement completion trigger.
     */
    private void handleMiniGameAdvancement(MiniGameNames name, MiniGameMedals medal){
        player.getEvents().trigger(medal.name()  +' ' + name.name());
    }

    /**
     * Subscribes to enemy beaten triggers and sends it as a specific achievement completion trigger.
     */
    private void handleEnemyAdvancement(Entity enemy){
        player.getEvents().trigger(enemy.getEnemyType().toString());
    }

    /**
     * Subscribes to item triggers and sends it as a specific achievement completion trigger.
     */
    private void handleItemAdvancement(AbstractItem item){
        player.getEvents().trigger(item.getName() + "Advancement");
    }

    private void handleEnemyQuest(Entity enemy) {
        String type = enemy.getEnemyType().toString();
        player.getEvents().trigger("defeat" + type);

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
    private void subscribeToAchievementEvents(Achievement achievement) {
        player.getEvents().addListener(achievement.getQuestName(), () -> this.completeAchievement(achievement));
    }
    /**
     * Adds a new quest to the manager.
     * @param quest The quest to be added.
     */

    public void addQuest(QuestBasic quest) {
        this.quests.put(quest.getQuestName(), quest);
        subscribeToQuestEvents(quest);
    }
    

    /**
     * Automatically loads and registers all of the quests stored in GameState.
     * @see GameState
     */
    public void loadQuests() {
        for (QuestBasic quest : GameState.quests.quests) {
            addQuest(quest);
            logger.info("Dialogue loaded: {}", quest.getQuestDialogue().getFirst());

            // Setup item collection tasks (if inventory exists)
            if (quest.getQuestName().equals("Guide's Request")) {
                if (!quest.isQuestCompleted()) {
                    setupItemCollectionsTask("Defense Potion", 5, "collectPotions");
                } else {
                    setupItemCollectionsTask("Candy", 6, "collectCandy");
                }
                continue;
            }
            if (quest.getQuestName().equals("Water Sage's research") && quest.isQuestCompleted()) {
                setupItemCollectionsTask("Apple", 4, "collectApples");
            }
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

        if (quest.getQuestName().equals("Guide's Request")) {
            setupItemCollectionsTask("Candy", 6, "collectCandy");
        }
        if (quest.getQuestName().equals("Water Sage's research")) {
            setupItemCollectionsTask("Apple", 4, "collectApples");
        }
    }



    /**
     * Completes the achievement by changing the state of the achievement and triggering an achievement popup
     * @param achievement The achievement being completed
     */
    public void completeAchievement(Achievement achievement) {
        if (achievement != null && !achievement.isCompleted()) {
            achievement.complete();
            achievementComplete.play();
            player.getEvents().trigger("achievementCompleted");
            saveAchievements(achievements,"saves/achievements.json");
            logger.info("{} Completed!", achievement.getQuestName());
        }
    }

    @Override
    public void dispose() {
        saveAchievements(achievements,"saves/achievements.json");
        super.dispose();
    }




}
