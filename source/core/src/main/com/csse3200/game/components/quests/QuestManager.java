package com.csse3200.game.components.quests;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.components.Component;
import com.csse3200.game.minigames.MiniGameMedals;
import com.csse3200.game.minigames.MiniGameNames;
import com.csse3200.game.components.inventory.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.gamestate.Achievements;
import com.csse3200.game.gamestate.GameState;
import com.csse3200.game.gamestate.SaveHandler;
import com.csse3200.game.inventory.Inventory;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.services.ServiceLocator;

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
     private final List<Achievement> achievements;
    /** Logger for logging quest related attributes. */
    private static final Logger logger = LoggerFactory.getLogger(QuestManager.class);
    /** Sound effect for quest completion. */
    private final Sound questComplete = ServiceLocator.getResourceService().getAsset("sounds/QuestComplete.wav", Sound.class);
    private final Sound achievementComplete = ServiceLocator.getResourceService().getAsset("sounds/achievement-sound.mp3", Sound.class);
    /** Map of relevant quests. As of Sprint 1 the String[] should contain only one quest as only one is accessed*/

    private final Entity player;


    /**Constructs questManager instance */
    public QuestManager(Entity player) {
        this.quests = new LinkedHashMap<>();
        this.player = player;
        AchievementManager achievementManager = new AchievementManager();
        this.achievements =  achievementManager.getAchievements();
        setupAchievements();
        player.getEvents().addListener("defeatedEnemy",this::handleEnemyQuest);
        player.getEvents().addListener("landBossDefeated",
              () ->  player.getEvents().trigger("defeatLandBoss"));
        player.getEvents().addListener("waterBossDefeated",
                () ->  player.getEvents().trigger("defeatWaterBoss"));
        player.getEvents().addListener("airBossDefeated",
                () ->  player.getEvents().trigger("defeatAirBoss"));
    }

    private void handleEnemyQuest(Entity enemy) {
        String type = enemy.getEnemyType().toString();
        player.getEvents().trigger("defeat" + type);

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
        logger.info(item.getName());
        player.getEvents().trigger(item.getName() + "Advancement");
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
                !quest.isFailed()
                && Objects.equals(taskName, quest.getTasks().get(quest.getProgression()).getTaskName())
                && quest.isActive();
    }

    public ArrayList<QuestBasic> getActiveQuests() {
        ArrayList<QuestBasic> newList = new ArrayList<>();
        for(QuestBasic quest : quests.values()) {
            if(quest.isActive() || quest.isQuestCompleted()) {
                newList.add(quest);
            }
        }
        return newList;
    }

    /**
     * Completes the task of the updates the quest progression.
     * @param quest The quest to be completed.
     */
    private void completeTask(QuestBasic quest) {
        ; //advance quest progression
        if (quest.progressQuest(player)) {
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

        for(QuestBasic questCheck : quests.values()) {
            boolean newActive = true;
            if(questCheck.getFollowQuests() != null) {
                for(String name : questCheck.getFollowQuests()) {
                    if(quests.containsKey(name)) {
                        newActive = getQuest(name).isQuestCompleted();
                    }
                }
            }
            questCheck.setActive(newActive);
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
            SaveHandler.save(Achievements.class, "saves/achievement", FileLoader.Location.LOCAL);
            logger.info("{} Completed!", achievement.getQuestName());
        }
    }

    @Override
    public void dispose() {
        SaveHandler.save(Achievements.class, "saves/achievement", FileLoader.Location.LOCAL);
        super.dispose();
    }




}
