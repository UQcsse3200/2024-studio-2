package com.csse3200.game.Overlays;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;

import java.util.ArrayList;
import java.util.List;

public class Overlay {
    List<Entity> entities = new ArrayList<>();

    public Overlay(){}

    public void remove() {
        for (Entity entity : entities) {
            entity.dispose();
        }
        entities.clear();

    }

    public void rest() {
        for (Entity entity : entities) {
            entity.setEnabled(false);
            ServiceLocator.getEntityService().unregister(entity);
        }
    }

    public void wake() {
        for (Entity entity : entities) {
            entity.setEnabled(true);
            ServiceLocator.getEntityService().register(entity);
        }
    }

    public enum MenuType {
        PAUSE_OVERLAY, QUEST_OVERLAY
    }
}
