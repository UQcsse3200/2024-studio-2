package com.csse3200.game.overlays;

import com.csse3200.game.entities.Entity;

import java.util.*;


/**
 * Represents a general overlay in the game that manages a collection of entities.
 *  provides functionality for adding, removing, and managing
 * entities within the overlay.
 */
public class Overlay {
    private final List<Entity> entities = new ArrayList<>();
    public final OverlayType overlayType;

    /**
     * Constructs an overlay with a specified type.
     * @param overlayType the type of the overlay
     */
    public Overlay(OverlayType overlayType){
        this.overlayType = overlayType;
    }


    /**
     * Adds an entity to the overlay.
     * @param entity the entity to add
     */
    public void add(Entity entity) {
        entities.add(entity);
    }


    /**
     * Removes all entities from the overlay and disposes of them.
     * This method clears the list of entities and disposes each one, releasing any
     * associated resources.
     */
    public void remove() {
        for (Entity entity : this.entities) {
            entity.dispose();
        }
        this.entities.clear();
    }


    /**
     * Puts the overlay into a resting state by disabling all entities.
     */
    public void rest() {
        for (Entity entity : this.entities) {
            entity.setEnabled(false);
        }
    }

    /**
     * Wakes the overlay by enabling all entities.
     */
    public void wake() {
        for (Entity entity : this.entities) {
            entity.setEnabled(true);
        }
    }

    /**
     * Enumeration of different types of overlays.
     */
    public enum OverlayType {
        PAUSE_OVERLAY, QUEST_OVERLAY, SETTINGS_OVERLAY
    }

    /**
     * Creates a map of all overlay types with their active status set to false.
     * @return a map with ative status
     */
    public static Map<OverlayType, Boolean> getNewActiveOverlayList(){
        Map<OverlayType, Boolean> overlayList = new EnumMap<>(OverlayType.class);
        for (OverlayType overlayType : OverlayType.values()) {
            overlayList.put(overlayType, false);
        }
        return overlayList;
    }
}
