package com.csse3200.game.components.stats;

import com.badlogic.gdx.utils.Array;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.gamestate.GameState;
import com.csse3200.game.gamestate.SaveHandler;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.minigames.MiniGameMedals;
import com.csse3200.game.minigames.MiniGameNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to store Stats and listen for events to update them.
 */
public class StatManager extends Component {
    /** Data structure for all stats */
    private final Array<Stat> stats;
    /** Logger for tracking stat manager events */
    private static final Logger logger = LoggerFactory.getLogger(StatManager.class);
    /** Player entity stats are being tracked for */
    private final Entity player;

    public StatManager(Entity player) {
        this.player = player;
//        StatSaveManager statSaveManager = new StatSaveManager();
        this.stats = GameState.stats.stats;
        setupStats();
    }

    /**
     * Initialise stats within the game and setup listener to handle a
     * certain update event.
     * Currently, the only update event is collecting items.
     */
    void setupStats() {
        // Event for defeating an enemy
        player.getEvents().addListener("addItem", this::handleCollection);
        player.getEvents().addListener("defeatedEnemy", this::handleCombatWin);
        player.getEvents().addListener("miniGameStats", this::handleMiniGameScore);
        for (Stat stat : stats) {
            subscribeToStatEvents(stat);
        }
    }

    /**
     * Subscribes to item triggers and sends it as a specific stat collection trigger.
     */
    void handleCollection(AbstractItem item){
        player.getEvents().trigger(item.getName() + " Collected");
    }

    /**
     * Subscribes to combat win triggers and sends it as a specific stat collection trigger.
     */
    void handleCombatWin(Entity enemy){
        player.getEvents().trigger(enemy.getEnemyType().toString() + " Defeated");
    }

    /**
     * Subscribes to mini-game triggers and sends it as a specific achievement completion trigger.
     */
    private void handleMiniGameScore(MiniGameNames minigame){
        player.getEvents().trigger(minigame.name() + " Medal");
    }

    /**
     * Adds a listener for the stat, which updates the stat when triggered.
     * Currently only tracks item collection.
     * @param stat The stat being listened to.
     */
    private void subscribeToStatEvents(Stat stat) {
        player.getEvents().addListener(stat.getStatName(), () -> this.incrementStat(stat.getStatName(), "add", 1));
    }

    /**
     * Get all the stats within the game.
     * @return stats The array of stats
     */
    public Array<Stat> getAllStats() {
        return stats;
    }

    /** Handler for event triggering an update of the a stat
     *
     * @param statName: the string triggering the update
     * @param value: the new value of the stat
     */
    public void incrementStat(String statName, String operation, int value) {
        for (Stat stat : stats) {
            if (stat.getStatName().equals(statName)) {
                logger.info("Updating {} with {} by {}", stat.getStatName(), operation, value);
                stat.update(operation, value);
            } else {
                logger.info("stat not found in stats");
            }
        }
        SaveHandler.getInstance().save(GameState.class, "saves", FileLoader.Location.LOCAL);
    }

    @Override
    public void dispose() {
        SaveHandler.getInstance().save(GameState.class, "saves", FileLoader.Location.LOCAL);
        super.dispose();
    }
}
