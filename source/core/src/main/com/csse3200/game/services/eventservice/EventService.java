package com.csse3200.game.services.eventservice;

import com.csse3200.game.events.EventHandler;

import java.util.ArrayList;

public class EventService {
    public ArrayList<GlobalEvent> globalEvents;
    public EventHandler globalEventHandler;

    public EventService() {
        globalEvents = new ArrayList<>();
        globalEventHandler = new EventHandler();
        // Events should Load from config
        globalEvents.add(new GlobalEvent("steps", 0));
        for(GlobalEvent event : globalEvents) {
            globalEventHandler.addListener(event.eventName, this::recordEvent);
        }
    }

    public void recordEvent(int questId) {
        for (GlobalEvent globalEvent : this.globalEvents) {
            if (globalEvent.questId == questId) {
                // Can re-add listener here for multiple events
                globalEvent.recordEvent();
            }
        }
    }

    public ArrayList<GlobalEvent> getGlobalEvents() {
        return globalEvents;
    }
}
