package com.csse3200.game.Overlays;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;

import java.util.ArrayList;
import java.util.List;

public class Overlay {
    private final List<Entity> entities = new ArrayList<>();

    public Overlay(){}

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
}
