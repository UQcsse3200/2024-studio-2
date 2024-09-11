package com.csse3200.game.components.stats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatDisplay {

    private final StatManager statManager;
    private static final Logger logger = LoggerFactory.getLogger(StatDisplay.class);
    public StatDisplay(StatManager statManager) {
        this.statManager = statManager;
    }

    /**
     * Display a single stat.
     * @param stat the Stat object to be displayed
     */
    public void displayStat(Stat stat) {
        StringBuilder statDisplay = new StringBuilder();
        statDisplay.append(stat.getStatDescription())
                .append(": ")
                .append(stat.getCurrent());
        if (stat.hasMax()) {
            statDisplay.append(" / ").append(stat.getMax());
        }
        //System.out.println(statDisplay.toString());
        logger.info("stat is {}", statDisplay.toString());
    }
}
