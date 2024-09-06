package com.csse3200.game.components.stats;

import com.csse3200.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;

/**
 * Class to store Stats and listen for events to update them.
 *
 */
public class StatManager extends Component {

    /** Data structure for all stats, the string is the value returned by Stat.getStatName()
     */
    private final HashMap<String, Stat> stats;
    private static final Logger logger = LoggerFactory.getLogger(StatManager.class);

    public StatManager() {
        this.stats = new HashMap<>();
    }

    /** Adds a stat to be tracked in the game and registers a listener with the EventService
     *
     * @param stat: a Stat object to be tracked
     */
    public void addStat(Stat stat) {
        stats.put(stat.getStatName(), stat);
    }

    /** Gets a Stat object from within the StatManager
     * @param statName the name of the stat
     * @return the Stat object
     */
    public Stat getStat(String statName) {
        return stats.get(statName);
    }

    /** Handler for event triggering an update of the a stat
     *
     * @param statName: the string triggering the update
     * @param value: the new value of the stat
     */
    public void updateStat(String statName, String operation, int value) {
        Stat stat = stats.get(statName);
        if (stat != null) {
            logger.info("Updating {} with {} to {}", stat.getStatName(), operation, value);
            stat.update(operation, value);
        } else {
            logger.info("stat not found in stats");
        }
    }

}
