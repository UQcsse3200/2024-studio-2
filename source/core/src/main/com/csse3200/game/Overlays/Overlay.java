package com.csse3200.game.Overlays;

import com.csse3200.game.entities.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Overlay {
    private final List<Entity> entities = new ArrayList<>();
    public final OverlayType overlayType;

    public Overlay(OverlayType overlayType){
        this.overlayType = overlayType;
    }

    public void add(Entity entity) {
        entities.add(entity);
    }

    public void remove() {
        for (Entity entity : this.entities) {
            entity.dispose();
        }
        this.entities.clear();

    }

    public void rest() {
        for (Entity entity : this.entities) {
            entity.setEnabled(false);
        }
    }

    public void wake() {
        for (Entity entity : this.entities) {
            entity.setEnabled(true);
        }
    }

    public enum OverlayType {
        PAUSE_OVERLAY, QUEST_OVERLAY
    }

    public static HashMap<OverlayType, Boolean> getNewActiveOverlayList(){
        HashMap<OverlayType, Boolean> overlayList = new HashMap<OverlayType, Boolean>();
        for (OverlayType overlayType : OverlayType.values()) {
            overlayList.put(overlayType, false);
        }
        return overlayList;
    }
}
