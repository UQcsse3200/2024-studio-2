package com.csse3200.game.components.stats;

import com.badlogic.gdx.utils.Array;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.inventory.items.AbstractItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.csse3200.game.components.stats.StatSaveManager.saveStats;

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

        // 1. Create json for stats to be tracked
        // 2. Create StatSaveManager to load the json stats in
        // 3. Create setup handle and subscribe to achievements functions
        StatSaveManager statSaveManager = new StatSaveManager();
        this.stats = statSaveManager.getStats();
        setupStats();
    }

    /**
     * Initialise stats within the game and setup listener to handle a
     * certain update event.
     * Currently, the only update event is collecting items.
     */
    private void setupStats() {
        // Event for defeating an enemy
        player.getEvents().addListener("addItem", this::handleCollection);
        for (Stat stat : stats) {
            subscribeToStatEvents(stat);
        }
    }

    /**
     * Subscribes to item triggers and sends it as a specific stat collection trigger.
     */
    private void handleCollection(AbstractItem item){
        player.getEvents().trigger(item.getName() + "Collected");
    }

    /**
     * Adds a listener for the stat, which updates the stat when triggered.
     * Currently only tracks item collection.
     * @param stat The stat being listened to.
     */
    private void subscribeToStatEvents(Stat  stat) {
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
                logger.info("Total {}: {}", stat.getStatName(), stat.getCurrent());
            } else {
                logger.info("stat not found in stats");
            }
        }
    }

    @Override
    public void dispose() {
        saveStats(stats);
        super.dispose();
    }
}
