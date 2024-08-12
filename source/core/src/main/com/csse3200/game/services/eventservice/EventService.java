package com.csse3200.game.services.eventservice;

import com.csse3200.game.events.EventHandler;
import com.csse3200.game.events.listeners.*;
import java.util.ArrayList;
import java.util.Objects;

public class EventService {
    public ArrayList<GlobalEvent> globalEvents;
    public EventHandler globalEventHandler;

    public EventService() {
        globalEvents = new ArrayList<>();
        globalEventHandler = new EventHandler();
        // Events should Load from config
        //example loading basic step event
        globalEvents.add(new GlobalEvent("steps"));
        for(GlobalEvent event : globalEvents) {
            globalEventHandler.addListener(event.eventName, this::recordEvent);
        }
    }

    /*
    createEvent adds a new event to the Eventhandler and sets a function to run on its triggering.
    eventName is the name of the event
    questId
    listener is the listener function that is called when the event is triggered
     */
    public void createEvent(String eventName, EventListener0 listener) {
        globalEvents.add(new GlobalEvent(eventName));
        globalEventHandler.addListener(eventName, listener);
    }

    public <T> void createEvent(String eventName, EventListener1<T> listener) {
        globalEvents.add(new GlobalEvent(eventName));
        globalEventHandler.addListener(eventName, listener);
    }

    public <T0, T1> void createEvent(String eventName, EventListener2<T0, T1> listener) {
        globalEvents.add(new GlobalEvent(eventName));
        globalEventHandler.addListener(eventName, listener);
    }


    public void recordEvent(String eventName) {
        for (GlobalEvent globalEvent : this.globalEvents) {
            if (Objects.equals(globalEvent.eventName, eventName)) {
                // Can re-add listener here for multiple events
                globalEvent.recordEvent();
            }
        }
    }

    public ArrayList<GlobalEvent> getGlobalEvents() {
        return globalEvents;
    }
}
