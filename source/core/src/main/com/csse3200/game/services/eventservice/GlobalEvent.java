package com.csse3200.game.services.eventservice;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class GlobalEvent  {
    public String eventName;
    private static final Logger logger = LoggerFactory.getLogger(GlobalEvent.class);

    /*
    Constructor
     */
    public GlobalEvent(String eventName) {
        this.eventName = eventName;
    }

    public void recordEvent() {
        // Send trigger to quest here accessing information about this GlobalEvent
        // e.g. QuestManager.getInstance().QuestHandler.trigger(this.eventName);
        // will send a trigger with the eventName as the argument to questId listener in an event handler called QuestHandler in the QuestManager singleton class
        if (Objects.equals(this.eventName, "steps")) {
            logger.info("Steps Event Recorded");
        }
        else {
            logger.info("Event Recorded");
        }
    }
}
