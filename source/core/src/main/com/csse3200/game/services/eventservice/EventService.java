package com.csse3200.game.services.eventservice;

import com.csse3200.game.events.EventHandler;

public class EventService {
    public EventHandler globalEventHandler;

    public EventService() {
        globalEventHandler = new EventHandler();
    }

    public void dispose() {
        globalEventHandler.dispose();
    }
}
