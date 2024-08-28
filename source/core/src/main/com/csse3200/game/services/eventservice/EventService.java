package com.csse3200.game.services.eventservice;

import com.csse3200.game.events.EventHandler;

public class EventService {
    private final EventHandler globalEventHandler;

    public EventService() {
        globalEventHandler = new EventHandler();
    }

    public EventHandler getGlobalEventHandler() {
        return globalEventHandler;
    }

    public void dispose() {
        globalEventHandler.dispose();
    }
}
