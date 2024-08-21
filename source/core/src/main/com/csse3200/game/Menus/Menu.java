package com.csse3200.game.Menus;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    List<Entity> entities = new ArrayList<>();

    public Menu(){}

    public void remove() {
        for (Entity entity : entities) {
            entity.dispose();
        }
        entities.clear();

    }

    public void pause() {
        for (Entity entity : entities) {
            entity.setEnabled(false);
            ServiceLocator.getEntityService().unregister(entity);
        }
    }

    public void resume() {
        for (Entity entity : entities) {
            entity.setEnabled(true);
            ServiceLocator.getEntityService().register(entity);
        }
    }

    public enum MenuType {
        PAUSE_MENU, QUEST_MENU
    }
}
